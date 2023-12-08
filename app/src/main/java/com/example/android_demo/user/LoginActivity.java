package com.example.android_demo.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android_demo.R;
import com.example.android_demo.database.DBHelper;
import com.example.android_demo.entity.LoginInfo;
import com.example.android_demo.utils.UserUtils;

/**
 * @author SummCoder
 * @date 2023/11/26 22:15
 */
public class LoginActivity extends AppCompatActivity implements View.OnFocusChangeListener{
    private DBHelper mHelper;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private CheckBox cb_remember;

    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showLoginDialog();
    }

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

        ActivityResultLauncher<Intent> register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result != null){
                Intent intent = result.getData();
                if(intent != null && result.getResultCode() == Activity.RESULT_OK){
                    Bundle bundle = intent.getExtras();
                    username = bundle.getString("username");
                    password = bundle.getString("password");
                    dialog.dismiss();

                    Intent intent1 = new Intent();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("username", username);
                    bundle1.putString("password", password);
                    intent1.putExtras(bundle1);
                    setResult(Activity.RESULT_OK, intent1);

                    finish();
                }
            }
        });


        TextView tvRegister = dialog.findViewById(R.id.tv_register);
        assert tvRegister != null;
        tvRegister.setOnClickListener(v -> {
            // 启动RegisterActivity
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            register.launch(intent);
        });

        // 设置登录按钮的点击事件
        loginButton.setOnClickListener(v -> {
            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
            // 进行登录验证，这里可以根据具体的登录逻辑进行处理
            if (login(username, password)) {
//                记住密码
                LoginInfo loginInfo = new LoginInfo(username, password, cb_remember.isChecked());
                mHelper.saveLoginInfo(loginInfo);
                // 登录成功，关闭对话框
                dialog.dismiss();

                Intent intent1 = new Intent();
                Bundle bundle1 = new Bundle();
                bundle1.putString("username", username);
                bundle1.putString("password", password);
                intent1.putExtras(bundle1);
                setResult(Activity.RESULT_OK, intent1);

                finish();
            } else {
                // 登录失败，提示用户登录失败
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // 登录验证的方法
    private boolean login(String username, String password) {
        // 进行登录验证，这里可以根据具体的登录逻辑进行处理
        return UserUtils.login(username, password);
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
