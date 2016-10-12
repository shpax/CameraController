package com.pidev.cameracontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;

public class RotateFragment extends OrderFragment {

    public SeekBar leftD;
    public static EditText leftE;
    public static ImageView rotateCW;
    public static ImageView rotateCCW;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layout;
        layout = R.layout.order_rotate;
        View rootView = inflater.inflate(layout, container, false);
        leftD = (SeekBar) rootView.findViewById(R.id.leftDriveSpeed);
        leftE = (EditText) rootView.findViewById(R.id.leftDriveSpeedNum);
        leftD.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                leftE.setText(String.valueOf((progress+1)*5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        rotateCW = (ImageView) rootView.findViewById(R.id.rotateCW);
        rotateCW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateCW.setVisibility(View.GONE);
                rotateCCW.setVisibility(View.VISIBLE);
            }
        });
        rotateCCW = (ImageView) rootView.findViewById(R.id.rotateCCW);
        rotateCCW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotateCCW.setVisibility(View.GONE);
                rotateCW.setVisibility(View.VISIBLE);
            }
        });
        return rootView;
    }

    @Override
    public Intent getInput() {
        Intent result = new Intent();
        result.putExtra("header", "Rotate ");
        byte rotation = (byte) (rotateCW.getVisibility() == View.VISIBLE ? 2:3);
        result.putExtra("infoMain", rotation==2?"CW":"CCW");
        result.putExtra("infoSub", "");
        result.putExtra("dimentions", " steps, ");
        result.putExtra("value", leftE.getText().toString());
        byte ldrive = (byte) (Byte.valueOf(leftE.getText().toString()));
        byte order[] = {rotation,ldrive,0};
        result.putExtra("bytes",order);
        return result;
    }
}
