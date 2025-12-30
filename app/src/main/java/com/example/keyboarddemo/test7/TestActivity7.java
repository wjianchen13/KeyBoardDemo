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

import android.content.res.Resources;
import android.graphics.Rect;
import android.view.ViewGroup;

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
public class TestActivity7 extends AppCompatActivity implements View.OnClickListener, MyFrameLayout.OnLayoutListener {

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
    private android.view.ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private boolean isSoftInputShowing = false;

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
            
            // 添加JavaScript接口，用于H5页面通知Android端软键盘状态
            webView.addJavascriptInterface(new Object() {
                @android.webkit.JavascriptInterface
                public void onInputFocus() {
                    // H5输入框获得焦点，软键盘即将弹出
                    flytGame.post(new Runnable() {
                        @Override
                        public void run() {
                            // 延迟一点时间，等待软键盘弹出
                            flytGame.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adjustForSoftInput();
                                }
                            }, 300);
                        }
                    });
                }
                
                @android.webkit.JavascriptInterface
                public void onInputBlur() {
                    // H5输入框失去焦点，软键盘即将收起
                    flytGame.post(new Runnable() {
                        @Override
                        public void run() {
                            flytGame.setTranslationY(0);
                            isSoftInputShowing = false;
                        }
                    });
                }
            }, "AndroidKeyboard");

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
                    // 页面加载完成后，注入JavaScript代码来监听输入框焦点变化
                    injectKeyboardListener();
                    // 同时初始化布局监听作为备用
                    initSoftInputListener();
                }
            });
            webView.setWebChromeClient(new WebChromeClient() {
            });
            
            // 监听WebView的焦点变化，当H5输入框获得焦点时确保监听器已初始化
            webView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus && globalLayoutListener == null) {
                        // WebView获得焦点时，确保监听器已初始化
                        initSoftInputListener();
                    }
                }
            });

            webView.setHorizontalScrollBarEnabled(true); // 水平不显示
            webView.setVerticalScrollBarEnabled(true); // 垂直不显示
            webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY); // 滚动条在WebView内侧显示
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); // 滚动条在WebView外侧显示
        }
        flytGame.setVisibility(View.VISIBLE);
        webView.loadUrl("https://twww.hayuki.com/s/test/clientTestData");
        // 延迟初始化软键盘监听，确保布局已经稳定
        flytGame.post(new Runnable() {
            @Override
            public void run() {
                initSoftInputListener();
            }
        });
    }

    /**
     * 注入JavaScript代码来监听H5页面输入框的焦点变化
     */
    private void injectKeyboardListener() {
        if (webView == null) {
            return;
        }
        
        String jsCode = 
            "(function() {" +
            "  var inputs = document.querySelectorAll('input, textarea');" +
            "  for (var i = 0; i < inputs.length; i++) {" +
            "    inputs[i].addEventListener('focus', function() {" +
            "      if (window.AndroidKeyboard) {" +
            "        window.AndroidKeyboard.onInputFocus();" +
            "      }" +
            "    });" +
            "    inputs[i].addEventListener('blur', function() {" +
            "      if (window.AndroidKeyboard) {" +
            "        window.AndroidKeyboard.onInputBlur();" +
            "      }" +
            "    });" +
            "  }" +
            "  // 监听动态添加的输入框" +
            "  var observer = new MutationObserver(function(mutations) {" +
            "    var newInputs = document.querySelectorAll('input, textarea');" +
            "    for (var i = 0; i < newInputs.length; i++) {" +
            "      if (!newInputs[i].hasAttribute('data-keyboard-listener')) {" +
            "        newInputs[i].setAttribute('data-keyboard-listener', 'true');" +
            "        newInputs[i].addEventListener('focus', function() {" +
            "          if (window.AndroidKeyboard) {" +
            "            window.AndroidKeyboard.onInputFocus();" +
            "          }" +
            "        });" +
            "        newInputs[i].addEventListener('blur', function() {" +
            "          if (window.AndroidKeyboard) {" +
            "            window.AndroidKeyboard.onInputBlur();" +
            "          }" +
            "        });" +
            "      }" +
            "    }" +
            "  });" +
            "  observer.observe(document.body, { childList: true, subtree: true });" +
            "})();";
        
        webView.evaluateJavascript(jsCode, null);
    }
    
    /**
     * 调整WebView位置以适应软键盘
     */
    private void adjustForSoftInput() {
        if (flytGame == null || flytGame.getVisibility() != View.VISIBLE) {
            return;
        }
        
        View rootView = getWindow().getDecorView();
        if (rootView == null) {
            return;
        }
        
        int rootHeight = rootView.getHeight();
        Rect rect = new Rect();
        rootView.getWindowVisibleDisplayFrame(rect);
        
        int softInputHeight = rootHeight - rect.bottom;
        
        // 获取导航栏高度
        Resources resources = getResources();
        int navigationBarHeight = 0;
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        
        // 如果有导航栏，需要减去导航栏高度
        if (softInputHeight == navigationBarHeight) {
            softInputHeight = 0;
        } else if (softInputHeight > navigationBarHeight) {
            softInputHeight = softInputHeight - navigationBarHeight;
        }
        
        if (softInputHeight > 100) {
            flytGame.setTranslationY(-softInputHeight);
            isSoftInputShowing = true;
            Utils.log("通过JS接口调整，软键盘高度: " + softInputHeight);
        }
    }
    
    /**
     * 初始化软键盘监听，用于H5页面输入框弹出软键盘时，让H5页面上移（备用方案）
     */
    private void initSoftInputListener() {
        if (flytGame == null || globalLayoutListener != null) {
            return;
        }
        
        // 获取DecorView作为根视图
        final View rootView = getWindow().getDecorView();
        if (rootView == null) {
            return;
        }
        
        // 获取导航栏高度
        Resources resources = getResources();
        int navigationBarHeight = 0;
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }
        
        final int finalNavigationBarHeight = navigationBarHeight;
        
        // 使用ViewTreeObserver的OnGlobalLayoutListener，这比OnLayoutChangeListener更可靠
        globalLayoutListener = new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 只有flytGame可见时才处理
                if (flytGame == null || flytGame.getVisibility() != View.VISIBLE) {
                    return;
                }
                
                // 获取根视图高度
                int rootHeight = rootView.getHeight();
                
                // 获取可见区域
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);
                
                // 计算软键盘高度
                int softInputHeight = rootHeight - rect.bottom;
                
                // 判断是否有导航栏
                boolean hasNavigationBar = (softInputHeight == finalNavigationBarHeight);
                
                // 如果有导航栏，需要减去导航栏高度才是真正的软键盘高度
                if (hasNavigationBar) {
                    softInputHeight = 0;
                } else if (softInputHeight > finalNavigationBarHeight) {
                    softInputHeight = softInputHeight - finalNavigationBarHeight;
                }
                
                // 判断软键盘是否显示（高度大于100像素认为是显示状态）
                boolean isSoftInputShow = softInputHeight > 100;
                
                // 只有当状态改变时才更新
                if (isSoftInputShow != isSoftInputShowing) {
                    isSoftInputShowing = isSoftInputShow;
                    if (isSoftInputShow) {
                        // 软键盘弹出，将WebView容器向上移动
                        flytGame.setTranslationY(-softInputHeight);
                        Utils.log("软键盘弹出，高度: " + softInputHeight + ", 移动距离: " + (-softInputHeight));
                    } else {
                        // 软键盘收起，恢复原位置
                        flytGame.setTranslationY(0);
                        Utils.log("软键盘收起");
                    }
                } else if (isSoftInputShow && softInputHeight > 0) {
                    // 软键盘高度变化时，更新位置
                    flytGame.setTranslationY(-softInputHeight);
                }
            }
        };
        
        // 在DecorView的ViewTreeObserver上添加监听器
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }

    private void hideGame() {
        // 隐藏游戏时，重置位置，移除软键盘监听
        if (flytGame != null) {
            flytGame.setTranslationY(0);
        }
        
        // 移除全局布局监听器
        if (globalLayoutListener != null) {
            View rootView = getWindow().getDecorView();
            if (rootView != null && rootView.getViewTreeObserver().isAlive()) {
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
            }
            globalLayoutListener = null;
        }
        
        isSoftInputShowing = false;
        flytGame.setVisibility(View.GONE);
    }

}