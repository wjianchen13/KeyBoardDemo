package com.example.keyboarddemo.test13;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.keyboarddemo.BaseActivity;
import com.example.keyboarddemo.R;

/**
 * H5页面软键盘弹出H5上移
 */
public class TestActivity13 extends BaseActivity implements View.OnClickListener{

    private RelativeLayout rlytMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test13);
        rlytMenu = findViewById(R.id.rlyt_menu);

    }

    @Override
    public void onClick(View v) {
    }


}