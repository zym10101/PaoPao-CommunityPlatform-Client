package com.example.android_demo.ui.square.post;

import static com.example.android_demo.utils.UserUtils.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.android_demo.Constants.constant;
import com.example.android_demo.R;
import com.example.android_demo.adapter.CommentAdapter;
import com.example.android_demo.utils.CommentData;
import com.example.android_demo.utils.PostData;
import com.example.android_demo.utils.TimeUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostDetailActivity extends AppCompatActivity {
    private List<CommentData.Comment> commentList = new ArrayList<>();

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
            getComments(post);

            CommentAdapter commentAdapter = new CommentAdapter(this, commentList);
            ListView commentListView = findViewById(R.id.commentListView);
            commentListView.setAdapter(commentAdapter);
            setListViewHeightBasedOnChildren(commentListView);
        }
    }

    private void getComments(PostData.Post post) {
        Thread thread = new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(60000, TimeUnit.MILLISECONDS)
                        .readTimeout(60000, TimeUnit.MILLISECONDS)
                        .build();

                RequestBody requestBody = new FormBody.Builder()
                        .add("postId", String.valueOf(post.getPostId()))
                        .build();

                Request request = new Request.Builder()
                        .url("http://" + constant.IP_ADDRESS + "/user/getComments")
                        .post(requestBody)
                        .addHeader("satoken", Objects.requireNonNull(application.infoMap.get("satoken")))
                        .build();

                Response response = client.newCall(request).execute();

                // 读取响应体的内容并保存到变量中
                String responseData = response.body().string();

                // 在这里进行两次调用之间的处理，比如将字符串转换为对象
                Gson gson = new Gson();
                CommentData rdata = gson.fromJson(responseData, CommentData.class);
                commentList = rdata.getData();

            } catch (Exception e) {
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


    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        totalHeight += (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
        listView.requestLayout();
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
