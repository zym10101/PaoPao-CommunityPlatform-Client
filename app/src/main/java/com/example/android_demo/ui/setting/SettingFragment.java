package com.example.android_demo.ui.setting;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.android_demo.Constants.constant;
import com.example.android_demo.MainActivity;
import com.example.android_demo.MainViewModel;
import com.example.android_demo.R;
import com.example.android_demo.bean.RegisterRequest;
import com.example.android_demo.databinding.FragmentSettingBinding;
import com.example.android_demo.user.RegisterActivity;
import com.example.android_demo.utils.ConvertType;
import com.example.android_demo.utils.ResponseData;
import com.example.android_demo.utils.UserUtils;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SettingFragment extends Fragment {
    private FragmentSettingBinding binding;
    private MainViewModel mainViewModel;
     TextView textView;
    ImageView postAva;
    Button xiugai,logout;
    private String userName, password,phoneNumber, verify;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(inflater, container, false);

        //获取textView的时候也通过binding获取
        textView = binding.textSetting;
        xiugai = binding.xiugai;
        logout=binding.logout;
        postAva=binding.ivAvatar;

        //获取viewModel,用来存储数据
        SettingViewModel settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        //尝试获取共享数据viewmodel
        mainViewModel=new ViewModelProvider(getActivity()).get(MainViewModel.class);

        //适用于textview的观察函数
        final Observer<String> nameObserver = newName -> textView.setText(newName);

        //给username加上观察器，当username值发生改变的时候，调用观察函数
        mainViewModel.getUsername().observe(getActivity(), nameObserver);

        // 获取头像
        final Observer<String> avatarObserver = newAvatar -> {
            // 使用 Glide 加载图像并设置给 postAva
            Glide.with(this)
                    .load(newAvatar)
                    .into(postAva);
        };

        //给avatar加上观察器，当avatar值发生改变的时候，调用观察函数
        mainViewModel.getAvatar().observe(getActivity(), avatarObserver);

        //TODO 上传头像功能
        postAva.setOnClickListener(view -> {

        });

        //退出登录
        logout.setOnClickListener(view -> {
            showLogoutDialog();
        });

        //修改密码功能
        xiugai.setOnClickListener(view -> {
            showResetDialog();
        });

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //展示修改密码对话框
    private void showResetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("修改密码");

        // 设置对话框的布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_change, null);
        builder.setView(view);

        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();

        // 获取输入框和登录按钮
        EditText usernameEditText = view.findViewById(R.id.et_username);
        EditText passwordEditText = view.findViewById(R.id.et_password);
        EditText phoneEditText = view.findViewById(R.id.et_phone);
        EditText verifyEditText = view.findViewById(R.id.et_verify);

        Button sendButton = view.findViewById(R.id.btn_send);
        Button resetButton = view.findViewById(R.id.btn_reset);
        Button quitButton = view.findViewById(R.id.btn_quit);


        // 阻止用户进行其他操作
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        // 设置发送验证码按钮的点击事件
        sendButton.setOnClickListener(v -> {

            phoneNumber = phoneEditText.getText().toString().trim();
            Thread thread = new Thread(() -> {
                try {
                    // 创建HTTP客户端
                    OkHttpClient client = new OkHttpClient()
                            .newBuilder()
                            .connectTimeout(60000, TimeUnit.MILLISECONDS)
                            .readTimeout(60000, TimeUnit.MILLISECONDS)
                            .build();
                    // 创建HTTP请求

                    Request request = new Request.Builder()
                            .url("http://" + constant.IP_ADDRESS + "/sms/send?phone=" + phoneNumber)
                            .build();
                    // 执行发送的指令，获得返回结果
                    Response response = client.newCall(request).execute();
                    String reData=response.body().string();
                    Gson gson = new Gson();
                    ResponseData rdata= gson.fromJson(reData, ResponseData.class);
                    if (rdata.getCode().equals("200")) {
                        Looper.prepare();
                        Toast.makeText(getActivity(), "验证码发送成功", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(getActivity(), "验证码发送失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                    Looper.prepare();
                    Toast.makeText(getActivity(), "网络或进程问题", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 设置reset按钮的点击事件
        resetButton.setOnClickListener(v -> {
            userName = usernameEditText.getText().toString().trim();
            password = passwordEditText.getText().toString().trim();
            phoneNumber = phoneEditText.getText().toString().trim();
            verify = verifyEditText.getText().toString().trim();

            RegisterRequest registerRequest = new RegisterRequest(userName, password, phoneNumber, verify);

            Thread thread = new Thread(() -> {
                try {
                    String json = ConvertType.beanToJson(registerRequest);
                    // 创建HTTP客户端
                    OkHttpClient client = new OkHttpClient()
                            .newBuilder()
                            .connectTimeout(60000, TimeUnit.MILLISECONDS)
                            .readTimeout(60000, TimeUnit.MILLISECONDS)
                            .build();
                    // 创建HTTP请求
                    Request request = new Request.Builder()
                            .url("http://" + constant.IP_ADDRESS + "/user/update")
                            .post(RequestBody.create(MediaType.parse("application/json"), json))
                            .build();
                    // 执行发送的指令
                    Response response = client.newCall(request).execute();
                    if(response.code()==200){
                        Looper.prepare();
                        Toast.makeText(getActivity(), "修改成功", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        Looper.loop();
                    }else{
                        Looper.prepare();
                        Toast.makeText(getActivity(), "修改失败", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(getActivity(), "网络或进程问题", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        quitButton.setOnClickListener(view1 -> {
            dialog.dismiss();
        });
    }

    //展示退出登录对话框
    private void showLogoutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示");
        builder.setMessage("是否确认退出登录？");

        builder.setPositiveButton("确认退出", (dialogInterface, i) -> {
            UserUtils.logout();
            dialogInterface.dismiss();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        });
        builder.setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.show();
    }
}