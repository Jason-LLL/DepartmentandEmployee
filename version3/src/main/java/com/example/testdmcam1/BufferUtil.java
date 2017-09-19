package com.example.testdmcam1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by Administrator on 2017/8/24 0024.
 */

/*
  OpenGL 是一个非常底层的画图接口，它所使用的缓冲区存储结构是和我们的 Java 程序中不相同的。
  Java 是大端字节序(BigEdian)，而 OpenGL 所需要的数据是小端字节序(LittleEdian)。
  所以，我们在将 Java 的缓冲区转化为 OpenGL 可用的缓冲区时需要作一些工作。
  该类是一个工具类, 把我们的float[]数组的数据 和 int[]数组的数据转换成符合opengl的buffer
 */
public class BufferUtil {

    public static FloatBuffer mBuffer;
    public static FloatBuffer floatToBuffer(float[] a){
        //先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length*4);
        //数组排序用nativeOrder
        mbb.order(ByteOrder.nativeOrder());
        mBuffer = mbb.asFloatBuffer();
        mBuffer.put(a);
        mBuffer.position(0);
        return mBuffer;
    }
    public static IntBuffer intToBuffer(int[] a){

        IntBuffer intBuffer;
        //先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer mbb = ByteBuffer.allocateDirect(a.length*4);
        //数组排序用nativeOrder
        mbb.order(ByteOrder.nativeOrder());
        intBuffer = mbb.asIntBuffer();
        intBuffer.put(a);
        intBuffer.position(0);
        return intBuffer;
    }
}

