package com.example.testdmcam1;

/**
 * Created by Administrator on 2017/8/22 0022.
 */


        import android.app.AlarmManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.os.Build;
        import android.util.Log;

        //import com.mediasoc.hewoxue.ui.CoreActivity;
      //  import com.socks.library.KLog;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.PrintWriter;
        import java.io.StringWriter;
        import java.io.Writer;
        import java.lang.Thread.UncaughtExceptionHandler;
        import java.lang.reflect.Field;
        import java.text.DateFormat;
        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.Locale;

public class DebugHandler implements UncaughtExceptionHandler {
    public static final String KEY_UPLOAD = "upload";
    public static final String KEY_FILE_PATH = "file_path";
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static DebugHandler instance = new DebugHandler();
    private Context mContext;
    private String deviceInfos;
    private String fileName;
    private File debugPath;
    public static DateFormat formatterDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static DateFormat formatterTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private DebugHandler() {
    }

    public static DebugHandler getInstance() {
        return instance;
    }

    /**
     * 初始化
     */
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        deviceInfos = getDeviceInfo(mContext);
        debugPath = new File(FileUtils.getAppCachePath(mContext) + "/debug");
    }

    /**
     * 当UncaughtException发生时会转入该函数来处理
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            Intent intent = new Intent(mContext.getApplicationContext(), TestDmcam.class);
            intent.putExtra(KEY_UPLOAD, true);
            intent.putExtra(KEY_FILE_PATH, new File(debugPath, fileName).getAbsolutePath());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent restartIntent = PendingIntent.getActivity(mContext.getApplicationContext(), 0, intent, Intent.FILL_IN_ACTION);
            //退出程序
            AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent); // 1秒钟后重启应用
            System.exit(1);
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        saveDebugInfo2File(ex);
        return true;
    }

    /**
     * 收集设备参数信息
     */
    public String getDeviceInfo(Context ctx) {
        StringBuilder deviceInfo = new StringBuilder();
       // deviceInfo.append("ver：")
                //.append(UpdateUtils.getAppVersion(ctx))
               // .append("\n");
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                deviceInfo.append(field.getName())
                        .append("=")
                        .append(field.get(null).toString()
                        ).append("\n");
              //  Log.d(field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return deviceInfo.toString();
    }

    /**
     * 保存错误信息到文件中
     *
     * @return 返回文件名称
     */
    private String saveDebugInfo2File(Throwable ex) {
        StringBuilder infoBuffer = new StringBuilder();
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        infoBuffer.append(result);
        try {
            String day = formatterDay.format(new Date());
           // String user = StringUtils.isEmpty(AccountHelper.getInstance().getName()) ?
                   // StringUtils.getRandomString(16) : AccountHelper.getInstance().getName();
            fileName = "lll"+ "-debug-" + day + ".txt";
            saveDeviceInfo2File(deviceInfos);
            saveLogInfo2File(infoBuffer.toString());
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveDeviceInfo2File(String deviceInfo) {
        try {
            if (FileUtils.checkSDcard()) {
                if (!debugPath.exists()) {
                    debugPath.mkdirs();
                }
                File logFile = new File(debugPath, fileName);
                if (!logFile.exists()) {
                    if (!logFile.createNewFile()) {
                        return;
                    }
                    FileOutputStream fos = new FileOutputStream(logFile, true);
                    fos.write(deviceInfo.getBytes());
                    fos.flush();
                    fos.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLogInfo2File(String log) {
        StringBuilder sb = new StringBuilder();
        try {
            String time = formatterTime.format(new Date());
            sb.append(time)
                    .append(" : ")
                    .append(log)
                    .append("\r\n");
            if (FileUtils.checkSDcard()) {
                if (!debugPath.exists()) {
                    debugPath.mkdirs();
                }
                File logFile = new File(debugPath, fileName);
                if (!logFile.exists()) {
                    if (!logFile.createNewFile()) {
                        return;
                    }
                }
                FileOutputStream fos = new FileOutputStream(logFile, true);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
