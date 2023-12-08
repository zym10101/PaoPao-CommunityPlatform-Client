package com.example.android_demo.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.android_demo.R;
import com.example.android_demo.adapter.CommunityAdapter;
import com.example.android_demo.databinding.FragmentCommunityBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author SummCoder
 * @date 2023/12/7 21:24
 */
public class CommunityFragment extends Fragment {

    private FragmentCommunityBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCommunityBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        TabLayout tabLayout_community = view.findViewById(R.id.tabLayout_community);
        ViewPager2 vp2_community = view.findViewById(R.id.vp2_community);
        vp2_community.setAdapter(new CommunityAdapter(requireActivity()));
        TabLayoutMediator tab = new TabLayoutMediator(tabLayout_community, vp2_community, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("热门");
                        break;
                    case 1:
                        tab.setText("推荐");
                        break;
                    case 2:
                        tab.setText("关注");
                        break;
                }
            }
        });
        tab.attach();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}