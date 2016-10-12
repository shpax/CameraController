package com.pidev.cameracontroller;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;

public class MoveFragment extends OrderFragment {

    public String fragmentName = "Move";
    public CheckBox move_straight;

    public SeekBar leftD;
    public SeekBar rightD;

    public static EditText leftE;
    public static EditText rightE;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layout;
        layout = R.layout.order_move;
        View rootView = inflater.inflate(layout, container, false);
        move_straight = (CheckBox) rootView.findViewById(R.id.move_straight);
        leftD = (SeekBar) rootView.findViewById(R.id.leftDriveSpeed);
        rightD = (SeekBar) rootView.findViewById(R.id.rightDriveSpeed);
        leftE = (EditText) rootView.findViewById(R.id.leftDriveSpeedNum);
        rightE = (EditText) rootView.findViewById(R.id.rightDriveSpeedNum);
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
        rightD.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rightE.setText(String.valueOf((progress+1)*5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return rootView;
    }

    @Override
    public Intent getInput() {
        Intent result = new Intent();
        result.putExtra("header", "Move ");
        result.putExtra("infoMain", "");
        result.putExtra("infoSub", "");
        result.putExtra("dimentions", "%");
        result.putExtra("value", leftE.getText().toString() + "/" + rightE.getText().toString());
        byte rdrive = (byte) (Byte.valueOf(rightE.getText().toString()) | (1<<7));
        byte ldrive = (byte) (Byte.valueOf(leftE.getText().toString()) | (1<<7));
        byte order[] = {4,rdrive,ldrive};
        result.putExtra("bytes",order);
        return result;
    }
}
