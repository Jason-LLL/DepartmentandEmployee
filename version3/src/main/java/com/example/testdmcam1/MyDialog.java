package com.example.testdmcam1;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/8/15 0015.
 */

public class MyDialog extends Dialog {

    private static int power=50,exp=200;
    //private String[] freq = {"12","24","36","48","60"};
    //private String[] frame ={"30","15","60"};
    //private TextView tvbase,tvadvanced;
    //private LinearLayout llbase,lladvanced;
    private SeekBar sbarPower, sbarExposure,sbarMinDistance,sbarMaxDistance;
    private TextView tvPowerValue, tvExposureValue,tvMinDistance,tvMaxDistance;
    private Spinner spin1,spin2;
    public MyDialog(Context context) {
        super(context,R.style.AppTheme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fg1);
        initView();
        initListener();
    }

    private  void initView()
    {
        spin1 = (Spinner)findViewById(R.id.spin1);
        spin2 = (Spinner)findViewById(R.id.spin2);
        //spin_freq=(Spinner)findViewById(R.id.spin_freq);
        //spin_frame=(Spinner)findViewById(R.id.spin_frame_rate);
        //ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,freq);
        //spin_freq.setAdapter(adapter1);
        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,frame);
        //spin_frame.setAdapter(adapter2);
        sbarPower = (SeekBar)findViewById(R.id.sbarPower);
        sbarExposure = (SeekBar)findViewById(R.id.sbarExposure);
        sbarMinDistance = (SeekBar)findViewById(R.id.sbarMinDistance);
        sbarMaxDistance =(SeekBar)findViewById(R.id.sbarMaxDistance);
        //sbarRevise = (SeekBar)findViewById(R.id.sbarRevise);
        tvPowerValue=(TextView)findViewById(R.id.tvPowerValue);
        tvExposureValue= (TextView)findViewById(R.id.tvExposureValue);
        tvMinDistance =(TextView)findViewById(R.id.tvMinDistance);
        tvMaxDistance = (TextView)findViewById(R.id.tvMaxDistance);
        //tvReviseValue = (TextView)findViewById(R.id.tv_revise_Value);
        //tvbase = (TextView)findViewById(R.id.tv_base);
        //tvadvanced =(TextView)findViewById(R.id.tv_advanced);
        //llbase = (LinearLayout)findViewById(R.id.ll_base);
        //lladvanced = (LinearLayout)findViewById(R.id.ll_advanced);
       // llbase.setVisibility(View.VISIBLE);
    }
    private void initListener() {
        spin2.setOnItemSelectedListener(new spinItemSelectedListener2());
        sbarPower.setOnSeekBarChangeListener(new sbarPowerChangeListener());
        sbarExposure
                .setOnSeekBarChangeListener(new sbarExposureChangeListener());
        sbarMinDistance.setOnSeekBarChangeListener(new sbarMinDistanceChangeListener());
        sbarMaxDistance.setOnSeekBarChangeListener(new sbarMaxDistanceChangeListener());
    }

    public  static int getPower(){
        return power;
    }
    public  static  int getExp(){
        return exp;
    }
    /*public void onClick(View view) {
        switch(view.getId()){
            case com.example.testdmcam1.R.id.tv_base:
                lladvanced.setVisibility(View.GONE);
                llbase.setVisibility(View.VISIBLE);
                break;
            case com.example.testdmcam1.R.id.tv_advanced:
                llbase.setVisibility(View.GONE);
                lladvanced.setVisibility(View.VISIBLE);
                break;
        }
    }*/
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
            else if(str.equals("点云图")){
                TestDmcam.getDmcamView().VIEW_TYPE=3;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

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
}
