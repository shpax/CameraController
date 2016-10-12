package com.pidev.cameracontroller;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.UUID;

public class ConnectFragment extends Fragment {

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public static BluetoothSocket btSocket = null;
    public EditText MacAddress;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_connect, container, false);
        MacAddress = (EditText) rootView.findViewById(R.id.btMAC);
        rootView.findViewById(R.id.connectButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    BluetoothDevice device = MainActivity.btAdapter.getRemoteDevice(MacAddress.getText().toString());
                    btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                    //MainActivity.btAdapter.cancelDiscovery();
                    try {
                        btSocket.connect();
                        MainActivity.connection = new ConnectedThread(btSocket);
                        Toast.makeText(getActivity().getApplicationContext(),"Connected",Toast.LENGTH_SHORT).show();
                        MainActivity.mViewPager.setCurrentItem(1);
                    } catch (Exception e) {
                        btSocket.close();
                        Toast.makeText(getActivity().getApplicationContext(),"Error while connecting",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("mytag",e.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(),"Unable to connect, check your MAC", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rootView.findViewById(R.id.manualMode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ManualActivity.class);
                startActivity(i);
            }
        });
        return rootView;
    }
}
