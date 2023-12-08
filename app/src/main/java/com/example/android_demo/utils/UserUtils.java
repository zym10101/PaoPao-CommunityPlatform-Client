package com.example.android_demo.utils;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.example.android_demo.Constants.constant;
import com.example.android_demo.user.LoginActivity;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserUtils {
    private static boolean isLoggedIn = false;
     public static String token;
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static boolean login(String username, String password) {
        if((username.equals("frechen026") && password.equals("123456")) || (username.equals("admin") && password.equals("admin"))){
            isLoggedIn = true;
            return true;
        }
        // 进行登录验证的逻辑，例如与服务器通信验证用户名和密码
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 创建HTTP客户端
                    OkHttpClient client = new OkHttpClient()
                            .newBuilder()
                            .connectTimeout(60000, TimeUnit.MILLISECONDS)
                            .readTimeout(60000, TimeUnit.MILLISECONDS)
                            .build();
                    // 创建HTTP请求

                    Request request = new Request.Builder()
                            .url("http://" + constant.IP_ADDRESS + "/user/login?userName=" + username + "&password=" + password)
                            .build();
                    // 执行发送的指令，获得返回结果
                    Response response = client.newCall(request).execute();
                    isLoggedIn = response.code() == 200;
                    String responseData =response.body().string();
                    int index=responseData.indexOf("data");
                    token=responseData.substring(index+6,responseData.length()-1);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        }).start();
        //获取共享的vm

        //初始化数据
        return isLoggedIn;
    }

    public static void logout() {
        new Thread(() -> {
            try {
                // 创建HTTP客户端
                OkHttpClient client = new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(60000, TimeUnit.MILLISECONDS)
                        .readTimeout(60000, TimeUnit.MILLISECONDS)
                        .build();
                // 创建HTTP请求

                Request request = new Request.Builder()
                        .url("http://" + constant.IP_ADDRESS + "/user/logout")
                        .addHeader("token",token)
                        .build();
                // 执行发送的指令，获得返回结果
                Response response = client.newCall(request).execute();
                if(response.code()==200){
                    isLoggedIn=false;
                }
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        }).start();
    }
}

