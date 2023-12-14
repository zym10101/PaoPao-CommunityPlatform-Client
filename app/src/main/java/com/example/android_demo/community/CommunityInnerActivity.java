package com.example.android_demo.community;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_demo.R;
import com.example.android_demo.bean.CommunityBean;
import com.example.android_demo.bean.PostBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class CommunityInnerActivity extends AppCompatActivity {
    CommunityBean communityBean;
    List<PostBean> PostBeanList = new ArrayList<>();
    TextView tv_name,tv_follow;
    ImageView iv_cover;
    private RecyclerView recyclerview;
    HotFragment.MyAdapter myAdapter;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        //获取数据
        Bundle bundle = intent.getBundleExtra("Message");
        assert bundle != null;
        long id = Long.parseLong(Objects.requireNonNull(bundle.getString("id")));
        int cover= Integer.parseInt(Objects.requireNonNull(bundle.getString("cover")));
        String name=bundle.getString("name");
        String follow=bundle.getString("follow");
        communityBean=new CommunityBean(id,cover,name,follow);
        System.out.println("communityBean id: "+communityBean+"community src: "+communityBean.cover);
        //开始渲染
        setContentView(R.layout.activity_community_inner);
        tv_name=findViewById(R.id.tv_community_name);
        tv_name.setText(communityBean.name);
        tv_follow=findViewById(R.id.tv_community_follow);
        tv_follow.setText(communityBean.follow);

        iv_cover=findViewById(R.id.tv_community_image);
        iv_cover.setImageResource(cover);


        recyclerview =findViewById(R.id.recyclerview_in_community);

    }

}
