package com.example.keyboarddemo.test14;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keyboarddemo.R;
import com.example.keyboarddemo.test6.MyAdapter;
import com.example.keyboarddemo.test6.MyFrameLayout;
import com.example.keyboarddemo.utils.SoftInputUtils;
import com.example.keyboarddemo.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * H5页面软键盘弹出H5上移
 * 如果不设置initEdgeToEdge和initEdgeToEdgePadding，android:windowSoftInputMode="adjustPan" ，点击H5的输入框
 * 界面是可以整体上移的不压缩的，但是这个时候并没有边到边的效果
 * 如果设置了initEdgeToEdge和initEdgeToEdgePadding，android:windowSoftInputMode="adjustPan"，点击H5输入框
 * 界面上移并且会压缩
 * BaseWindowInsetConstraintLayout 其实就是设置了上下边距，和initEdgeToEdgePadding()要做的事情差不多，都是
 * 根据状态栏和导航栏的尺寸动态设置实际布局的边距
 * 下面的例子实际上BaseWindowInsetConstraintLayout已经生效了，当设置initEdgeToEdge()时，已经是设置了边到边
 * BaseWindowInsetHelper里面的defaultApplySystemWindowInsets21()方法设置了状态栏和导航栏的缩进。所以看到的
 * 效果就是状态栏和导航栏都不会遮挡布局上的内容，然后点击H5上的输入框，界面也可以上移不压缩。
 * 如果不使用BaseWindowInsetConstraintLayout，那么状态栏和导航栏都会遮挡布局内容，然后点击H5的输入框，整体
 * 界面上移，并且不会压缩
 *
 *
 */
public class TestActivity14 extends AppCompatActivity implements View.OnClickListener, MyFrameLayout.OnLayoutListener {

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

    /**
     * 用于底部弹窗间距，适配android15使用，值为负值，若需要设置margin可使用绝对值
     */
    MutableLiveData bottomPadding = new MutableLiveData(0);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initEdgeToEdge();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test14);
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

    private void initEdgeToEdge() {
        //设置边到边特性，API兼容到SDK24
        EdgeToEdge.enable(this);
        //是否设置系统状态栏为亮色，默认false，此时状态栏时间等字体颜色为白色
        WindowInsetsControllerCompat windowInsetsControllerCompat =  WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        if(windowInsetsControllerCompat != null) {
            windowInsetsControllerCompat.setAppearanceLightStatusBars(isStatusBarLightMode());
            //隐藏底部导航栏或者小白条
            if (isNavBarHide()) {
                windowInsetsControllerCompat.hide(WindowInsetsCompat.Type.navigationBars());
            }
            if (isNavBarTransparent()) {
                windowInsetsControllerCompat.setAppearanceLightNavigationBars(false);
            }
        }

    }

    /**
     * 是否设置状态栏黑色字体图标
     */
    protected boolean isStatusBarLightMode() {
        return true;
    }

    protected boolean isNavBarHide(){
        return false;
    }

    protected boolean isNavBarTransparent() {
        return false;
    }



    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
//        initEdgeToEdgePadding();
    }

    private void initEdgeToEdgePadding() {
        //将APP内容调整为底部导航栏或者底部白色横条之上，避免底部内容被导航条遮挡
        //app原先的BaseTitleView或者statusbar控件已处理顶部状态栏间距，此处不再做处理
        ViewCompat.setOnApplyWindowInsetsListener( (((ViewGroup)findViewById(android.R.id.content))).getChildAt(0), new OnApplyWindowInsetsListener() {
            @NonNull
            @Override
            public WindowInsetsCompat onApplyWindowInsets(@NonNull View v, @NonNull WindowInsetsCompat windowInsets) {
                Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
                Insets imeInsets = windowInsets.getInsets(WindowInsetsCompat.Type.ime());
                Insets sInsets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars());
                Insets nInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars());
                int bottom = Math.max(insets.bottom, imeInsets.bottom);
                if (needBottomPadding()) {
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams)v.getLayoutParams();
                    // 在EdgeToEdge模式下，根布局可能会向上延伸（top为负数）来处理状态栏
                    // 我们需要考虑这个偏移，确保bottomMargin设置后，根布局的底部位置正确
                    // 由于根布局向上延伸了（通常是状态栏高度），我们需要在设置bottomMargin时
                    // 考虑这个偏移，或者让子视图自己处理
                    p.bottomMargin = bottom;
                    v.setLayoutParams(p);
                }
                if (-bottom != (int)bottomPadding.getValue()) {
                    bottomPadding.postValue(-bottom);
                }
                System.out.println("==================> H5 软键盘测试 systemBars bottom: " + insets.bottom + "   imeInsets bottom: "
                        + imeInsets.bottom + "   status bar height: " + sInsets.bottom + "   navigation bar height: " + nInsets.bottom);
//                imeHeight.postValue(imeInsets.bottom)
                // Return CONSUMED if you don't want want the window insets to keep passing
                // down to descendant views.

                return WindowInsetsCompat.CONSUMED;
            }
        });
    }

    protected boolean needBottomPadding() {
        return true;
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