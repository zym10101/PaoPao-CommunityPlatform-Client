package com.example.android_demo.adapter;

// 适配器类 CommentAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android_demo.R;
import com.example.android_demo.utils.CommentData;
import com.example.android_demo.utils.TimeUtils;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<CommentData.Comment> {

    public CommentAdapter(Context context, List<CommentData.Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);
        }

        // 获取当前位置的评论对象
        CommentData.Comment comment = getItem(position);

        // 获取视图
        ImageView avatarImageView = convertView.findViewById(R.id.avatar);
        TextView usernameTextView = convertView.findViewById(R.id.username1);
        TextView postTimeTextView = convertView.findViewById(R.id.postTime1);
        TextView contentTextView = convertView.findViewById(R.id.tv_content);


        assert comment != null;

        // 设置头像（这里使用了占位图 avatar0）
        Glide.with(this.getContext())
                .load(comment.getPhoto())
                .into(avatarImageView);

        // 设置用户名
        usernameTextView.setText(comment.getUserName());

        // 设置发布时间
        postTimeTextView.setText(TimeUtils.convert(comment.getCreateTime()));

        // 设置评论内容
        contentTextView.setText(comment.getContent());

        return convertView;
    }
}
