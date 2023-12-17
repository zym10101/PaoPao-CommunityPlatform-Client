package com.example.android_demo.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.android_demo.R;
import com.example.android_demo.adapter.CommunityExpandableAdapter;
import com.example.android_demo.bean.CommunityExpandBean;
import com.example.android_demo.databinding.FragmentJoinedBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * @author SummCoder
 * @date 2023/12/9 17:53
 */
public class JoinedFragment extends Fragment {

    private FragmentJoinedBinding binding;

    private ExpandableListView lv_joined;

    private List<CommunityExpandBean> dataEntityList = new ArrayList<>();

    private CommunityExpandableAdapter communityExpandableAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentJoinedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        lv_joined = root.findViewById(R.id.lv_joined);
        initData();
        initAdapter();
        return root;
    }

    private void initData() {
        List<CommunityExpandBean.ChildrenData> childrenData1 = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            CommunityExpandBean.ChildrenData children=new CommunityExpandBean.ChildrenData("学生"+(i+1));
            childrenData1.add(children);
        }
        CommunityExpandBean communityExpandBean1 = new CommunityExpandBean("我创建的社区",childrenData1);
        dataEntityList.add(communityExpandBean1);

        List<CommunityExpandBean.ChildrenData> childrenData2 = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            CommunityExpandBean.ChildrenData children = new CommunityExpandBean.ChildrenData("学生"+(i+1));
            childrenData2.add(children);
        }
        CommunityExpandBean communityExpandBean2 = new CommunityExpandBean("我管理的社区",childrenData2);
        dataEntityList.add(communityExpandBean2);

        List<CommunityExpandBean.ChildrenData> childrenData3 = new ArrayList<>();
        for(int i = 0; i < 8; i++){
            CommunityExpandBean.ChildrenData children=new CommunityExpandBean.ChildrenData("学生"+(i+1));
            childrenData3.add(children);
        }
        CommunityExpandBean communityExpandBean3 = new CommunityExpandBean("我加入的社区",childrenData3);
        dataEntityList.add(communityExpandBean3);
    }

    private void initAdapter() {
        communityExpandableAdapter = new CommunityExpandableAdapter(getContext(), dataEntityList);
        lv_joined.setAdapter(communityExpandableAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        communityExpandableAdapter.reFreshData(dataEntityList);
    }
}
