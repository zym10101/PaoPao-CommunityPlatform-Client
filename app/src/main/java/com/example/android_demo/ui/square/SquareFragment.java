package com.example.android_demo.ui.square;

import static com.example.android_demo.utils.UserUtils.application;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.android_demo.Constants.constant;
import com.example.android_demo.R;
import com.example.android_demo.databinding.FragmentSquareBinding;
import com.example.android_demo.ui.square.post.PostDetailActivity;
import com.example.android_demo.utils.PostData;
import com.example.android_demo.utils.TimeUtils;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SquareFragment extends Fragment {
    private FragmentSquareBinding binding;
    private View view;
    private GridLayout gl_posts;
    private SquareViewModel squareViewModel;

    private static final String LIKE_URL = "http://" + constant.IP_ADDRESS + "/user/like";
    private static final String LIKE_BACK_URL = "http://" + constant.IP_ADDRESS + "/user/like_back";
    private static final String DISLIKE_URL = "http://" + constant.IP_ADDRESS + "/user/dislike";
    private static final String DISLIKE_BACK_URL = "http://" + constant.IP_ADDRESS + "/user/dislike_back";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSquareBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        gl_posts = view.findViewById(R.id.gl_posts);

        // 初始化 ViewModel
        squareViewModel = new ViewModelProvider(this).get(SquareViewModel.class);

        // 观察 LiveData 变化
        squareViewModel.getPostsLiveData().observe(getViewLifecycleOwner(), posts -> {
            if (posts != null && !posts.isEmpty()) {
                updateUI(posts);
            } else {
                // 处理数据为空的情况
                Toast.makeText(getActivity(), "获取帖子失败", Toast.LENGTH_SHORT).show();
            }
        });

        // 发起数据请求
        squareViewModel.fetchData();

        return view;
    }

    private void updateUI(List<PostData.Post> posts) {
        for (PostData.Post post : posts) {
            View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.post_item, null);

            TextView NameTextView = view1.findViewById(R.id.username1);
            NameTextView.setText(post.getUserName());

            ImageView avatorImageView = view1.findViewById(R.id.avatar);
            Glide.with(this)
                    .load(post.getPhoto())
                    .into(avatorImageView);
            System.out.println(post.getPhoto());

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

            // 点赞按钮
            CheckBox cbUp = view1.findViewById(R.id.cb_up);

            AtomicInteger currentLikes = new AtomicInteger(Integer.parseInt(post.getLikeNum()));

            cbUp.setOnClickListener(v -> {
                if (cbUp.isChecked()) {
                    // 如果之前未点赞，进行点赞
                    currentLikes.incrementAndGet();
                } else {
                    // 如果之前已经点赞，取消点赞
                    currentLikes.decrementAndGet();
                }

                // 更新点赞数
                UpTextView.setText(String.valueOf(currentLikes.get()));

                // 在这里可以执行其他你想要的操作，比如发送网络请求来保存点赞状态等
                saveData(post, cbUp.isChecked() ? LIKE_URL : LIKE_BACK_URL);
            });

            // 点踩按钮
            CheckBox ivDown = view1.findViewById(R.id.iv_down);

            AtomicInteger currentDisLikes = new AtomicInteger(Integer.parseInt(post.getDislikeNum()));

            ivDown.setOnClickListener(v -> {

                if (ivDown.isChecked()) {
                    // 如果之前未点踩，进行点踩
                    currentDisLikes.incrementAndGet();
                } else {
                    // 如果之前已经点踩，取消点踩
                    currentDisLikes.decrementAndGet();
                }

                // 更新点踩数
                DownTextView.setText(String.valueOf(currentDisLikes.get()));
                // 在这里可以执行其他你想要的操作，比如发送网络请求来保存点踩状态等
                saveData(post, ivDown.isChecked() ? DISLIKE_URL : DISLIKE_BACK_URL);
            });

            // 为 post_item 添加点击事件
            view1.setOnClickListener(v -> {
                // 在这里处理点击事件，例如导航到帖子详情页
                navigateToPostDetail(post);
            });

            gl_posts.addView(view1);
        }
    }

    private void navigateToPostDetail(PostData.Post post) {
        // 创建一个Intent或使用其他导航方法，将post传递给新的活动或片段
        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
        intent.putExtra("post", post);
        startActivity(intent);
    }

    private void saveData(PostData.Post post, String url) {
        Thread thread = new Thread(() -> {
            try {
                // 创建 HTTP 客户端
                OkHttpClient client = new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(60000, TimeUnit.MILLISECONDS)
                        .readTimeout(60000, TimeUnit.MILLISECONDS)
                        .build();

                // 创建 POST 请求的表单数据
                RequestBody requestBody = new FormBody.Builder()
                        .add("postId", String.valueOf(post.getPostId()))
                        .build();

                // 创建 HTTP 请求
                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .addHeader("satoken", Objects.requireNonNull(application.infoMap.get("satoken")))
                        .build();

                // 执行发送的指令，获得返回结果
                Response response = client.newCall(request).execute();

                // 输出响应的内容
                System.out.println(response.body().string());
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

}