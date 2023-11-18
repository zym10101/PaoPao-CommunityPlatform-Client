package com.example.android_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.android_demo.databinding.ActivityMainBinding;
import com.example.android_demo.user.RegisterActivity;
import com.example.android_demo.user.UserUtils;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isUserLoggedIn()) {
            showLoginDialog();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_square, R.id.nav_community, R.id.nav_home, R.id.nav_setting)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // 在主界面或者基类中添加判断用户登录状态的方法
    public boolean isUserLoggedIn() {
        // 判断用户是否已登录，这里可以根据具体的登录逻辑进行判断
        return UserUtils.isLoggedIn();
    }

    // 弹出登录对话框的方法
    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("登录");

        // 设置对话框的布局
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
        builder.setView(view);

        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();

        // 获取输入框和登录按钮
        EditText usernameEditText = view.findViewById(R.id.et_username);
        EditText passwordEditText = view.findViewById(R.id.et_password);
        Button loginButton = view.findViewById(R.id.btn_login);


        // 阻止用户进行其他操作
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // 设置注册文本的跳转事件
        TextView tvRegister = dialog.findViewById(R.id.tv_register);
        assert tvRegister != null;
        tvRegister.setOnClickListener(v -> {
            // 启动RegisterActivity，进行跳转
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // 设置登录按钮的点击事件
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // 进行登录验证，这里可以根据具体的登录逻辑进行处理
            if (login(username, password)) {
                // 登录成功，关闭对话框
                dialog.dismiss();
            } else {
                // 登录失败，提示用户登录失败
                Toast.makeText(MainActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 登录验证的方法
    private boolean login(String username, String password) {
        // 进行登录验证，这里可以根据具体的登录逻辑进行处理
        return UserUtils.login(username, password);
    }


}