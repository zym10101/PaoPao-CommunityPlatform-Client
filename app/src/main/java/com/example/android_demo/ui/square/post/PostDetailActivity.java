package com.example.android_demo.ui.square.post;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_demo.R;
import com.example.android_demo.adapter.CommentAdapter;
import com.example.android_demo.utils.CommentData;
import com.example.android_demo.utils.PostData;
import com.example.android_demo.utils.TimeUtils;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);

        // 获取从上一个活动传递的帖子ID
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("post")) {
            PostData.Post post = (PostData.Post) intent.getSerializableExtra("post");
            // 在这里加载和显示帖子详细信息
            assert post != null;
            showPostDetails(post);
        }

        // 创建一些示例评论数据
        ArrayList<CommentData.Comment> comments = new ArrayList<>();
        CommentData.Comment comment = new CommentData.Comment();
        comment.setContent("test");
        comments.add(comment);

        CommentData.Comment comment2 = new CommentData.Comment();
        comment2.setContent("test2");
        comments.add(comment2);
        // ... 添加更多评论

        // 创建适配器并设置给 ListView
        CommentAdapter commentAdapter = new CommentAdapter(this, comments);
        ListView commentListView = findViewById(R.id.commentListView);
        commentListView.setAdapter(commentAdapter);
    }


    @SuppressLint("SetTextI18n")
    private void showPostDetails(PostData.Post post) {
        // 根据postId加载帖子详细信息，例如从数据库或网络请求中获取数据
        // 这里只是一个示例，您需要根据您的应用程序逻辑进行实际的实现

        // 在布局中找到用于显示帖子详细信息的TextView
        TextView postDetailTitleTextView = findViewById(R.id.postDetailTitle);
        TextView postDetailUsernameAndTimeTextView = findViewById(R.id.postDetailUsername);
        TextView postDetailTagListTextView = findViewById(R.id.postDetailTagList);
        TextView postDetailContentTextView = findViewById(R.id.postDetailContent);

        // 将帖子信息显示在TextView中
        postDetailTitleTextView.setText(post.getTitle());
        postDetailUsernameAndTimeTextView.setText("作者：" + post.getUserName() + "    发布时间：" + TimeUtils.convert(post.getCreateTime()));
        postDetailTagListTextView.setText("标签：" + post.getTagList());
        postDetailContentTextView.setText(post.getContent());
    }

}
