package com.example.keyboarddemo.test6;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keyboarddemo.R;
import com.example.keyboarddemo.utils.SoftInputUtils;
import com.example.keyboarddemo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 测试softInputMode
 * SOFT_INPUT_ADJUST_UNSPECIFIED
 * 不指定调整方式，系统自行决定使用哪种调整方式
 *
 * SOFT_INPUT_ADJUST_RESIZE
 * 调整方式为布局需要重新计算大小适配当前可见区域
 *
 * SOFT_INPUT_ADJUST_PAN
 * 调整方式为布局需要整体移动
 *
 * SOFT_INPUT_ADJUST_NOTHING
 * 不做任何操作
 */
public class TestActivity6 extends AppCompatActivity implements View.OnClickListener, MyFrameLayout.OnLayoutListener {

    private EditText edtvTest;
    private RelativeLayout rlytMenu;
    private RelativeLayout rlytInput;
    private RecyclerView rvList;
    private LinearLayoutManager layoutManager;
    private MyAdapter adapter;
    private List<String> datas1 = new ArrayList<>();
    private TextView tvMicro;
    private ImageView imgvTest1;
    private TextView imgvTest2;
    private TextView imgvTest3;
    private TextView imgvTest4;
    private TextView imgvTest5;
    private TextView imgvTest6;
    private MyFrameLayout flytMicro;
    private TextView tvInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test6);
        edtvTest = findViewById(R.id.edtv_test);
        rlytMenu = findViewById(R.id.rlyt_menu);
        rlytInput = findViewById(R.id.rlyt_input);
        tvMicro = findViewById(R.id.tv_micro);
        imgvTest1 = findViewById(R.id.imgv_test1);
        imgvTest2 = findViewById(R.id.imgv_test2);
        imgvTest3 = findViewById(R.id.imgv_test3);
        imgvTest4 = findViewById(R.id.imgv_test4);
        imgvTest5 = findViewById(R.id.imgv_test5);
        imgvTest6 = findViewById(R.id.imgv_test6);
        flytMicro = findViewById(R.id.flyt_micro);
        tvInfo = findViewById(R.id.tv_info);
        flytMicro.setListener(this);
        rlytInput.setOnClickListener(this);
        imgvTest1.setOnClickListener(this);
        imgvTest2.setOnClickListener(this);
        imgvTest3.setOnClickListener(this);
        imgvTest4.setOnClickListener(this);
        imgvTest5.setOnClickListener(this);
        imgvTest6.setOnClickListener(this);
        rvList = findViewById(R.id.rv_test);
        initRv();
    }

    private void initRv() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        setData();
        adapter = new MyAdapter(datas1);
        rvList.setAdapter(adapter);
    }

    private void setData() {
        for(int i = 0; i < 20; i ++) {
            String s1 = "add" + i;
            datas1.add(s1);
        }
    }

    public void onTest1(View v) {
        rlytMenu.setVisibility(View.GONE);
        rlytInput.setVisibility(View.VISIBLE);
        SoftInputUtils.showSoftInput(edtvTest);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.rlyt_input) {
            SoftInputUtils.hideSoftInput(edtvTest);
            rlytMenu.setVisibility(View.VISIBLE);
            rlytInput.setVisibility(View.GONE);
        } else if(v.getId() == R.id.imgv_test1) {

        } else if(v.getId() == R.id.imgv_test2) {
            rvList.setZ(5.5f);
        } else if(v.getId() == R.id.imgv_test3) {
            rvList.setZ(11f);
        } else if(v.getId() == R.id.imgv_test4) {
            rvList.setZ(15f);
        } else if(v.getId() == R.id.imgv_test5) {
            initMicroHeight(688);
        } else if(v.getId() == R.id.imgv_test6) {
            initMicroHeight(500);
        }
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        initMsgHeight(h);
    }

    private void initMicroHeight(int height) {
        FrameLayout.LayoutParams p = (FrameLayout.LayoutParams)tvMicro.getLayoutParams();
        if(p != null && p.height != height) {
            p.height = height;
            tvMicro.setLayoutParams(p);
        }
    }

    private void initMsgHeight(int height) {
        int microY = Utils.getLocationY(flytMicro);
        int msgY = Utils.getLocationY(rlytMenu);
        int msgHeight = msgY - microY - height;
        ConstraintLayout.LayoutParams p = (ConstraintLayout.LayoutParams)rvList.getLayoutParams();
        if(p != null && p.height != msgHeight) {
            p.height = msgHeight;
            rvList.setLayoutParams(p);
        }
    }

}