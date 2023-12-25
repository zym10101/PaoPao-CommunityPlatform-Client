package com.example.android_demo.community;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android_demo.Constants.constant;
import com.example.android_demo.MyApplication;
import com.example.android_demo.R;
import com.example.android_demo.ui.chat.ChatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPostActivity extends AppCompatActivity {
    EditText titleET,contentET;
    TextView to_ai;
    String title,content,communityId;
    Button cancel,commit;
    public static MyApplication application;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("Message");
        assert bundle != null;
        communityId=bundle.getString("community_id");

        setContentView(R.layout.add_post_layout);
        titleET=findViewById(R.id.titleEditText);
        contentET=findViewById(R.id.contentEditText);
        cancel=findViewById(R.id.post_cancel_Button);
        commit=findViewById(R.id.post_commit_Button);
        to_ai=findViewById(R.id.to_AI);

        application = MyApplication.getInstance();

        cancel.setOnClickListener(v-> finish());

        to_ai.setOnClickListener(v->{
            Intent intent1 = new Intent(this, ChatActivity.class);
            startActivity(intent1);
        });

        commit.setOnClickListener(v->{
            title = titleET.getText().toString();
            content = contentET.getText().toString();
            sendPost(title,content);
        });
    }
    private void sendPost(String title, String content) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", 1);
            jsonObject.put("communityId", communityId);
            jsonObject.put("title", title);
            jsonObject.put("content", content);
            jsonObject.put("isPublic", "true");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());
        Log.i("Addpost", jsonObject.toString());

        String url = constant.IP_ADDRESS + "/post/addPost";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 异步发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // success
                assert response.body() != null;
                final String responseData = response.body().string();
                Log.i("Addpost", responseData);
                try {
                    JSONObject json = new JSONObject(responseData);
                    String code=json.getString("code");
                    if(code.equals("1")){
                        Looper.prepare();
                        Toast.makeText(AddPostActivity.this, "发帖成功", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Looper.prepare();
                        Toast.makeText(AddPostActivity.this, "发帖失败", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
