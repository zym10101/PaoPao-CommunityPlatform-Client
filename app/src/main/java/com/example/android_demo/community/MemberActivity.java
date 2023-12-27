package com.example.android_demo.community;

import static com.example.android_demo.utils.UserUtils.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.android_demo.Constants.constant;
import com.example.android_demo.R;
import com.example.android_demo.bean.MemberData;
import com.example.android_demo.bean.OwnerData;
import com.example.android_demo.bean.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author SummCoder
 * @date 2023/12/26 22:10
 */
public class MemberActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_memberBack;
    private long communityID;

    private User owner;

    private List<User> managers;

    private List<User> members;

    private List<Map<String, Object>> memberList;
    private ListView lv_members;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        Intent intent = getIntent();
        communityID = intent.getLongExtra("communityId", 1L);
        iv_memberBack = findViewById(R.id.iv_memberBack);
        iv_memberBack.setOnClickListener(this);
        lv_members = findViewById(R.id.lv_members);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchOwnerData();
        fetchManagerData();
        fetchMemberData();
        updateUI();
    }

    private void updateUI() {
        List<User> users = new ArrayList<>();
        users.add(owner);
        if(managers != null){
            users.addAll(managers);
        }
        if(members != null){
            users.addAll(members);
        }
        memberList = new ArrayList<>();
        for (User user : users) {
            Map<String, Object> map = new HashMap<>();
            map.put("avatar", user.photo);
            map.put("username", user.userName);
            memberList.add(map);
        }

        String[] from = {"avatar", "username"};
        int[] to = {R.id.iv_member_avatar, R.id.tv_member_name};

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, memberList, R.layout.item_member, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                ImageView iv_cover = view.findViewById(R.id.iv_member_avatar);
                String avatarUrl = (String) memberList.get(position).get("avatar");
                // 使用Glide加载图片
                Glide.with(MemberActivity.this)
                        .load(avatarUrl)
                        .into(iv_cover);

                return view;
            }
        };

        lv_members.setAdapter(simpleAdapter);
    }

    private void fetchOwnerData(){
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
                        .url(constant.IP_ADDRESS + "/user/getCommunityOwner?communityID=" + communityID)
                        .addHeader("satoken", Objects.requireNonNull(application.infoMap.get("satoken")))
                        .build();
                // 执行发送的指令，获得返回结果
                Response response = client.newCall(request).execute();
                String reData = response.body().string();
                System.out.println("res" + reData);
                Gson gson = new Gson();
                OwnerData rdata = gson.fromJson(reData, OwnerData.class);
                if (rdata.getCode().equals("200")) {
                    System.out.println("获取群主成功");
                    owner = rdata.getData();
                } else {
                    System.out.println("获取群主失败");
                }
            } catch (Exception e) {
                // 处理异常，例如记录日志
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fetchManagerData(){
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
                        .url(constant.IP_ADDRESS + "/user/getCommunityManagers?communityID=" + communityID)
                        .addHeader("satoken", Objects.requireNonNull(application.infoMap.get("satoken")))
                        .build();
                // 执行发送的指令，获得返回结果
                Response response = client.newCall(request).execute();
                String reData = response.body().string();
                System.out.println("res" + reData);
                Gson gson = new Gson();
                MemberData rdata = gson.fromJson(reData, MemberData.class);
                if (rdata.getCode().equals("200")) {
                    System.out.println("获取管理员成功");
                    managers = rdata.getData();
                } else {
                    System.out.println("获取管理员失败");
                }
            } catch (Exception e) {
                // 处理异常，例如记录日志
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void fetchMemberData(){
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
                        .url(constant.IP_ADDRESS + "/user/getCommunityMembers?communityID=" + communityID)
                        .addHeader("satoken", Objects.requireNonNull(application.infoMap.get("satoken")))
                        .build();
                // 执行发送的指令，获得返回结果
                Response response = client.newCall(request).execute();
                String reData = response.body().string();
                System.out.println("res" + reData);
                Gson gson = new Gson();
                MemberData rdata = gson.fromJson(reData, MemberData.class);
                if (rdata.getCode().equals("200")) {
                    System.out.println("获取普通用户成功");
                    members = rdata.getData();
                } else {
                    System.out.println("获取普通用户失败");
                }
            } catch (Exception e) {
                // 处理异常，例如记录日志
                e.printStackTrace();
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.iv_memberBack){
            finish();
        }
    }
}
