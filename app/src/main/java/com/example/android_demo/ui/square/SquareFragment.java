package com.example.android_demo.ui.square;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.android_demo.R;
import com.example.android_demo.databinding.FragmentSquareBinding;
import com.example.android_demo.utils.PostData;
import com.example.android_demo.utils.TimeUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SquareFragment extends Fragment {

    private FragmentSquareBinding binding;
    private View view;
    private GridLayout gl_posts;

    List<PostData.Post> posts = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSquareBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        gl_posts = view.findViewById(R.id.gl_posts);
        putData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "载入成功", Toast.LENGTH_SHORT).show();
        gl_posts.setVisibility(View.VISIBLE);//设置成可见
        for (PostData.Post post : posts) {
            System.out.println(post);
            View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.post_item, null);

            TextView NameTextView = view1.findViewById(R.id.username1);

            NameTextView.setText(post.getUserName());

            TextView contentTextView = view1.findViewById(R.id.tv_content);
            contentTextView.setText(post.getTitle());

            TextView TimeTextView = view1.findViewById(R.id.postTime1);
            TimeTextView.setText(TimeUtils.convert(post.getCreateTime()));

            TextView UpTextView = view1.findViewById(R.id.cb_number_up);
            UpTextView.setText(post.getLikeNum());

            TextView DownTextView = view1.findViewById(R.id.tv_number_down);
            DownTextView.setText(post.getDislikeNum());

            TextView CommentTextView = view1.findViewById(R.id.tv_number_review);
            CommentTextView.setText(post.getCommentNum());

            gl_posts.addView(view1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void updateContent(String username) {
        // 根据用户名更新内容
        // 可以调用一个方法从数据库或网络加载内容
    }

    public void refresh() {

    }

    public void putData() {
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
                        .url("http://10.0.2.2:8081/post/getRecentPosts")
                        .build();
                // 执行发送的指令，获得返回结果
                Response response = client.newCall(request).execute();
                String reData = response.body().string();
                System.out.println("res" + reData);
                Gson gson = new Gson();
                PostData rdata = gson.fromJson(reData, PostData.class);
                if (rdata.getCode().equals("1")) {
                    System.out.println("获取帖子成功");
                    posts = rdata.getData();
                } else {
                    System.out.println("获取帖子失败");
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

}