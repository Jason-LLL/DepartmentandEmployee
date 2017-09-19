package com.example.testdmcam1;

/**
 * Created by Administrator on 2017/7/26 0026.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/7/26 0026.
 */

public class Fragment1 extends Fragment {
    private View tab1view;
    private SeekBar sbarPower, sbarExposure,sbarMinDistance,sbarMaxDistance;
    private TextView tvPowerValue, tvExposureValue,tvMinDistance,tvMaxDistance;
    private Spinner spin1,spin2;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tab1view=inflater.inflate(com.example.testdmcam1.R.layout.fg1, container, false);
        spin1 = (Spinner)tab1view.findViewById(com.example.testdmcam1.R.id.spin1);
        spin2 = (Spinner)tab1view.findViewById(com.example.testdmcam1.R.id.spin2);
        sbarPower = (SeekBar)tab1view.findViewById(com.example.testdmcam1.R.id.sbarPower);
        sbarExposure = (SeekBar)tab1view.findViewById(com.example.testdmcam1.R.id.sbarExposure);
        sbarMinDistance = (SeekBar)tab1view.findViewById(com.example.testdmcam1.R.id.sbarMinDistance);
        sbarMaxDistance =(SeekBar)tab1view.findViewById(com.example.testdmcam1.R.id.sbarMaxDistance);
        tvPowerValue=(TextView)tab1view.findViewById(com.example.testdmcam1.R.id.tvPowerValue);
        tvExposureValue= (TextView)tab1view.findViewById(com.example.testdmcam1.R.id.tvExposureValue);
        tvMinDistance =(TextView)tab1view.findViewById(com.example.testdmcam1.R.id.tvMinDistance);
        tvMaxDistance = (TextView)tab1view.findViewById(com.example.testdmcam1.R.id.tvMaxDistance);

        spin2.setOnItemSelectedListener(new spinItemSelectedListener2());
        sbarPower.setOnSeekBarChangeListener(new sbarPowerChangeListener());
        sbarExposure
                .setOnSeekBarChangeListener(new sbarExposureChangeListener());
        sbarMinDistance.setOnSeekBarChangeListener(new sbarMinDistanceChangeListener());
        sbarMaxDistance.setOnSeekBarChangeListener(new sbarMaxDistanceChangeListener());
        return tab1view;
    }

    private class spinItemSelectedListener2 implements Spinner.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            String str = (String) spin2.getSelectedItem();
            if(str.equals("深度图(彩色编码)")) {
                TestDmcam.td.getDmcamView().VIEW_TYPE =0;
            }
            else if (str.equals("深度图(灰度编码)")){
                TestDmcam.td.getDmcamView().VIEW_TYPE=1;
            }
            else if (str.equals("灰度图")) {
                TestDmcam.td.getDmcamView().VIEW_TYPE=2;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }

    private class sbarPowerChangeListener implements SeekBar.OnSeekBarChangeListener {
        private long lastUpdateMs = -1;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            long curMs =System.currentTimeMillis();
            tvPowerValue.setText(String.format("%d%%",progress));
           if(TestDmcam.td.getDev()==null)
               return;
            if (lastUpdateMs == -1 || curMs - lastUpdateMs > 300) {
                lastUpdateMs = curMs;
				/* update to device */
                TestDmcam.td.devSetIllumPower(TestDmcam.td.getDev(), progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
          TestDmcam.td.devSetIllumPower(TestDmcam.td.getDev(),seekBar.getProgress());
        }
    }

    private class sbarExposureChangeListener implements SeekBar.OnSeekBarChangeListener {
        private long lastUpdateMs = -1;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            int expoUs = progress * 5;
            long curMs =System.currentTimeMillis();
            tvPowerValue.setText(String.format("%d%%",expoUs));
            if(TestDmcam.td.getDev()==null)
                return;
            if (lastUpdateMs == -1 || curMs - lastUpdateMs > 300) {
                lastUpdateMs = curMs;
				/* update to device */
                TestDmcam.td.devSetExposure(TestDmcam.td.getDev(), expoUs);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
             int expoUs = 5*seekBar.getProgress();
            TestDmcam.td.devSetExposure(TestDmcam.td.getDev(),expoUs);
        }
    }

    private class sbarMinDistanceChangeListener implements SeekBar.OnSeekBarChangeListener {
        private long lastUpdateMs = -1;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            int minDis = 50*progress;
            long curMs = System.currentTimeMillis();
            tvMinDistance.setText(String.format("%dmm",minDis));

            if(TestDmcam.td.getDev()==null)
                return;
            if(curMs==-1 || curMs-lastUpdateMs>300)
            {
                lastUpdateMs=curMs;
                TestDmcam.td.getDmcamView().RNG_MIN = minDis;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int minDis = 50*seekBar.getProgress();
            TestDmcam.td.getDmcamView().RNG_MIN=minDis;
        }
    }

    private class sbarMaxDistanceChangeListener implements SeekBar.OnSeekBarChangeListener {
        private long lastUpdateMs = -1;
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            int maxDis = 50*progress;
            long curMs = System.currentTimeMillis();
            tvMinDistance.setText(String.format("%dmm",maxDis));

            if(TestDmcam.td.getDev()==null)
                return;
            if(curMs==-1 || curMs-lastUpdateMs>300)
            {
                lastUpdateMs=curMs;
                TestDmcam.td.getDmcamView().RNG_MAX = maxDis;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int maxDis = 50*seekBar.getProgress();
            TestDmcam.td.getDmcamView().RNG_MAX=maxDis;
        }
    }
}