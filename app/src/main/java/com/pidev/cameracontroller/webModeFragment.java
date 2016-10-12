package com.pidev.cameracontroller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.protocol.HttpService;

public class webModeFragment extends Fragment {

    public static HttpService server;

    public webModeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_web_mode, container, false);
//        if (server == null) {
//            server = new HttpService();
//        }
        return rootView;
    }


}
