package com.example.android_demo.ui.square;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_demo.MainActivity;
import com.example.android_demo.MyApplication;
import com.example.android_demo.R;
import com.example.android_demo.databinding.FragmentSquareBinding;
import com.example.android_demo.utils.UserUtils;

import java.util.Objects;

public class SquareFragment extends Fragment {

    private FragmentSquareBinding binding;
    private View view;
    private GridLayout gl_posts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = FragmentSquareBinding.inflate(inflater, container, false).getRoot();
        gl_posts = view.findViewById(R.id.gl_posts);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "载入成功", Toast.LENGTH_SHORT).show();
        gl_posts.setVisibility(View.VISIBLE);//设置成可见
        for (int i = 0; i < 20; i++) {
            View view1 = LayoutInflater.from(getActivity()).inflate(R.layout.post_item, null);
            gl_posts.addView(view1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    public void updateContent(String username) {
        // 根据用户名更新内容
        // 可以调用一个方法从数据库或网络加载内容
    }

    public void refresh(){

    }

}