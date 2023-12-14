package com.example.android_demo.ui.chat;

import androidx.lifecycle.Observer;
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

import com.example.android_demo.MainViewModel;
import com.example.android_demo.R;
import com.example.android_demo.databinding.FragmentChatBinding;
import com.example.android_demo.databinding.FragmentCommunityBinding;
import com.example.android_demo.ui.community.CommunityViewModel;
import com.example.android_demo.ui.setting.SettingViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    private MainViewModel mainViewModel;

    private static final String ADDR = "121.40.84.9:8000";

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    private String outputArticle = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.i("ChatFragment", "onCreateView");
        ChatViewModel chatViewModel =
                new ViewModelProvider(this).get(ChatViewModel.class);

        //尝试获取共享数据viewmodel
        mainViewModel=new ViewModelProvider(getActivity()).get(MainViewModel.class);


        binding = FragmentChatBinding.inflate(inflater, container, false);
        final Observer<String> outputObserver = newOutput -> binding.outputTextView.setText(newOutput);
        //mainViewModel.getUsername().observe(getActivity(), outputObserver);
        View root = binding.getRoot();
        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = binding.inputEditText.getText().toString();
                String style = "article";
                getArticle(userInput, style);
            }
        });
        binding.poemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = binding.inputEditText.getText().toString();
                String style = "poem";
                getArticle(userInput, style);
            }
        });
        binding.redbookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = binding.inputEditText.getText().toString();
                String style = "redbook";
                getArticle(userInput, style);
            }
        });
        return root;
    }


    private void getArticle(String json, String style) {
        // okHttp

        OkHttpClient client = new OkHttpClient();
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("keywords", json);
            jsonObject.put("style", style);
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
                outputArticle = "failed";
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // success
                final String responseData = response.body().string();
                Log.i("ChatFragment", responseData);
                String output = "";

                try {
                    JSONObject json = new JSONObject(responseData);
                    String data = json.getString("data");
                    JSONObject dataJson = new JSONObject(data);
                    String article = dataJson.getString("article");
                    outputArticle = article;
                    Log.i("ChatFragment", article);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.outputTextView.setText(outputArticle);
                        if (style == "article" && outputArticle != "failed") {
                            binding.outputTextView.setTextSize(20);
                        }
                        else if (style == "poem" && outputArticle != "failed") {
                            binding.outputTextView.setTextSize(50);
                        }
                        else if (style == "redbook" && outputArticle != "failed") {
                            binding.outputTextView.setTextSize(20);
                        }
                    }
                });
            }
        });
    }
}