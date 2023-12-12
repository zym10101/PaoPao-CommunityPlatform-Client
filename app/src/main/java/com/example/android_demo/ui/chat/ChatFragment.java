package com.example.android_demo.ui.chat;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android_demo.R;
import com.example.android_demo.databinding.FragmentChatBinding;
import com.example.android_demo.databinding.FragmentCommunityBinding;
import com.example.android_demo.ui.community.CommunityViewModel;

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
import okhttp3.ResponseBody;

public class ChatFragment extends Fragment {

    private FragmentChatBinding binding;

    private static final String ADDR = "10.0.2.2:5000";

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("ChatFragment", "onCreateView");

        ChatViewModel chatViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);

        binding = FragmentChatBinding.inflate(inflater, container, false);
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = binding.inputEditText.getText().toString();

                // get
                getArticle(userInput);
            }
        });
        View root = binding.getRoot();
        return root;
    }


    private void getArticle(String json) {
        // okHttp

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS) // 设置连接超时时间
                .writeTimeout(10, TimeUnit.SECONDS) // 设置写入超时时间
                .readTimeout(30, TimeUnit.SECONDS) // 设置读取超时时间
                .build();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("keywords", json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, jsonObject.toString());
        Log.i("ChatFragment", jsonObject.toString());

        String url = "http://" + ADDR + "/api/article";
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 异步发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // failed
                Log.e("ChatFragment", "failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // success
                final String responseData = response.body().string();
                Log.i("ChatFragment", responseData);

                try {
                    JSONObject json = new JSONObject(responseData);
                    String article = json.optString("article");
                    binding.outputTextView.setText(article);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}