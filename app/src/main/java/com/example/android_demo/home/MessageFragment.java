package com.example.android_demo.home;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.android_demo.MyApplication;
import com.example.android_demo.R;
import com.example.android_demo.databinding.FragmentMessageBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author SummCoder
 * @date 2023/12/9 17:52
 */
public class MessageFragment extends Fragment {
    private FragmentMessageBinding binding;
    private ListView lv_message;

    List<HashMap<String,Object>> list = new ArrayList<>();
    private SimpleAdapter simpleAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        lv_message = root.findViewById(R.id.lv_message);
        simpleAdapter = new SimpleAdapter(getActivity(),list, R.layout.community_item_layout,
                new String[]{"name"},
                new int[]{R.id.tv_name_message});
        lv_message.setAdapter(simpleAdapter);
        return root;
    }

    public void load() {
        Thread thread = new Thread(() -> {
            try {
                // 创建HTTP客户端
                OkHttpClient client = new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(60000, TimeUnit.MILLISECONDS)
                        .readTimeout(60000, TimeUnit.MILLISECONDS)
                        .build();
                // 创建HTTP请求
                MyApplication application = MyApplication.getInstance();
                Request request = new Request.Builder()
                        .url("http://" + "10.0.2.2:8200" + "/application/getApplicationByAdminId?adminID=" + Objects.requireNonNull(application.infoMap.get("loginId")))
                        .addHeader("satoken", Objects.requireNonNull(application.infoMap.get("satoken")))
                        .build();
                // 执行发送的指令，获得返回结果
                try {
                    // 执行发送的指令，获得返回结果
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseData);
                        System.out.println(jsonResponse);
                        JSONObject data = jsonResponse.getJSONObject("data");
                        List<HashMap<String, Object>> list = new ArrayList<>();
                        Iterator<String> keys = data.keys();
                        while (keys.hasNext()) {
                            String key = keys.next();
                            JSONArray value = data.getJSONArray(key);
                            for (int i = 0; i < value.length(); i++) {
                                HashMap<String, Object> map = new HashMap<>();
                                map.put(key, value.get(i));
                                list.add(map);
                            }
                        }
                        System.out.println(list);
                    } else {
                        // 处理请求失败的情况
                        System.out.println("error");
                    }
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
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

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
