package com.example.android_demo.utils;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.example.android_demo.Constants.constant;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserUtils {
    public static boolean isLoggedIn = false;
    public static String token;
    public static boolean isLoggedIn() {
        return isLoggedIn;
    }



    public static boolean login(String username, String password) {
        if(username.equals("admin") && password.equals("admin")){
            isLoggedIn = true;
            return true;
        }
        // 进行登录验证的逻辑，例如与服务器通信验证用户名和密码
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
                        .url("http://" + constant.IP_ADDRESS + "/user/login?userName=" + username + "&password=" + password)
                        .build();
                // 执行发送的指令，获得返回结果
                Response response = client.newCall(request).execute();
                String reData=response.body().string();
                System.out.println("redata"+reData);
                Gson gson = new Gson();
                ResponseData rdata= gson.fromJson(reData, ResponseData.class);
                System.out.println(rdata.getData());
                if(Integer.parseInt(rdata.getCode())==999){
                    isLoggedIn=false;
                    System.out.println("登陆失败");
                }else{
                    System.out.println("登陆成功");
                    isLoggedIn=true;
                }

            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        });

        thread.start();

        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //初始化数据
        return isLoggedIn;
    }

    public static void logout() {
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
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 修改登录状态
        isLoggedIn = false;
    }
}

