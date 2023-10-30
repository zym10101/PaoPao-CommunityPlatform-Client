package com.example.android_demo.ui.square;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_demo.databinding.FragmentSquareBinding;

public class SquareFragment extends Fragment {

    private FragmentSquareBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SquareViewModel squareViewModel =
                new ViewModelProvider(this).get(SquareViewModel.class);

        binding = FragmentSquareBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSquare;
        squareViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}