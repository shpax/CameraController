package com.pidev.cameracontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

public class OrderSetupActivity extends FragmentActivity {

    protected static final int FRAG_COUNT = 3;


    public ViewPager pager;
    private OrdersPagerAdapter pagerAdapter;
    private View submit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neworder);
        pagerAdapter = new OrdersPagerAdapter(getSupportFragmentManager());
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter);
        submit = findViewById(R.id.addOrder);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderFragment current = (OrderFragment) pagerAdapter.getItem(pager.getCurrentItem());
                setResult(RESULT_OK, current.getInput());
                finish();
            }
        });
    }

    class OrdersPagerAdapter extends FragmentPagerAdapter {

        public OrdersPagerAdapter (FragmentManager fm) {super(fm);}

        @Override
        public Fragment getItem(int position) {
            OrderFragment of;
            switch (position) {
                case 2: of = new MoveFragment(); break;
                case 1: of = new RotateFragment(); break;
                case 0: of = new FunctionFragment(); break;

                default: of = new MoveFragment();
            }
            return of;
        }

        @Override
        public int getCount() {
            return FRAG_COUNT;
        }
    }

}

