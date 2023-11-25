package com.example.android_demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.android_demo.database.DBHelper;
import com.example.android_demo.databinding.ActivityMainBinding;
import com.example.android_demo.entity.LoginInfo;
import com.example.android_demo.user.RegisterActivity;
import com.example.android_demo.utils.UserUtils;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private MainViewModel mainViewModel;

    FragmentManager fragmentManager;
    FragmentTransaction transaction;
    private DBHelper mHelper;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox cb_remember;

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

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_square, R.id.nav_topic, R.id.nav_home, R.id.nav_setting)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //获取共享的vm
        mainViewModel=new ViewModelProvider(this).get(MainViewModel.class);
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
        usernameEditText = view.findViewById(R.id.et_username);
        passwordEditText = view.findViewById(R.id.et_password);
        Button loginButton = view.findViewById(R.id.btn_login);
        cb_remember = view.findViewById(R.id.cb_remember);

        // 阻止用户进行其他操作
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        passwordEditText.setOnFocusChangeListener(this);


        TextView tvRegister = dialog.findViewById(R.id.tv_register);
        assert tvRegister != null;
        tvRegister.setOnClickListener(v -> {
            // 启动RegisterActivity
           Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // 设置登录按钮的点击事件
        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // 进行登录验证，这里可以根据具体的登录逻辑进行处理
            if (login(username, password)) {
                mainViewModel.getUsername().setValue(username);
                mainViewModel.getPassword().setValue(password);
//                记住密码
                LoginInfo loginInfo = new LoginInfo(username, password, cb_remember.isChecked());
                mHelper.saveLoginInfo(loginInfo);
                // 登录成功，关闭对话框
                dialog.dismiss();
            } else {
                // 登录失败，提示用户登录失败
                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 登录验证的方法
    private boolean login(String username, String password) {
        // 进行登录验证，这里可以根据具体的登录逻辑进行处理
        return UserUtils.login(username, password);
    }

    private void replaceFragment(Fragment fragment) {
        fragmentManager =getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.drawer_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 打开数据库读写连接
        mHelper = DBHelper.getInstance(this);
        mHelper.openReadLink();
        mHelper.openWriteLink();
        reload();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // 关闭数据库连接
        mHelper.closeLink();
    }

    // 进入页面时加载数据库中存储的用户名和密码
    private void reload(){
        LoginInfo info = mHelper.queryTop();
        if(info != null && info.remember){
            usernameEditText.setText(info.username);
            passwordEditText.setText(info.password);
            cb_remember.setChecked(true);
        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if(view.getId() == R.id.et_password && hasFocus){
            LoginInfo info = mHelper.queryByUsername(usernameEditText.getText().toString());
            if(info != null){
                passwordEditText.setText(info.password);
                cb_remember.setChecked(info.remember);
            }else {
                passwordEditText.setText("");
                cb_remember.setChecked(false);
            }
        }
    }
}