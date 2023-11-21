package com.example.android_demo.utils;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.example.android_demo.Constants.constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserUtils {
    private static boolean isLoggedIn = false;
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }

    public static boolean login(String username, String password) {
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
                            .url("http://" + constant.IP_ADDRESS + "/user/logout")
                            .build();
                    // 执行发送的指令，获得返回结果
                    Response response = client.newCall(request).execute();
                    isLoggedIn = response.code() == 200;
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
        }).start();
        isLoggedIn = false;
    }
}

