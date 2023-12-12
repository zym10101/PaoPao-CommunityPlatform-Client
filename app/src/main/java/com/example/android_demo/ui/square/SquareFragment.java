package com.example.android_demo.ui.square;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.android_demo.R;
import com.example.android_demo.databinding.FragmentSquareBinding;
import com.example.android_demo.utils.PostData;
import com.example.android_demo.utils.TimeUtils;

import java.util.List;

public class SquareFragment extends Fragment {
    private FragmentSquareBinding binding;
    private View view;
    private GridLayout gl_posts;
    private SquareViewModel squareViewModel;

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

            gl_posts.addView(view1);
        }
    }
}