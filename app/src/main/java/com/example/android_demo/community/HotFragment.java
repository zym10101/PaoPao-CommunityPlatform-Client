package com.example.android_demo.community;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_demo.Constants.constant;
import com.example.android_demo.MyApplication;
import com.example.android_demo.R;
import com.example.android_demo.bean.CommunityBean;
import com.example.android_demo.databinding.FragmentHotBinding;
import com.example.android_demo.utils.UserUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author SummCoder
 * @date 2023/12/7 20:38
 */
public class HotFragment extends Fragment {
    private FragmentHotBinding binding;
    List<CommunityBean> communityBeanList = new ArrayList<>();
    List<Long> liked=new ArrayList<>();
    MyAdapter myAdapter;
    public static MyApplication application;
    private static int[] coverArray = {R.drawable.cover0, R.drawable.cover1, R.drawable.cover2};
    private static String[] nameArray = {
            "薅羊毛小队",
            "薅羊毛小分队",
            "华为鸿蒙",
            "汽车",
            "壁纸",
            "好物安利",
            "今日热点",
            "美食家",
            "沙雕乐园",
            "手机摄影",
            "开箱评测",

    };
    private static String[] followArray = {
            "33.7万关注",
            "4.2万关注",
            "5.8万关注",
            "11.3万关注",
            "8.7万关注",
            "7.2万关注"
    };
    private RecyclerView recyclerview_hot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHotBinding.inflate(inflater, container, false);

        View root = binding.getRoot();

        getLiked();
        recyclerview_hot = root.findViewById(R.id.recyclerview_hot);
        for(int i = 1; i < nameArray.length+1; i++){
            CommunityBean communityBean = new CommunityBean(i, coverArray[i%3], nameArray[i-1], followArray[i%6]);
            communityBeanList.add(communityBean);
        }
        myAdapter = new MyAdapter();
        recyclerview_hot.setAdapter(myAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview_hot.setLayoutManager(layoutManager);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void getLiked(){
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
                        .url(constant.IP_ADDRESS + "/community/getJoinedCommunity?userID=1")
                        .build();
                // 执行发送的指令，获得返回结果
                Response response = client.newCall(request).execute();
                if (response.code() == 200) {
                    assert response.body() != null;
                    final String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        String data=json.getString("data");
                        JSONArray jsonArray=new JSONArray(data);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject j=new JSONObject(jsonArray.getString(i));
                            liked.add(Long.valueOf(j.getString("communityID")));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
    }
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(getActivity(), R.layout.community_item, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            CommunityBean communityBean = communityBeanList.get(position);
            holder.iv_cover.setImageResource(communityBean.cover);
            holder.tv_community_name.setText(communityBean.name);
            holder.tv_community_follow.setText(communityBean.follow);

            if(liked.contains(communityBean.id)){
                holder.cb_community_follow.setText("已关注");
                holder.cb_community_follow.setChecked(true);
            }

            // 为每个cb_community_follow添加OnCheckedChangeListener
            holder.cb_community_follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // 在这里定义选中状态改变时的操作
                    String desc = String.format("%s", isChecked ? "已关注" : "关注");
                    if(isChecked){
                        like(String.valueOf(communityBean.id));
                        holder.cb_community_follow.setText(desc);
                    }else {
                        unlike(String.valueOf(communityBean.id));
                        holder.cb_community_follow.setText(desc);
                    }
                }
            });
            //点击跳转到社区内部界面
            holder.iv_cover.setOnClickListener(view -> getInCommunity(communityBean));
            holder.tv_community_name.setOnClickListener(view -> getInCommunity(communityBean));
            holder.tv_community_follow.setOnClickListener(view -> getInCommunity(communityBean));
        }

        @Override
        public int getItemCount() {
            return communityBeanList.size();
        }

        public void getInCommunity(CommunityBean communityBean){
            Intent intent=new Intent(getActivity(), CommunityInnerActivity.class);
            Bundle bundle = new Bundle();
            //把数据保存到Bundle里
            bundle.putString("id",String.valueOf(communityBean.id));
            bundle.putString("cover",String.valueOf(communityBean.cover));
            bundle.putString("name",String.valueOf(communityBean.name));
            bundle.putString("follow",String.valueOf(communityBean.follow));
            bundle.putString("isliked",liked.contains(communityBean.id)? "1":"0");
            //把bundle放入intent里
            intent.putExtra("Message",bundle);
            startActivity(intent);
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_cover;
        TextView tv_community_name;
        TextView tv_community_follow;
        CheckBox cb_community_follow;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_cover = itemView.findViewById(R.id.iv_cover);
            tv_community_name = itemView.findViewById(R.id.tv_community_name);
            tv_community_follow = itemView.findViewById(R.id.tv_community_follow);
            cb_community_follow = itemView.findViewById(R.id.cb_community_follow);
        }
    }
    public void like(String communityId){
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
                        .url(constant.IP_ADDRESS + "/community/addMember?communityID="+communityId+"&memberID=1&role=2")
                        .build();
                // 执行发送的指令，获得返回结果
                Log.d("likeurl",constant.IP_ADDRESS + "/community/addMember?communityID="+communityId+"&memberID=1&role=2");
                client.newCall(request).execute();
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
    }
    public void unlike(String communityId){
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
                        .url(constant.IP_ADDRESS + "/community/deleteMember?communityID="+communityId+"&memberID=1")
                        .build();
                // 执行发送的指令，获得返回结果
                Log.d("unlikeurl",constant.IP_ADDRESS + "/community/deleteMember?communityID="+communityId+"&memberID=1");
                client.newCall(request).execute();
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
    }

}
