package com.example.android_demo.user;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_demo.R;

/**
 * @author SummCoder
 */
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    // 实现注册界面的逻辑和布局
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById(R.id.bt_test).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
//        Intent intent = new Intent(this, MainActivity.class);
//        为了简便，注册成功后直接跳转回主页重新登录，销毁注册活动的栈
        finish();
    }
}

