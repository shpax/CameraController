package com.pidev.cameracontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

public class FunctionFragment extends OrderFragment {

    public static final int FUNCTION_SHOT = 1;
    public static final int FUNCTION_WAIT = 2;
    public static final int FUNCTION_STOP = 3;

    public static SeekBar waitS;
    public static EditText waitE;
    public static int selectFunction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layout;
        layout = R.layout.order_function;
        View rootView = inflater.inflate(layout, container, false);
        waitS = (SeekBar) rootView.findViewById(R.id.functionWaitSeek);
        waitE = (EditText) rootView.findViewById(R.id.functionWaitEdit);
        waitS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                waitE.setText(String.valueOf((progress + 1) * 0.5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        View.OnClickListener select = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.functionShot: selectFunction = FUNCTION_SHOT; break;
                    case R.id.functionDelay: selectFunction = FUNCTION_WAIT; break;
                    case R.id.functionStop: selectFunction = FUNCTION_STOP; break;
                }
            }
        };
        rootView.findViewById(R.id.functionShot).setOnClickListener(select);
        rootView.findViewById(R.id.functionDelay).setOnClickListener(select);
        rootView.findViewById(R.id.functionStop).setOnClickListener(select);
        selectFunction = FUNCTION_SHOT;
        return rootView;
    }

    @Override
    public Intent getInput() {
        Intent result = new Intent();
        byte order[] = {1,1,1};
        switch (selectFunction) {
            case FUNCTION_SHOT:
                result.putExtra("header", "Take a shot");
                result.putExtra("infoMain", "");
                result.putExtra("infoSub", "");
                result.putExtra("dimentions", "");
                result.putExtra("value", "");
                order[0] = 1;
                result.putExtra("bytes",order);
                break;
            case FUNCTION_STOP:
                result.putExtra("header", "Stop");
                result.putExtra("infoMain", "");
                result.putExtra("infoSub", "");
                result.putExtra("dimentions", "");
                result.putExtra("value", "");
                order[0] = 5;
                result.putExtra("bytes",order);
                break;
            case FUNCTION_WAIT:
                result.putExtra("header", "Wait for ");
                result.putExtra("value", waitE.getText().toString());
                result.putExtra("infoSub", "");
                result.putExtra("dimentions", "s");
                result.putExtra("infoMain", "");
                order[0] = (byte) (waitS.getProgress()+1);
                result.putExtra("bytes",order);
                break;

        }
        return result;
    }
}
