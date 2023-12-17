package com.example.android_demo.bean;

import java.util.List;

/**
 * @author SummCoder
 * @date 2023/12/17 12:20
 */
public class CommunityExpandBean {
    private String title;
    private List<ChildrenData> childrenDataList;

    public CommunityExpandBean(String title, List<ChildrenData> childrenDataList) {
        this.title = title;
        this.childrenDataList = childrenDataList;
    }

    public List<ChildrenData> getChildrenDataList() {
        return childrenDataList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setChildrenDataList(List<ChildrenData> childrenDataList) {
        this.childrenDataList = childrenDataList;
    }

    public static class ChildrenData{
        private String subContent;//子内容
        public ChildrenData(String subContent) {
            this.subContent = subContent;
        }

        public String getSubContent() {
            return subContent;
        }

        public void setSubContent(String subContent) {
            this.subContent = subContent;
        }
    }
}
