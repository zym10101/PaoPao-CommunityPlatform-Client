package com.example.android_demo.ui.square.post;

import static com.example.android_demo.ui.square.SquareFragment.DISLIKE_BACK_URL;
import static com.example.android_demo.ui.square.SquareFragment.DISLIKE_URL;
import static com.example.android_demo.ui.square.SquareFragment.LIKE_BACK_URL;
import static com.example.android_demo.ui.square.SquareFragment.LIKE_URL;
import static com.example.android_demo.utils.UserUtils.application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.android_demo.Constants.constant;
import com.example.android_demo.R;
import com.example.android_demo.adapter.CommentAdapter;
import com.example.android_demo.utils.CommentData;
import com.example.android_demo.utils.PostData;
import com.example.android_demo.utils.TimeUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostDetailActivity extends AppCompatActivity {
    private List<CommentData.Comment> commentList = new ArrayList<>();

    private CommentAdapter commentAdapter;

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
            commentAdapter = new CommentAdapter(this, commentList);
            if (commentList != null) {
                ListView commentListView = findViewById(R.id.commentListView);
                commentListView.setAdapter(commentAdapter);
                setListViewHeightBasedOnChildren(commentListView);
            }
        }

        CheckBox up = findViewById(R.id.detail_up);
        CheckBox down = findViewById(R.id.detail_down);
        up.setOnClickListener(v -> {
            down.setEnabled(!up.isChecked());
            PostData.Post post = (PostData.Post) getIntent().getSerializableExtra("post");
            // 在这里可以执行其他你想要的操作，比如发送网络请求来保存点赞状态等
            saveData(post, up.isChecked() ? LIKE_URL : LIKE_BACK_URL);
        });

        down.setOnClickListener(v -> {
            up.setEnabled(!down.isChecked());
            PostData.Post post = (PostData.Post) getIntent().getSerializableExtra("post");
            // 在这里可以执行其他你想要的操作，比如发送网络请求来保存点赞状态等
            saveData(post, down.isChecked() ? DISLIKE_URL : DISLIKE_BACK_URL);
        });
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

    private void addComments(String comment, PostData.Post post) {
        Thread thread = new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(60000, TimeUnit.MILLISECONDS)
                        .readTimeout(60000, TimeUnit.MILLISECONDS)
                        .build();

                // 设置请求体为 JSON
                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                // 构建 JSON 数据
                String json = new Gson().toJson(new CommentData.CommentVO("", post.getPostId(), comment));

                RequestBody requestBody = RequestBody.create(JSON, json);

                Request request = new Request.Builder()
                        .url("http://" + constant.IP_ADDRESS + "/user/comment")
                        .post(requestBody)
                        .addHeader("satoken", Objects.requireNonNull(application.infoMap.get("satoken")))
                        .build();

                Response response = client.newCall(request).execute();
                String responseData = response.body().string();

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

    public void submitComment(View view) {
        // 获取评论框的文本
        EditText commentEditText = findViewById(R.id.commentEditText);
        String commentText = commentEditText.getText().toString().trim();

        // 检查评论是否为空
        if (!commentText.isEmpty()) {
            // 执行提交评论的逻辑，可以发送评论到服务器
            System.out.println(commentText);
            addComments(commentText, (PostData.Post) getIntent().getSerializableExtra("post"));
            // 清空评论框
            commentEditText.setText("");
            // 刷新评论列表
            runOnUiThread(() -> {
                commentAdapter.notifyDataSetChanged();
                restartActivity();
            });

        } else {
            // 提示用户评论不能为空
            Toast.makeText(this, "评论不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
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
