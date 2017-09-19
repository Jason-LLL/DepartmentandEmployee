package com.example.testdmcam1;

/**
 * Created by Administrator on 2017/7/26 0026.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2017/7/26 0026.
 */

public class Fragment2 extends Fragment {
    private View tab1view;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tab1view=inflater.inflate(com.example.testdmcam1.R.layout.fg2, container, false);
        return tab1view;
    }
}