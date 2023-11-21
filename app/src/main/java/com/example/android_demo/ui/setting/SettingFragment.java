package com.example.android_demo.ui.setting;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_demo.MainActivity;
import com.example.android_demo.MainViewModel;
import com.example.android_demo.databinding.FragmentSettingBinding;

public class SettingFragment extends Fragment {
    private FragmentSettingBinding binding;
    private MainViewModel mainViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingBinding.inflate(inflater, container, false);

        //获取textView的时候也通过binding获取
        final TextView textView = binding.textSetting;

        //获取viewModel,用来存储数据
        SettingViewModel settingViewModel = new ViewModelProvider(this).get(SettingViewModel.class);

        //尝试获取共享数据viewmodel
        mainViewModel=new ViewModelProvider(getActivity()).get(MainViewModel.class);

        //适用于textview的观察函数
        final Observer<String> nameObserver = newName -> textView.setText(newName);

        //给username加上观察器，当username值发生改变的时候，调用观察函数
        mainViewModel.getUsername().observe(getActivity(), nameObserver);

        return binding.getRoot();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}