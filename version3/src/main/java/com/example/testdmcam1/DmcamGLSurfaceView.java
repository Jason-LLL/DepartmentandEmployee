package com.example.testdmcam1;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.smarttof.dmcam.camera_para_t;
import com.smarttof.dmcam.dmcam;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

public class DmcamGLSurfaceView implements GLSurfaceView.Renderer {
    private ByteBuffer frameBuf = ByteBuffer.allocateDirect(320 * 240 * 4);
    private ByteBuffer srcBuf = ByteBuffer.allocateDirect(320 * 240 * 4);
    private float[] mColorArray={
            1f,0f,0f,1f,     //红
            0f,1f,0f,1f,     //绿
            0f,0f,1f,1f      //蓝
    };
    private FloatBuffer mColorBuffer;
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glShadeModel(GL10.GL_SMOOTH);
        gl10.glClearColor(1.0f,1.0f,1.0f,0f);
        gl10.glClearDepthf(1.0f);
        gl10.glEnable(GL10.GL_DEPTH_TEST);
        gl10.glDepthFunc(GL10.GL_LEQUAL);
        gl10.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h) {
        gl10.glViewport(0, 0, w, h);

        float ratio = (float) w / h;
        gl10.glMatrixMode(GL10.GL_PROJECTION);//是说我们现在改变的是坐标系与Surface的映射关系（投影矩阵
        gl10.glLoadIdentity();//是将以前的改变都清掉
        gl10.glFrustumf(-ratio, ratio, -1, 1, 1, 10);// 这个函数非常Powerful。它实现了Surface和坐标系之间的映射关系。它是以透视投影的方式来进行映射的。
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        camera_para_t cpt = new camera_para_t();
        float [] dist = new float [320 * 240];
        float [] f = {111.5f,85.5f,157f,157f,10000};
        cpt.setCx(f[0]);
        cpt.setCy(f[1]);
        cpt.setFx(f[2]);
        cpt.setFy(f[3]);
        dmcam.raw2dist(dist, dist.length, frameBuf, frameBuf.capacity());
        for(float dis:dist)
        {
            srcBuf.putFloat(dis);
        }
        float [] pcl = new float [320 * 240 * 4];
        dmcam.depth_to_pcl(pcl,pcl.length,srcBuf,320,240,(short)3,cpt);
        FloatBuffer VertexBuffer = FloatBuffer.allocate(pcl.length*4);
        VertexBuffer.put(pcl);
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
        //使用数组作为颜色
        gl10.glColorPointer(4,GL10.GL_FLOAT, 0, mColorBuffer);
        gl10.glLoadIdentity();
        gl10.glTranslatef(1.5f,0.0f,-6.0f);
        gl10.glVertexPointer(3,GL10.GL_FLOAT,0,VertexBuffer);
        gl10.glDrawElements(GL10.GL_POINTS,pcl.length,1,VertexBuffer);
    }
}
