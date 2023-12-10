package com.example.android_demo.utils;

import java.util.List;

public class PostData {
    private String code;
    private String message;
    private List<Post> data;

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Post> getData() {
        return data;
    }

    public static class Post {
        private String postId;
        private String communityId;
        private boolean isPublic;
        private List<String> tagList;
        private String title;
        private String content;
        private String photo;
        private String commentNum;
        private String likeNum;
        private String dislikeNum;
        private String userName;
        private String createTime;
        private String lastUpdateTime;

        public String getPostId() {
            return postId;
        }

        public String getCommunityId() {
            return communityId;
        }

        public boolean isPublic() {
            return isPublic;
        }

        public List<String> getTagList() {
            return tagList;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getPhoto() {
            return photo;
        }

        public String getCommentNum() {
            return commentNum;
        }

        public String getLikeNum() {
            return likeNum;
        }

        public String getDislikeNum() {
            return dislikeNum;
        }

        public String getUserName() {
            return userName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public String getLastUpdateTime() {
            return lastUpdateTime;
        }
    }
}
