package com.example.keyboarddemo.test7;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keyboarddemo.BaseActivity;
import com.example.keyboarddemo.R;
import com.example.keyboarddemo.test6.MyAdapter;
import com.example.keyboarddemo.test6.MyFrameLayout;
import com.example.keyboarddemo.utils.SoftInputUtils;
import com.example.keyboarddemo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * H5页面软键盘弹出H5上移
 */
public class TestActivity7 extends BaseActivity implements View.OnClickListener, MyFrameLayout.OnLayoutListener {

    private EditText edtvTest;
    private RelativeLayout rlytMenu;
    private RelativeLayout rlytInput;
    private RecyclerView rvList;
    private LinearLayoutManager layoutManager;
    private MyAdapter adapter;
    private List<String> datas1 = new ArrayList<>();
    private TextView tvMicro;
    private TextView tvGame;
    private TextView imgvTest2;
    private TextView imgvTest3;
    private TextView imgvTest4;
    private TextView imgvTest5;
    private TextView imgvTest6;
    private MyFrameLayout flytMicro;
    private TextView tvInfo;
    private FrameLayout flytGame;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test7);
        edtvTest = findViewById(R.id.edtv_test);
        rlytMenu = findViewById(R.id.rlyt_menu);
        rlytInput = findViewById(R.id.rlyt_input);
        tvMicro = findViewById(R.id.tv_micro);
        tvGame = findViewById(R.id.tv_game);
        imgvTest2 = findViewById(R.id.imgv_test2);
        imgvTest3 = findViewById(R.id.imgv_test3);
        imgvTest4 = findViewById(R.id.imgv_test4);
        imgvTest5 = findViewById(R.id.imgv_test5);
        imgvTest6 = findViewById(R.id.imgv_test6);
        flytMicro = findViewById(R.id.flyt_micro);
        tvInfo = findViewById(R.id.tv_info);
        flytGame = findViewById(R.id.flyt_game);
        flytMicro.setListener(this);
        rlytInput.setOnClickListener(this);
        tvGame.setOnClickListener(this);
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
        } else if(v.getId() == R.id.tv_game) {
            showGame();
        } else if(v.getId() == R.id.imgv_test2) {
//            rvList.setZ(5.5f);
        } else if(v.getId() == R.id.imgv_test3) {
//            rvList.setZ(11f);
        } else if(v.getId() == R.id.imgv_test4) {
//            rvList.setZ(15f);
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

    private void showGame() {
        if(webView == null) {
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            webView = new WebView(getApplicationContext());
            webView.setLayoutParams(params);
            flytGame.addView(webView);
            WebSettings settings = webView.getSettings();

//        setWebViewSettings(settings);
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setTextZoom(100);
            settings.setUseWideViewPort(true);
            webView.setBackgroundColor(Color.parseColor("#00000000"));

            webView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                if (webView != null)
//                    webView.loadUrl(request.getUrl().toString());
                    return false;
                }


                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageFinished(view, url);
                }

                @Override
                public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                    super.onReceivedError(view, request, error);
                }

                //设置结束加载函数
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
            webView.setWebChromeClient(new WebChromeClient() {
            });

            webView.setHorizontalScrollBarEnabled(true); // 水平不显示
            webView.setVerticalScrollBarEnabled(true); // 垂直不显示
            webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY); // 滚动条在WebView内侧显示
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 滚动条在WebView外侧显示
        }
        flytGame.setVisibility(View.VISIBLE);
        webView.loadUrl("https://twww.hayuki.com/s/test/clientTestData");
    }

    private void hideGame() {
        flytGame.setVisibility(View.GONE);
    }

}