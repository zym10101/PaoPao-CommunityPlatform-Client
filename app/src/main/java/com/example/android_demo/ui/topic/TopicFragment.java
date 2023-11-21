package com.example.android_demo.ui.topic;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_demo.databinding.FragmentTopicBinding;

public class TopicFragment extends Fragment {

    private FragmentTopicBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TopicViewModel topicViewModel =
                new ViewModelProvider(this).get(TopicViewModel.class);

        binding = FragmentTopicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textTopic;
        topicViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}