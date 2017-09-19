package com.example.testdmcam1;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;


import com.smarttof.dmcam.dev_param_e;
import com.smarttof.dmcam.dev_t;
import com.smarttof.dmcam.dmcam;
import com.smarttof.dmcam.dmcamParamArray;
import com.smarttof.dmcam.log_level_e;
import com.smarttof.dmcam.param_item_t;

public class TestDmcam extends Activity implements OnClickListener{
	public static TestDmcam td;
	public static FrameLayout layoutDmcamPreview;
	private Button btnCapture,btnStop,btnExit;
	private TextView tvBase,tvAdvance;
	private CheckBox checkBox;
	private TextView tvLog;
	private ScrollView svLog;
	private UsbManager usbManager;
	private UsbDevice usbDevice;
	private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
	private PendingIntent pendingIntent;
	static int fd = -1;
	static int dev_power,dev_exp,dev_corr;
	static long dev_freq,dev_frame;
	private boolean devCapturing = false;
	private static DmcamSurfaceView dmcamView;
	private static  dev_t dev = null; // current dmcam dev handler
	private Handler handler = new Handler() {
		@SuppressLint("ShowToast")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 3:
				// Toast.makeText(TestDmcam.this, "33333333333333333",
				// 0).show();
				break;
			case 4:
				// Toast.makeText(TestDmcam.this, "44444444444444444",
				// 0).show();
				break;
			}
		}
	};
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (devCapturing && dev != null) {
			Thread th = new MyThread2();
			th.start();
			try {
				th.join(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mUsbReceiver);
		super.onDestroy();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_testdmcam);
		td=this;
		//DebugHandler.getInstance().init(getApplicationContext());
		usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
		pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
				ACTION_USB_PERMISSION), 0);//发送一条usb允许的广播
		IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
		registerReceiver(mUsbReceiver, filter);//注册广播和IntentFilter实例

		btnCapture = (Button)findViewById(R.id.btnCapture);
		btnStop = (Button) findViewById(R.id.btnStop);
		btnExit = (Button) findViewById(R.id.btnExit);
		tvBase  =(TextView)findViewById(R.id.tv_base2);
		tvAdvance = (TextView)findViewById(R.id.tv_advanced2);
		checkBox =(CheckBox)findViewById(R.id.ckb);
		tvLog = (TextView) findViewById(R.id.tvLog);
		svLog = (ScrollView) findViewById(R.id.svLog);
		layoutDmcamPreview = (FrameLayout) findViewById(R.id.layoutDmcamPreview);
		//spin1=(Spinner)findViewById(R.id.spin1);
		//spin2=(Spinner)findViewById(R.id.spin2);
		//tvbase=(TextView)findViewById(R.id.tv_base);
		//tvadvanced=(TextView)findViewById(R.id.tv_advanced);
		//tvPowerValue = (TextView) findViewById(R.id.tvPowerValue);
		//tvExposureValue = (TextView) findViewById(R.id.tvExposureValue);
		//tvMinDistance=(TextView)findViewById(R.id.tvMinDistance);
		//tvMaxDistance=(TextView)findViewById(R.id.tvMaxDistance);
		//sbarPower = (SeekBar) findViewById(R.id.sbarPower);
		//sbarExposure = (SeekBar) findViewById(R.id.sbarExposure);
        //sbarMinDistance=(SeekBar)findViewById(R.id.sbarMinDistance);
		//sbarMaxDistance=(SeekBar)findViewById(R.id.sbarMaxDistance);
		//tvbase.setOnClickListener(this);
		//tvadvanced.setOnClickListener(this);
		//sbarPower.setOnSeekBarChangeListener(new sbarPowerChangeListener());
		//sbarExposure
		//		.setOnSeekBarChangeListener(new sbarExposureChangeListener());
        //sbarMinDistance.setOnSeekBarChangeListener(new sbarMinDistanceChangeListener());
		//sbarMaxDistance.setOnSeekBarChangeListener(new sbarMaxDistanceChangeListener());
		//spin2.setOnItemSelectedListener(new spinItemSelectedListener2());
		tvBase.setOnClickListener(this);
		tvAdvance.setOnClickListener(this);
		tvLog.setText("");
		tvLog.setMovementMethod(new ScrollingMovementMethod());
		dmcamView = new DmcamSurfaceView(this);
		layoutDmcamPreview.addView(dmcamView);
		dmcam.log_cfg(log_level_e.LOG_LEVEL_INFO, log_level_e.LOG_LEVEL_INFO,
				log_level_e.LOG_LEVEL_NONE);
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
				if(b){
					svLog.setVisibility(View.VISIBLE);
				}
				else{
					svLog.setVisibility(View.GONE);
				}
			}
		});
	}
	public static  DmcamSurfaceView getDmcamView()
	{
		return dmcamView;
	}
	public static  dev_t getDev()
	{
		return dev;
	}


	public void logUI(String tag, String msg) {
		Log.i(tag, msg);

		if (!msg.endsWith("\n")) {
			msg += "\n";
		}
		final String _msg = msg;
		runOnUiThread(new Runnable() {
			public void run() {
				tvLog.append(_msg);
				svLog.scrollTo(0, svLog.getBottom());
			}
		});
	}
	public void onClick_Capture(View view) {
		// Toast.makeText(getApplicationContext(), "start test",
		// Toast.LENGTH_LONG)
		// .show();
		logUI("test", "capture button clicked\n");
		new MyThread2().start();

	}

	public void onClick_Stop(View view ) {
		logUI("test", "stop button click\n");
		btnCapture.setClickable(true);
		btnStop.setClickable(false);
		if (devCapturing && dev != null) {
			runOnUiThread(new Runnable() {
				public void run() {
                 dmcamView.stop();
				}
			});
			logUI("DMCAM", "Stop capture ...");
			dmcam.cap_stop(dev);
			logUI("DMCAM", "Close dmcam device ..");
			dmcam.dev_close(dev);
			dev = null;
			devCapturing = false;
			return;
		}
	}

    public void onClick_Exit(View view){
		this.finish();
	}



	public boolean devSetIllumPower(dev_t dev, int percent) {
		if (dev == null)
			return false;

		param_item_t wparam = new param_item_t();
		// dmcamParamArray wparams = new dmcamParamArray(1);

		wparam.setParam_id(dev_param_e.PARAM_ILLUM_POWER);
		wparam.getParam_val().getIllum_power().setPercent((short) percent);
		// wparams.setitem(0, wparam);

		logUI("DMCAM",
				String.format("set pwr = %d %%\n", wparam.getParam_val()
						.getIllum_power().getPercent()));
		if (!dmcam.param_batch_set(dev, wparam, 1)) {
			logUI("DMCAM",
					String.format(" set illu_power to %d %% failed\n", percent));
			return false;
		}
		return true;
	}
	public boolean devSetExposure(dev_t dev, int expoUs) {
		if (dev == null)
			return false;

		param_item_t wparam = new param_item_t();
		dmcamParamArray wparams = new dmcamParamArray(1);

		wparam.setParam_id(dev_param_e.PARAM_INTG_TIME);
		wparam.getParam_val().getIntg().setIntg_us(expoUs);

		wparams.setitem(0, wparam);
		logUI("DMCAM",
				String.format("set exposure = %d us\n", wparams.getitem(0)
						.getParam_val().getIntg().getIntg_us()));
		if (!dmcam.param_batch_set(dev, wparams.cast(), 1)) {
			logUI("DMCAM",
					String.format(" set exposure to %d us failed\n", expoUs));
			return false;
		}
		return true;
	}
	public boolean devSetModifyFreq(dev_t dev,long freq) {
		if(dev==null)
			return false;
		param_item_t wparam = new param_item_t();
		wparam.setParam_id(dev_param_e.PARAM_MOD_FREQ);
		wparam.getParam_val().setMod_freq(freq);
		logUI("DMCAM",String.format("set Mod_freq= %d \n",wparam.getParam_val().getMod_freq()));
		if (!dmcam.param_batch_set(dev, wparam, 1)) {
			logUI("DMCAM",
					String.format(" set Mod_freq =%d failed\n", freq));
			return false;
		}
		return true;
	}
    public boolean devSetFrameRate(dev_t dev,long frameRate){
		if(dev==null)
			return false;
		param_item_t wparam = new param_item_t();
		wparam.setParam_id(dev_param_e.PARAM_FRAME_RATE);
		wparam.getParam_val().getFrame_rate().setFps(frameRate);
		logUI("DMCAM",String.format("set frameRate = %dfps\n",frameRate));
		if (!dmcam.param_batch_set(dev, wparam, 1)) {
			logUI("DMCAM",
					String.format(" set illu_power to %dfps failed\n", frameRate));
			return false;
		}
		return true;
	}
    public boolean devSetRevise(dev_t dev,int reviseValue){
		if(dev==null)
			return false;
		param_item_t wparam = new param_item_t();
		wparam.setParam_id(dev_param_e.PARAM_PHASE_CORR);
		wparam.getParam_val().getPhase_corr().setCorr1(reviseValue);
		wparam.getParam_val().getPhase_corr().setCorr2(reviseValue);
		logUI("DMCAM",String.format("set Phase_corr = %d \n",reviseValue));
		if(!dmcam.param_batch_set(dev,wparam,1)){
			logUI("DMCAM",String.format("set Phase_corr = %d failed\n",reviseValue));
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View view) {
		//Intent intent = new Intent();
		//intent.setClass(TestDmcam.this,ParamSetting.class);
		//startActivity(intent);
		//startActivityForResult(intent,0);
		switch(view.getId()) {
			case R.id.tv_base2:
				MyDialog myDialog = new MyDialog(this);
				Window dialogWindow = myDialog.getWindow();
				WindowManager.LayoutParams lp = dialogWindow.getAttributes();
				lp.width = WindowManager.LayoutParams.MATCH_PARENT;
				lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
				dialogWindow.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
				dialogWindow.setAttributes(lp);
				myDialog.show();
				break;
			case R.id.tv_advanced2:
				MyDialog2 myDialog2 = new MyDialog2(this);
				Window dialogWindow2 = myDialog2.getWindow();
				WindowManager.LayoutParams lp2 = dialogWindow2.getAttributes();
				lp2.width = WindowManager.LayoutParams.MATCH_PARENT;
				lp2.height = WindowManager.LayoutParams.WRAP_CONTENT;
				dialogWindow2.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);
				dialogWindow2.setAttributes(lp2);
				myDialog2.show();
		}
	}

/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode ==0 && resultCode ==1)
		{
         Bundle bundle = data.getExtras();
		 dev_power = bundle.getInt("DevPower");
		 dev_exp = bundle.getInt("FinalExposure");
		}
	}
*/
	class MyThread2 extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				//判段device是否正在采集中,若在采集的话需要停止采集
                 /*
				if (devCapturing && dev != null) {
					runOnUiThread(new Runnable() {
						public void run() {
							btnCapture.setText("开始采集");
							dmcamView.stop();
						}
					});

					logUI("DMCAM", "Stop capture ...");
					dmcam.cap_stop(dev);
					logUI("DMCAM", "Close dmcam device ..");
					dmcam.dev_close(dev);
					dev = null;
					devCapturing = false;
					return;
				}*/
				//若没有在采集.需要查询是否有dcam设备
				UsbDevice ud = null;
				// 链接到设备的USB设备列表
				HashMap<String,UsbDevice> map = usbManager.getDeviceList();

				Collection<UsbDevice> usbDevices = map.values();
				Iterator<UsbDevice> usbDeviceIter = usbDevices.iterator();
				while (usbDeviceIter.hasNext()) {
					ud = usbDeviceIter.next();
					// VendorID(4379) 和ProductID(4452) 十进制
					if (0x111B == ud.getVendorId()
							&& 0x1238 == ud.getProductId()) {
						usbDevice = ud;
						logUI("device", "find dmcam device"); // bhw
					}
					logUI("device", String.format(
							"find usb device: vid=0x%X, pid=0x%X, id=0x%x",
							ud.getVendorId(), ud.getProductId(),
							ud.getDeviceId()));
				}

				// 检测USB设备权限是否授权
				if (usbManager.hasPermission(usbDevice)) {
					handler.sendEmptyMessage(3);

					logUI("device", "usbManager.hasPermission");// bhw
					new MyThread3().start();
				} else {
					// 如果没有授权就授予权限
					handler.sendEmptyMessage(4);
					usbManager.requestPermission(usbDevice, pendingIntent); // 在此Android系统会弹出对话框，选择是否授予权限
					logUI("device", "usbManager.requestPermission"); // bhw
				}
			} catch (Exception e) {
				logUI("device", "no dmcam device found");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static int openUsbDevice(int vid, int pid)
			throws InterruptedException {

		Log.e("device", "openUsbDevice  " + fd);
		return fd;

	}

	class MyThread3 extends Thread {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Log.w("device", "MyThread3");// bhw
			UsbDeviceConnection connection = usbManager.openDevice(usbDevice);
			fd = connection.getFileDescriptor();// 获取文件描述符
			Log.w("device", "MyThread3  " + fd);
			dev = dmcam.dev_open_by_fd(fd);//根据File Descriptor打开设备
			if (dev == null) {
				logUI("device", "open failed!\n");
				return;
			}
			logUI("DMCAM", "device open ok with fd!\n");
           // SeekBar sbarPower = (SeekBar) ParamSetting.ps.getSupportFragmentManager().findFragmentById(R.id.content).getView().findViewById(R.id.sbarPower);
			//eekBar sbarExposure = (SeekBar) ParamSetting.ps.getSupportFragmentManager().findFragmentById(R.id.content).getView().findViewById(R.id.sbarExposure);

			//LayoutInflater factory = LayoutInflater.from(TestDmcam.this);
			//View layout = factory.inflate(R.layout.activity_setting,null);
			//SeekBar sbarPower = (SeekBar) layout.findViewById(R.id.sbarPower);
			//SeekBar sbarExposure = (SeekBar) layout.findViewById(R.id.sbarExposure);

            dev_power = MyDialog.getPower();
			dev_exp = MyDialog.getExp();
			dev_freq= MyDialog2.getModFreq();
			dev_frame=MyDialog2.getFrameRate();
			//dev_corr = MyDialog2.getReviseValue();
            devSetIllumPower(dev,dev_power);
			devSetExposure(dev,dev_exp);
			devSetModifyFreq(dev,dev_freq);
			devSetFrameRate(dev,dev_frame);
			//devSetRevise(dev,dev_corr);
			dmcam.cap_set_frame_buffer(dev, null, 10 * 320 * 240 * 4);
			// dmcam.cap_set_callback_on_error(dev, null);
			logUI("DMCAM", " Start dmcam view ...");

			logUI("DMCAM", " Start capture ...");
			devCapturing = dmcam.cap_start(dev);//返回true
			runOnUiThread(new Runnable() {
				public void run() {
					//btnCapture.setText("停止采集");
					dmcamView.start(dev);//显示采集到的数据
					btnCapture.setClickable(false);
					btnStop.setClickable(true);
				}
			});
			// ByteBuffer f = ByteBuffer.allocateDirect(320 * 240 * 4);
			// logUI("DMCAM", "start sampling ...");
			// int count = 0;
			// boolean run = false;
			//
			// float[] dist = new float[320 * 240];
			// while (run) {
			// // get one frame
			// frame_t finfo = new frame_t();
			// int ret = dmcam.cap_get_frames(dev, 1, f, f.capacity(), finfo);
			// if (ret < 0) {
			// logUI("DMCAM",
			// String.format("capture failed: code=%d\n", ret));
			// break;
			// }
			// if (ret > 0) {
			// byte[] firstSeg = new byte[4];
			// f.get(firstSeg, 0, 4);
			// // if (dmcam.cap_get_frame(dev, f, f.length, null)){
			// logUI("DMCAM",
			// String.format(
			// " frame @ %d, %d, %d [0x%02X, 0x%02X, 0x%02X, 0x%02X, ...]",
			// finfo.getFrame_fbpos(),
			// finfo.getFrame_count(),
			// finfo.getFrame_size(), firstSeg[0],
			// firstSeg[1], firstSeg[2], firstSeg[3]));
			//
			// // byte[] first_seg = new byte[16];
			// // f.get(first_seg, 0, 16);
			// // for (int n = 0; n < 16; n++) {
			// // Log.i("DMCAM", String.format("0x%02X, ", first_seg[n]));
			// // }
			// // Log.i("DMCAM", String.format("]\n"));
			// //
			// // dmcam.raw2dist(dist, dist.length, f, f.capacity());
			// // for (int n = 0; n < 16; n++) {
			// // Log.i("DMCAM", String.format("%.3f, ", dist[n]));
			// // }
			// // Log.i("DMCAM", String.format("]\n"));
			// count += 1;
			// if (count >= 100)
			// break;
			// }
			// }
			// // logUI("DMCAM", "Wait 3 seconds....");
			// // try {
			// // Thread.sleep(3000);
			// // } catch (InterruptedException ex) {
			// // Thread.currentThread().interrupt();
			// // }
			// logUI("DMCAM", "Stop capture ...");
			// dmcam.cap_stop(dev);
			// logUI("DMCAM", "Close dmcam device ..");
			// dmcam.dev_close(dev);
		}
	}

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();//获取Action内容
			Log.e("action", action);

			if (ACTION_USB_PERMISSION.equals(action)) {
				synchronized (this) {
					usbDevice = (UsbDevice) intent
							.getParcelableExtra(UsbManager.EXTRA_DEVICE);
					if (intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
						handler.sendEmptyMessage(1);//允许usb设备连接
						if (usbDevice != null) {
							new MyThread3().start();// MyThread3
						}
					} else {
						Log.d("denied", "permission denied for device "
								+ usbDevice);
					}

				}
			}
		}
	};
}
