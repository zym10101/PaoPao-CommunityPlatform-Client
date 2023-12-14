package com.example.android_demo.adapter;

// 适配器类 CommentAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android_demo.R;
import com.example.android_demo.utils.CommentData;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<CommentData.Comment> {

    public CommentAdapter(Context context, List<CommentData.Comment> comments) {
        super(context, 0, comments);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommentData.Comment comment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);
        }

        // 获取 comment_item.xml 中的控件
        ImageView avatarImageView = convertView.findViewById(R.id.avatar);
        TextView usernameTextView = convertView.findViewById(R.id.username1);
        TextView postTimeTextView = convertView.findViewById(R.id.postTime1);
        TextView contentTextView = convertView.findViewById(R.id.tv_content);

        // 设置控件的值
        // 请替换以下内容，以适应 Comment 对象的属性
        // 例如：comment.getAvatar(), comment.getUsername(), comment.getPostTime(), comment.getContent()
        avatarImageView.setImageResource(R.drawable.avatar0);
        usernameTextView.setText("用户名");
        postTimeTextView.setText("时间");
        contentTextView.setText("评论内容");

        return convertView;
    }
}
