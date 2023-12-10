package com.example.android_demo.community;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_demo.Constants.constant;
import com.example.android_demo.MyApplication;
import com.example.android_demo.R;
import com.example.android_demo.bean.CommunityBean;
import com.example.android_demo.databinding.FragmentInBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author SummCoder
 * @date 2023/12/7 20:39
 */
public class InFragment extends Fragment {
    private FragmentInBinding binding;

    List<CommunityBean> communityBeanList = new ArrayList<>();

    MyAdapter myAdapter;
    private RecyclerView recyclerview_in;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentInBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        recyclerview_in = root.findViewById(R.id.recyclerview_in);
        myAdapter = new MyAdapter();
        recyclerview_in.setAdapter(myAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview_in.setLayoutManager(layoutManager);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        load();
    }

    public void load() {
        Thread thread = new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient()
                        .newBuilder()
                        .connectTimeout(60000, TimeUnit.MILLISECONDS)
                        .readTimeout(60000, TimeUnit.MILLISECONDS)
                        .build();
                String url = "http://" + constant.IP_ADDRESS + "/uc/getCommunityIdList";
                MyApplication application = MyApplication.getInstance();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("satoken", Objects.requireNonNull(application.infoMap.get("satoken")))
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        // success
                        final String responseData = response.body().string();
                        Log.i("InFragment", responseData);
                        try {
                            JSONObject json = new JSONObject(responseData);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
        });
        thread.start();
    }



    class MyAdapter extends RecyclerView.Adapter<InFragment.MyViewHolder> {

        @NonNull
        @Override
        public InFragment.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(getActivity(), R.layout.community_item, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull InFragment.MyViewHolder holder, int position) {
            CommunityBean communityBean = communityBeanList.get(position);
            holder.iv_cover.setImageResource(communityBean.cover);
            holder.tv_community_name.setText(communityBean.name);
            holder.tv_community_follow.setText(communityBean.follow);
            // 为每个cb_community_follow添加OnCheckedChangeListener
            holder.cb_community_follow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // 在这里定义选中状态改变时的操作
                    String desc = String.format("%s", isChecked ? "已关注" : "关注");
                    if (isChecked) {
                        holder.cb_community_follow.setText(desc);
                    } else {
                        holder.cb_community_follow.setText(desc);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return communityBeanList.size();
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_cover;
        TextView tv_community_name;

        TextView tv_community_follow;

        CheckBox cb_community_follow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_cover = itemView.findViewById(R.id.iv_cover);
            tv_community_name = itemView.findViewById(R.id.tv_community_name);
            tv_community_follow = itemView.findViewById(R.id.tv_community_follow);
            cb_community_follow = itemView.findViewById(R.id.cb_community_follow);
        }
    }

}
