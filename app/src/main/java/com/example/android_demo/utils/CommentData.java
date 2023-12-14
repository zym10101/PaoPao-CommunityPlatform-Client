package com.example.android_demo.utils;

import java.io.Serializable;
import java.util.List;

public class CommentData {

    private String code;
    private String message;
    private List<Comment> data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Comment> getData() {
        return data;
    }

    public static class Comment implements Serializable {
        private String commentId;
        private String userName;
        private String postId;
        private String content;
        private String createTime;
        private String lastUpdateTime;

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(String lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }
    }
}
