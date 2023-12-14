package com.example.android_demo.community;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_demo.MainActivity;
import com.example.android_demo.R;
import com.example.android_demo.bean.CommunityBean;
import com.example.android_demo.databinding.FragmentHotBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SummCoder
 * @date 2023/12/7 20:38
 */
public class HotFragment extends Fragment {
    private FragmentHotBinding binding;

    List<CommunityBean> communityBeanList = new ArrayList<>();

    MyAdapter myAdapter;

    private static int[] coverArray = {R.drawable.cover0, R.drawable.cover1, R.drawable.cover2};
    private static String[] nameArray = {
            "薅羊毛小分队",
            "今日热点",
            "华为鸿蒙",
            "手机摄影",
            "沙雕乐园",
            "开箱评测"
    };
    private static String[] followArray = {
            "33.7万关注",
            "4.2万关注",
            "5.8万关注",
            "11.3万关注",
            "8.7万关注",
            "7.2万关注"
    };
    private RecyclerView recyclerview_hot;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHotBinding.inflate(inflater, container, false);

        View root = binding.getRoot();
        recyclerview_hot = root.findViewById(R.id.recyclerview_hot);
        for(int i = 0; i < 12; i++){
            CommunityBean communityBean = new CommunityBean(i, coverArray[i%3], nameArray[i%6], followArray[i%6]);
            communityBeanList.add(communityBean);
        }
        myAdapter = new MyAdapter();
        recyclerview_hot.setAdapter(myAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerview_hot.setLayoutManager(layoutManager);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(getActivity(), R.layout.community_item, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
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
                    if(isChecked){
                        holder.cb_community_follow.setText(desc);
                    }else {
                        holder.cb_community_follow.setText(desc);
                    }
                }
            });

            //点击社区头像跳转到社区内部界面
            holder.iv_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //进入社区内部
                    Intent intent=new Intent(getActivity(), CommunityInnerActivity.class);
                    Bundle bundle = new Bundle();

                    //把数据保存到Bundle里
                    bundle.putString("id",String.valueOf(communityBean.id));
                    bundle.putString("cover",String.valueOf(communityBean.cover));
                    bundle.putString("name",String.valueOf(communityBean.name));
                    bundle.putString("follow",String.valueOf(communityBean.follow));
                    //把bundle放入intent里
                    intent.putExtra("Message",bundle);
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return communityBeanList.size();
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
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
