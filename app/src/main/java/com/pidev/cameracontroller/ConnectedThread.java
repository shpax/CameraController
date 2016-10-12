package com.pidev.cameracontroller;

import android.bluetooth.BluetoothSocket;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread{
    private final BluetoothSocket copyBtSocket;
    private final OutputStream OutStrem;
    private final InputStream InStrem;

    public ConnectedThread(BluetoothSocket socket){
        copyBtSocket = socket;
        OutputStream tmpOut = null;
        InputStream tmpIn = null;
        try{
            tmpOut = socket.getOutputStream();
            tmpIn = socket.getInputStream();
        } catch (IOException e){}

        OutStrem = tmpOut;
        InStrem = tmpIn;
    }

    public void run()
    {
        byte[] buffer = new byte[1024];
        int bytes;

        while(true){
            try{
                bytes = InStrem.read(buffer);
                autoModeFragment.messageHandler.obtainMessage(autoModeFragment.ArduinoData, bytes, -1, buffer).sendToTarget();
            }catch(IOException e){break;}

        }

    }

    public void sendData(byte[] message) {
        try {
            OutStrem.write(message);
            Log.i("ORDER", String.valueOf(message[0] + " " + message[1] + " " + message[2]));
        } catch (IOException e) {}
    }

    public void cancel(){
        try {
            copyBtSocket.close();
        }catch(IOException e){}
    }

    public Object status_OutStrem(){
        if (OutStrem == null){return null;
        }else{return OutStrem;}
    }
}