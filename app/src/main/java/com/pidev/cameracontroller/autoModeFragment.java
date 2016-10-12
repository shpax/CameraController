package com.pidev.cameracontroller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class autoModeFragment extends Fragment {

    public static final int ArduinoData = 1;
    private static autoModeFragment currentInstance;
    public static Handler messageHandler;
    ArrayList<OrderInstance> orders = new ArrayList<>();

    public ImageView startButton;
    public ImageView stopButton;

    public int currentOrder = 0;
    public boolean sendFlag = false;

    public autoModeFragment() {
        currentInstance = autoModeFragment.this;
    }

    public static autoModeFragment getCurrentInstance() {
        return  currentInstance!=null ? currentInstance : new autoModeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order_sequence, container, false);
        View.OnClickListener addnew = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), OrderSetupActivity.class), MainActivity.ORDER_SETUP);
            }
        };
        rootView.findViewById(R.id.addNewOrders).setOnClickListener(addnew);
        rootView.findViewById(R.id.setup).setOnClickListener(addnew);
        rootView.findViewById(R.id.clearAllOrders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orders.clear();
                ((TableLayout) getActivity().findViewById(R.id.orders)).removeAllViews();
                getActivity().findViewById(R.id.setup).setVisibility(View.VISIBLE);
                getActivity().findViewById(R.id.Start).setVisibility(View.GONE);
                getActivity().findViewById(R.id.Stop).setVisibility(View.GONE);
            }
        });

        messageHandler = new Handler() {

            public void handleMessage(android.os.Message msg) {
                Toast.makeText(getActivity().getApplicationContext(),"got message",Toast.LENGTH_SHORT).show();
                switch (msg.what) {
                    case ArduinoData:
                        byte[] readBuf = (byte[]) msg.obj;
                        try {
                            String strIncom = new String(readBuf, "UTF-8");
                            Toast.makeText(getActivity().getApplicationContext(),strIncom,Toast.LENGTH_SHORT).show();
                            if (strIncom.equals("1488")) executeOrder();
                        } catch (Exception e) {
                            Toast.makeText(getActivity().getApplicationContext(),"Error with coding",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            };
        };

        startButton = (ImageView) rootView.findViewById(R.id.Start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.VISIBLE);
                sendFlag = true;
                executeOrder();
            }
        });
        stopButton = (ImageView) rootView.findViewById(R.id.Stop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.GONE);
                sendFlag = false;
            }
        });

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.ORDER_SETUP && resultCode == getActivity().RESULT_OK) {
            orders.add(new OrderInstance(data.getExtras()));
        }
        if (orders.size()>0) {
            getActivity().findViewById(R.id.setup).setVisibility(View.GONE);
            getActivity().findViewById(R.id.Start).setVisibility(View.VISIBLE);

        }
    }

    public void executeOrder() {
        if (orders.size()<=currentOrder) currentOrder = 0;
        if (orders.size()!=0 && sendFlag) {
            orders.get(currentOrder).sendOrder();
            currentOrder++;
        }
    }

    class OrderInstance {

        public View view;
        private byte[] bytes;
        private boolean send = true;
        private int delay = 120;

        public OrderInstance (Bundle data) {
            view = getActivity().getLayoutInflater().inflate(R.layout.order_row,null);
            ((TextView) view.findViewById(R.id.fType)).setText(data.getString("header"));
            ((TextView) view.findViewById(R.id.fVal)).setText(data.getString("value"));
            ((TextView) view.findViewById(R.id.fDim)).setText(data.getString("dimentions"));
            ((TextView) view.findViewById(R.id.iMain)).setText(data.getString("infoMain"));
            ((TextView) view.findViewById(R.id.iSub)).setText(data.getString("infoSub"));
            ((TableLayout) getActivity().findViewById(R.id.orders)).addView(view);
            bytes = data.getByteArray("bytes");
            if (data.getString("header").equals("Wait for ")) {
                this.send = false;
                delay = bytes[0]*500;
            }
            if (data.getString("header").equals("Rotate ")) {
                delay = 1000 + bytes[1]*5;
            }
        }

        public void sendOrder() {
            if (this.send) {
                if (MainActivity.connection != null) {
                    MainActivity.connection.sendData(this.bytes);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "No connection", Toast.LENGTH_SHORT);
                }
            }
                messageHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        executeOrder();
                    }
                },this.delay);

        }
    }
}
