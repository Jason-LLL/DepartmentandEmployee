package com.example.testdmcam1;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/4 0004.
 */

public class ParamSetting  extends Activity {
   // public static ParamSetting ps;
    private static int power=50,exp=200;
    private static int reviseValue;
    private String[] freq = {"12","24","36","48","60"};
    private String[] frame ={"30","15","60"};
    private TextView tvbase,tvadvanced;
    public  LinearLayout llbase,lladvanced;
    private SeekBar sbarPower, sbarExposure,sbarMinDistance,sbarMaxDistance,sbarRevise;
    private TextView tvPowerValue, tvExposureValue,tvMinDistance,tvMaxDistance,tvReviseValue;
    private Spinner spin1,spin2,spin_freq,spin_frame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(com.example.testdmcam1.R.layout.activity_setting);
        spin1 = (Spinner)findViewById(com.example.testdmcam1.R.id.spin1);
        spin2 = (Spinner)findViewById(com.example.testdmcam1.R.id.spin2);
        sbarPower = (SeekBar)findViewById(com.example.testdmcam1.R.id.sbarPower);
        sbarExposure = (SeekBar)findViewById(com.example.testdmcam1.R.id.sbarExposure);
        sbarRevise = (SeekBar)findViewById(R.id.sbarRevise);
        sbarMinDistance = (SeekBar)findViewById(com.example.testdmcam1.R.id.sbarMinDistance);
        sbarMaxDistance =(SeekBar)findViewById(com.example.testdmcam1.R.id.sbarMaxDistance);
        tvPowerValue=(TextView)findViewById(com.example.testdmcam1.R.id.tvPowerValue);
        tvExposureValue= (TextView)findViewById(com.example.testdmcam1.R.id.tvExposureValue);
        tvMinDistance =(TextView)findViewById(com.example.testdmcam1.R.id.tvMinDistance);
        tvMaxDistance = (TextView)findViewById(com.example.testdmcam1.R.id.tvMaxDistance);
        tvbase = (TextView)findViewById(com.example.testdmcam1.R.id.tv_base);
        tvadvanced =(TextView)findViewById(com.example.testdmcam1.R.id.tv_advanced);
        llbase = (LinearLayout)findViewById(com.example.testdmcam1.R.id.ll_base);
        lladvanced = (LinearLayout)findViewById(com.example.testdmcam1.R.id.ll_advanced);
        spin2.setOnItemSelectedListener(new spinItemSelectedListener2());
        spin_freq.setOnItemSelectedListener(new spinItemSelectedListener3());
        spin_frame.setOnItemSelectedListener(new spinItemSelectedListener4());
        sbarRevise.setOnSeekBarChangeListener(new sbarReviseChangeListener());
        sbarPower.setOnSeekBarChangeListener(new sbarPowerChangeListener());
        sbarExposure
                .setOnSeekBarChangeListener(new sbarExposureChangeListener());
        sbarMinDistance.setOnSeekBarChangeListener(new sbarMinDistanceChangeListener());
        sbarMaxDistance.setOnSeekBarChangeListener(new sbarMaxDistanceChangeListener());
       //按下基础参数文本触发
        tvbase.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                lladvanced.setVisibility(View.GONE);
                llbase.setVisibility(View.VISIBLE);
               // Log.i("press base","successful");
            }
        });
        //按下高级参数文本触发
        tvadvanced.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                llbase.setVisibility(View.GONE);
                lladvanced.setVisibility(View.VISIBLE);
               // Log.i("press advance","successful");

            }
        });
    }
    public  static int getPower(){
        return power;
    }
    public  static  int getExp(){
        return exp;
    }
  /*  public void onClick(View view) {
      switch(view.getId()){
          case R.id.tv_base:
              lladvanced.setVisibility(View.VISIBLE);
              llbase.setVisibility(View.VISIBLE);
           break;
          case R.id.tv_advanced:
              llbase.setVisibility(View.GONE);
              lladvanced.setVisibility(View.GONE);
              break;
      }
    }
    */

    private class spinItemSelectedListener2 implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String str = (String) spin2.getSelectedItem();
            if(str.equals("深度图(彩色编码)")) {
                TestDmcam.getDmcamView().VIEW_TYPE =0;
            }
            else if (str.equals("深度图(灰度编码)")){
                TestDmcam.getDmcamView().VIEW_TYPE=1;
            }
            else if (str.equals("灰度图")) {
                TestDmcam.getDmcamView().VIEW_TYPE=2;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    private class spinItemSelectedListener3 implements  Spinner.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
          Long freq = (Long)spin_freq.getSelectedItem();
            TestDmcam.td.devSetModifyFreq(TestDmcam.getDev(),freq);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    private class spinItemSelectedListener4 implements  Spinner.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
          Long frameRate =(Long)spin_frame.getSelectedItem();
            TestDmcam.td.devSetFrameRate(TestDmcam.getDev(),frameRate);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
    private class sbarReviseChangeListener implements SeekBar.OnSeekBarChangeListener{
        private long lastUpdateMs = -1;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            long curMs =System.currentTimeMillis();
            tvReviseValue.setText(String.format("d%",progress));
            if(TestDmcam.getDev()==null)
                return;
            if (lastUpdateMs == -1 || curMs - lastUpdateMs > 300){
                lastUpdateMs = curMs;
                TestDmcam.td.devSetRevise(TestDmcam.getDev(),progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            reviseValue = seekBar.getProgress();
            TestDmcam.td.devSetRevise(TestDmcam.getDev(),reviseValue);
        }
    }
    private class sbarPowerChangeListener implements SeekBar.OnSeekBarChangeListener {
        private long lastUpdateMs = -1;

        //Intent i = new Intent();
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            long curMs =System.currentTimeMillis();
            tvPowerValue.setText(String.format("%d%%",progress));
            if(TestDmcam.getDev()==null)
                return;
            if (lastUpdateMs == -1 || curMs - lastUpdateMs > 300) {
                lastUpdateMs = curMs;
				/* update to device */
               TestDmcam.td.devSetIllumPower(TestDmcam.getDev(), progress);
               // i.putExtra("DynamicPower",progress);
                //setResult(1,i);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            power = seekBar.getProgress();
            TestDmcam.td.devSetIllumPower(TestDmcam.getDev(),seekBar.getProgress());
          //  i.putExtra("DevPower",seekBar.getProgress());
            //setResult(2,i);
        }
    }


    private class sbarExposureChangeListener implements SeekBar.OnSeekBarChangeListener {
        private long lastUpdateMs = -1;
      //  Intent j = new Intent();
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            int expoUs = progress * 5;
            long curMs =System.currentTimeMillis();
            tvExposureValue.setText(String.format("%dus",expoUs));
            if(TestDmcam.getDev()==null)
                return;
            if (lastUpdateMs == -1 || curMs - lastUpdateMs > 300) {
                lastUpdateMs = curMs;
				/* update to device */
               TestDmcam.td.devSetExposure(TestDmcam.getDev(), expoUs);
             //   j.putExtra("DynamicExposure",expoUs);
               // setResult(3,j);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int expoUs = 5*seekBar.getProgress();
            exp =expoUs;
            TestDmcam.td.devSetExposure(TestDmcam.getDev(),expoUs);
            //j.putExtra("FinalExposure",expoUs);
            //setResult(4,j);
        }
    }


    private class sbarMinDistanceChangeListener implements SeekBar.OnSeekBarChangeListener {
        private long lastUpdateMs = -1;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            int minDis = 50*progress;
            long curMs = System.currentTimeMillis();
            tvMinDistance.setText(String.format("%dmm",minDis));

            if(TestDmcam.getDev()==null)
                return;
            if(curMs==-1 || curMs-lastUpdateMs>300)
            {
                lastUpdateMs=curMs;
                TestDmcam.getDmcamView().RNG_MIN = minDis;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int minDis = 50*seekBar.getProgress();
            TestDmcam.getDmcamView().RNG_MIN=minDis;
        }
    }

    private class sbarMaxDistanceChangeListener implements SeekBar.OnSeekBarChangeListener {
        private long lastUpdateMs = -1;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            int maxDis = 50*progress;
            long curMs = System.currentTimeMillis();
            tvMaxDistance.setText(String.format("%dmm",maxDis));

            if(TestDmcam.getDev()==null)
                return;
            if(curMs==-1 || curMs-lastUpdateMs>300)
            {
                lastUpdateMs=curMs;
                TestDmcam.getDmcamView().RNG_MAX = maxDis;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int maxDis = 50*seekBar.getProgress();
            TestDmcam.getDmcamView().RNG_MAX=maxDis;
        }
    }


/*
    private void setSelect(int i) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction trs = fm.beginTransaction();
        hideFragment(trs);
        switch(i){
            case 0:
                if(fg1==null){
                    fg1=new Fragment1();
                    trs.add(R.id.content,fg1);
                }
                else{
                    trs.show(fg1);
                }
                break;
            case 1:
                if(fg2==null){
                    fg2 = new Fragment2();
                    trs.add(R.id.content,fg2);
                }
                else{
                    trs.show(fg2);
                }
                break;
        }
        trs.commit();
    }
    private  void hideFragment(FragmentTransaction trs){
        if(fg1!=null)
            trs.hide(fg1);
        if(fg2!=null)
            trs.hide(fg2);
    }
    */

}
