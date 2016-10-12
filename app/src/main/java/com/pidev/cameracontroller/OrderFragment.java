package com.pidev.cameracontroller;

import android.content.Intent;
import android.support.v4.app.Fragment;

public abstract class OrderFragment extends Fragment{

    public String fragmentName;

    public abstract Intent getInput();

}
