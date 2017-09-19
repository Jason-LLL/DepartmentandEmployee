package com.example.testdmcam1;

import java.nio.ByteBuffer;

import com.smarttof.dmcam.camera_para_t;
import com.smarttof.dmcam.dev_t;
import com.smarttof.dmcam.dmcam;
import com.smarttof.dmcam.frame_t;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder.Callback;

public class DmcamSurfaceView extends SurfaceView implements Runnable, Callback {
	private SurfaceHolder mHolder; // 用于控制SurfaceView
	private Thread t; // 声明一条线程
	private volatile boolean flag; // 线程运行的标识，用于控制线程
	private Canvas mCanvas; // 声明一张画布
	private Paint p; // 声明一支画笔
	//float m_circle_r = 10;
	private dev_t dev;
	public boolean refreshImage = false;
	public boolean lastRefreshImage= false;
	private ByteBuffer frameBuf = ByteBuffer.allocateDirect(320 * 240 * 4);
	public   float RNG_MIN=1000;//最小视距
	public   float RNG_MAX=4000;//最大视距
	public int VIEW_TYPE=0;
	public DmcamSurfaceView(Context context) {
		super(context);

		mHolder = getHolder(); // 获得SurfaceHolder对象
		mHolder.addCallback(this); // 为SurfaceView添加状态监听
		p = new Paint(); // 创建一个画笔对象
		p.setColor(Color.WHITE); // 设置画笔的颜色为白色
		setFocusable(true); // 设置焦点
	}

	public void start(dev_t dmcamDev) {
		dev = dmcamDev;
		refreshImage = true;
	}

	public void stop() {
		refreshImage = false;
        lastRefreshImage=false;
	}

	/**
	 * 当SurfaceView创建的时候，调用此函数
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		t = new Thread(this); // 创建一个线程对象
		flag = true; // 把线程运行的标识设置成true
		t.start(); // 启动线程
	}

	/**
	 * 当SurfaceView的视图发生改变的时候，调用此函数
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * 当SurfaceView销毁的时候，调用此函数
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false; // 把线程运行的标识设置成false
		mHolder.removeCallback(this);
	}

	/**
	 * 当屏幕被触摸时调用
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return true;
	}

	/**
	 * 当用户按键时调用
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		surfaceDestroyed(mHolder);
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void run() {
		float [] dist = new float [320 * 240];
		int distImgPixels[] = new int[dist.length];

		lastRefreshImage = !refreshImage;

		while (flag) {
			try {
				if (!refreshImage) {
					if (refreshImage != lastRefreshImage) {
						lastRefreshImage = refreshImage;
						synchronized (mHolder) {
							mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
							if (mCanvas != null) {
								Bitmap img = ((BitmapDrawable) getResources()
										.getDrawable(com.example.testdmcam1.R.drawable.cover))
										.getBitmap();//获取初始画面
								// mCanvas.drawBitmap(pic, 0, 0, paint);
								Rect src = new Rect(0, 0, img.getWidth() - 1,
										img.getHeight() - 1);
								Rect dst = new Rect(0, 0,
										mCanvas.getWidth() - 1,
										mCanvas.getHeight() - 1);
								mCanvas.drawBitmap(img, src, dst, null);
								mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
							}
						}
					}
					Thread.sleep(100);
				} else {
					lastRefreshImage = refreshImage;
					synchronized (mHolder) {
						// get one frame
						frame_t finfo = new frame_t();
						//frameBuf 指帧数据,该函数获取指定数目帧数据,并填入缓冲区
						int ret = dmcam.cap_get_frames(dev, 1, frameBuf,
								frameBuf.capacity(), finfo);
						if (ret < 0) {
							Log.e("DMCAM", String.format(
									"capture failed: code=%d\n", ret));
							break;
						}
						if (ret > 0) {
							// byte[] firstSeg = new byte[4];
							// frameBuf.get(firstSeg, 0, 4);
							// if (dmcam.cap_get_frame(dev, f, f.length, null)){
							// Log.d("DMCAM",
							// String.format(
							// " frame @ %d, %d, %d [0x%02X, 0x%02X, 0x%02X, 0x%02X, ...]",
							// finfo.getFrame_fbpos(),
							// finfo.getFrame_count(),
							// finfo.getFrame_size(), firstSeg[0],
							// firstSeg[1], firstSeg[2],
							// firstSeg[3]));
							//原始数据转换成距离数据
							if(VIEW_TYPE==0){
							dmcam.raw2dist(dist, dist.length, frameBuf,
									frameBuf.capacity());
								float min_dist = RNG_MIN/1000;
								float max_dist = RNG_MAX/1000;
							for (int n = 0; n < dist.length; n++) {
								//控制视距范围 ,并将距离数据映射为rgb
								int [] c= getColor(dist[n],min_dist,max_dist);
								//对每一个像素进行颜色设置
                                distImgPixels[n]=Color.rgb(c[0],c[1],c[2]);
							}
							    int w = 320;
								int h = 240;
								//根据颜色数组来创建位图 Config配置图像压缩质量参数
								Bitmap img = Bitmap.createBitmap(distImgPixels,
										w, h, Bitmap.Config.ARGB_8888);
								mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
								if (mCanvas != null) {
									Rect src = new Rect(0, 0,
											img.getWidth() - 1,
											img.getHeight() - 1);
									Rect dst = new Rect(0, 0,
											mCanvas.getWidth() - 1,
											mCanvas.getHeight() - 1);
									mCanvas.drawBitmap(img, src, dst, null);

									mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
								}
						}else if(VIEW_TYPE==1)
							{
								dmcam.raw2dist(dist, dist.length, frameBuf,
										frameBuf.capacity());
								for (int n = 0; n < dist.length; n++) {
                                    if(dist[n]>=1) {
										dist[n] = 0;
									}
									byte v = (byte)(dist[n] * 255);
									//对每一个像素进行颜色设置
									distImgPixels[n] = Color.rgb(v, v, v);
								}
								int w = 320;
								int h = 240;
								//根据颜色数组来创建位图 Config配置图像压缩质量参数
								Bitmap img = Bitmap.createBitmap(distImgPixels,
										w, h, Bitmap.Config.ARGB_8888);
								mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
								if (mCanvas != null) {
									Rect src = new Rect(0, 0,
											img.getWidth() - 1,
											img.getHeight() - 1);
									Rect dst = new Rect(0, 0,
											mCanvas.getWidth() - 1,
											mCanvas.getHeight() - 1);
									mCanvas.drawBitmap(img, src, dst, null);

									mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
								}
							}
							else if(VIEW_TYPE==2){
								dmcam.raw2gray(dist, dist.length, frameBuf,
										frameBuf.capacity());
								float max =dist[0];
								for (int n = 0; n < dist.length; n++) {
									if(dist[n]>max)
										max=dist[n];
								}
								for(int n = 0; n < dist.length; n++){
									byte v =(byte)((dist[n]/max)*255);//提升各个像素点亮度
									distImgPixels[n]=Color.rgb(v,v,v);
								}
								int w = 320;
								int h = 240;
								//根据颜色数组来创建位图 Config配置图像压缩质量参数
								Bitmap img = Bitmap.createBitmap(distImgPixels,
										w, h, Bitmap.Config.ARGB_8888);
									mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
								if (mCanvas != null) {
									Rect src = new Rect(0, 0,
											img.getWidth() - 1,
											img.getHeight() - 1);
									Rect dst = new Rect(0, 0,
											mCanvas.getWidth() - 1,
											mCanvas.getHeight() - 1);
									mCanvas.drawBitmap(img, src, dst, null);


									mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
								}
							}
							else if(VIEW_TYPE==3)
							{
								GLSurfaceView gView = new GLSurfaceView(this.getContext());
								gView.setRenderer(new DmcamGLSurfaceView());
								gView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
								TestDmcam.layoutDmcamPreview.addView(gView);
							}



                                /*
                                mCanvas= mHolder.lockCanvas();
                                p=new Paint();
                                ColorMatrix cm =new ColorMatrix();
                                cm.setSaturation(0);
                                ColorMatrixColorFilter f =new ColorMatrixColorFilter(cm);
                                p.setColorFilter(f);
                                */

                            }
							// Thread.sleep(100); // 让线程休息100毫秒
							// Draw(); // 调用自定义画画方法
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (mCanvas != null) {
					// mHolder.unlockCanvasAndPost(mCanvas);//结束锁定画图，并提交改变。

				}
			}
		}
	}

	private int[] getColor(float v, float min_dist, float max_dist) {
		int [] c = new int [3];//c[0] 为r; c[1]为g; c[2] 为b
		if(v<min_dist){
			v =min_dist;
		}
		if(v>=max_dist){
			v=max_dist;
		}
		float dv=max_dist-min_dist;
		/*把距离数据分成四个范围,分别给rgb设置映射值
		距离数据(s)<1/4;1/4<s<1/2;1/2<s<3/4;3/4<s<1
		 */
		if(v<(min_dist+0.25*dv)){
			c[0]=0;
			c[1]=(int)(4*(v-min_dist)/dv*255);
		}else if(v<(min_dist+0.5*dv)){
			c[0]=0;
			c[2]=(int)((1+4*(min_dist+0.25*dv-v)/dv)*255);
		}
		else if(v<(min_dist+0.75*dv)){
			c[0]=(int)((4*(v-min_dist-0.5*dv)/dv)*255);
			c[2]=0;
		}
		else{
			c[1]=(int)((1+4*(min_dist+0.75*dv-v)/dv)*255);
			c[2]=0;
		}
		return  c;
	}

	/**
	 * 自定义一个方法，在画布上画一个圆
	 */
	// protected void Draw() {
	// mCanvas = mHolder.lockCanvas(); // 获得画布对象，开始对画布画画
	// if (mCanvas != null) {
	// // Create bitmap with width, height, and 4 bytes color (RGBA)
	//
	// Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	// paint.setColor(Color.BLUE);
	// paint.setStrokeWidth(10);
	// paint.setStyle(Style.FILL);
	// if (m_circle_r >= (getWidth() / 10)) {
	// m_circle_r = 0;
	// } else {
	// m_circle_r++;
	// }
	// Bitmap pic = ((BitmapDrawable) getResources().getDrawable(
	// R.drawable.ic_launcher)).getBitmap();
	// mCanvas.drawBitmap(pic, 0, 0, paint);
	// for (int i = 0; i < 5; i++)
	// for (int j = 0; j < 8; j++)
	// mCanvas.drawCircle(
	// (getWidth() / 5) * i + (getWidth() / 10),
	// (getHeight() / 8) * j + (getHeight() / 16),
	// m_circle_r, paint);
	// mHolder.unlockCanvasAndPost(mCanvas); // 完成画画，把画布显示在屏幕上
	// }
	// }
}