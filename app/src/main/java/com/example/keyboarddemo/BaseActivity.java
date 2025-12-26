package com.example.keyboarddemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class BaseActivity extends AppCompatActivity {

    /**
     * 用于底部弹窗间距，适配android15使用，值为负值，若需要设置margin可使用绝对值
     */
    MutableLiveData bottomPadding = new MutableLiveData(0);

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        initEdgeToEdge();
        super.onCreate(savedInstanceState);
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
        initEdgeToEdgePadding();
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

}
