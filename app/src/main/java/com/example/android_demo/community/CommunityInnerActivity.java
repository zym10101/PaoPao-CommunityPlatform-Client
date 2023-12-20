package com.example.android_demo.community;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_demo.Constants.constant;
import com.example.android_demo.MyApplication;
import com.example.android_demo.R;
import com.example.android_demo.bean.CommunityBean;
import com.example.android_demo.bean.PostBean;
import com.example.android_demo.utils.ResponseData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CommunityInnerActivity extends AppCompatActivity {
    CommunityBean communityBean;
    List<PostBean> PostBeanList = new ArrayList<>();
    TextView tv_name,tv_follow;
    ImageView iv_cover;
    private RecyclerView recyclerview;
    CheckBox cb_community_follow;
    MyAdapter myAdapter;
    private String res;
    long id;
    int cover;
    String name,follow;
    private static int[] coverArray = {R.drawable.cover0, R.drawable.cover1, R.drawable.cover2};
    private static String[] titleArray = {
            "C++秘籍",
            "JVM八股文必备",
            "华为鸿蒙开发",
            "手机摄影",
            "秋招面经分享第一弹",
            "双非上岸大厂经验分享"
    };
    private static String[] contentArray = {
            "C 语言可以零基础入门，它的语法是很多高级编程语言的基础，比如说 Java、C++；并且起到承上启下的作用，向上可以学习高级编程语言，向下可以学习计算机组成原理、操作系统等等偏基础的计算机基础知识",
            "Java 虚拟机栈用来描述 Java 方法执行的内存模型。线程创建时就会分配一个栈空间，线程结束后栈空间被回收 ,栈中元素用于支持虚拟机进行方法调用，每个方法在执行时都会创建一个栈帧存储方法的局部变量表、操作栈、动态链接和返回地址等信息",
            "鸿蒙（即HarmonyOS，开发代号Ark，正式名称为华为终端鸿蒙智能设备操作系统软件）是华为公司自2012年以来开发的一款可支持鸿蒙原生应用和兼容AOSP应用的分布式操作系统。该系统利用“分布式”技术，将手机、电脑、平板、电视、汽车和智能穿戴等多款设备融合成一个“超级终端”，使用户便于操作和共享各种设备的资源",
            "一直以来，很多人对手机摄影都存在一定的偏见，总认为，手机拍的照片质感比不上相机，手机怎么能玩好摄影？今天就来总结一下，我个人这几年用手机摄影的经验和方法，告诉大家手机摄影需要学的东西究竟有哪些？",
            "秋招整体战线比较长，我们可以有充足的时间来准备面试，多看一些大佬或者学长学姐分享的面试经验心得，熟悉面试官提问的问题、在意的点等，早做准备，勤加练习。我也是看了很多大佬的面经，才拿到很不错的offer。",
            "我第一次参加华为的群面，发现同组人都特别的厉害，整的自己有点自卑，最后也没发挥好，但后面参加多了，心态就调整过来了，就当他们在孔雀开屏好了~~嘻嘻~"
    };
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //获取数据
        Bundle bundle = intent.getBundleExtra("Message");
        assert bundle != null;

        id = Long.parseLong(Objects.requireNonNull(bundle.getString("id")));
        cover= Integer.parseInt(Objects.requireNonNull(bundle.getString("cover")));
        name=bundle.getString("name");
        follow=bundle.getString("follow");

        communityBean=new CommunityBean(id,cover,name,follow);
        System.out.println("communityBean id: "+communityBean+"community src: "+communityBean.cover);
        //开始渲染
        setContentView(R.layout.activity_community_inner);
        tv_name=findViewById(R.id.tv_community_name);
        tv_name.setText(communityBean.name);
        tv_follow=findViewById(R.id.tv_community_follow);
        tv_follow.setText(communityBean.follow);

        iv_cover=findViewById(R.id.tv_community_image);
        iv_cover.setImageResource(cover);

        cb_community_follow=findViewById(R.id.cb_community_follow);
        cb_community_follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 在这里定义选中状态改变时的操作
                String desc = String.format("%s", isChecked ? "已关注" : "关注");

                if(isChecked){
                    cb_community_follow.setText(desc);
                }else {
                    cb_community_follow.setText(desc);
                }
            }
        });

        getPosts(String.valueOf(id));

        recyclerview=findViewById(R.id.recyclerview_in_community);
        for(int i = 0; i < 12; i++){
            PostBean postBean = new PostBean(i, id,titleArray[i%6],contentArray[i%6],coverArray[i%3]);
            PostBeanList.add(postBean);
        }
        myAdapter = new MyAdapter();
        recyclerview.setAdapter(myAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            toAddPost();
        });
    }

    public void toAddPost(){
        Intent intent=new Intent(this, AddPostActivity.class);
        Bundle bundle = new Bundle();
        //把数据保存到Bundle里
        bundle.putString("community_id",String.valueOf(communityBean.id));
        //把bundle放入intent里
        intent.putExtra("Message",bundle);
        startActivity(intent);
    }
    public void getPosts(String community_id){
        Thread thread = new Thread(() -> {
            // 获取头像
            try {
                // 创建HTTP客户端
                OkHttpClient client = new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(60000, TimeUnit.MILLISECONDS)
                        .readTimeout(60000, TimeUnit.MILLISECONDS)
                        .build();
                // 创建HTTP请求

                Request request = new Request.Builder()
                        .url("http://" + constant.IP_ADDRESS + "/post/getAllPosts?communityId="+community_id)
                        .build();
                // 执行发送的指令，获得返回结果
                Response response = client.newCall(request).execute();
                String reData=response.body().string();
                System.out.println("redata为："+reData);
                Gson gson = new Gson();
                ResponseData rdata= gson.fromJson(reData, ResponseData.class);
                if (rdata.getCode().equals("200")) res = rdata.getData().toString();
                System.out.println("获取到的返回值为"+res);

            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        });
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.post_item, parent, false);
            return new MyViewHolder(view);
        }
        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            PostBean postBean = PostBeanList.get(position);
            holder.iv_cover.setImageResource(postBean.photo);
            holder.tv_post_title.setText(postBean.title);
            holder.tv_post_content.setText(postBean.content);
        }

        @Override
        public int getItemCount() {
            return PostBeanList.size();
        }
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_cover;
        TextView tv_post_title;
        TextView tv_post_content;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_cover = itemView.findViewById(R.id.avatar);
            tv_post_title = itemView.findViewById(R.id.username1);
            tv_post_content = itemView.findViewById(R.id.tv_content);
        }
    }

}
