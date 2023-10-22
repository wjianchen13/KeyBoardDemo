package com.example.keyboarddemo.test4;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.keyboarddemo.R;
import com.example.keyboarddemo.utils.SoftInputUtils;

/**
 * EditText顶上去，ImageView保持不动
 */
public class TestActivity4 extends AppCompatActivity {

    private EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);
        attachView();
    }

    private void attachView() {

        //editText2为需要调整的View
        editText2 = findViewById(R.id.et2);
        SoftInputUtil softInputUtil = new SoftInputUtil();
        softInputUtil.attachSoftInput(editText2, new SoftInputUtil.ISoftInputChanged() {
            @Override
            public void onChanged(boolean isSoftInputShow, int softInputHeight, int viewOffset) {
                if (isSoftInputShow) {
                    editText2.setTranslationY(editText2.getTranslationY() - viewOffset);
                } else {
                    editText2.setTranslationY(0);
                }
            }
        });
    }


}