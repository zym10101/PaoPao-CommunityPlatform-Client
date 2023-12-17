package com.example.android_demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.android_demo.R;
import com.example.android_demo.bean.CommunityExpandBean;

import java.util.List;

/**
 * @author SummCoder
 * @date 2023/12/17 12:17
 */
public class CommunityExpandableAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private List<CommunityExpandBean> communityExpandBeanList;

    public CommunityExpandableAdapter(Context context, List<CommunityExpandBean> communityExpandBeanList){
        this.context = context;
        this.communityExpandBeanList = communityExpandBeanList;
    }
    /**
     * 获取组的数目
     *
     * @return 返回一级列表组的数量
     */
    @Override
    public int getGroupCount() {
        return communityExpandBeanList == null ? 0 : communityExpandBeanList.size();
    }

    /**
     * 获取指定组中的子节点数量
     *
     * @param groupPosition 子元素组所在的位置
     * @return 返回指定组中的子数量
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return communityExpandBeanList.get(groupPosition).getChildrenDataList().size();
    }
    /**
     * 获取与给定组相关联的对象
     *
     * @param groupPosition 子元素组所在的位置
     * @return 返回指定组的子数据
     */
    @Override
    public Object getGroup(int groupPosition) {
        return communityExpandBeanList.get(groupPosition).getTitle();
    }
    /**
     * 获取与给定组中的给定子元素关联的数据
     *
     * @param groupPosition 子元素组所在的位置
     * @param childPosition 子元素的位置
     * @return 返回子元素的对象
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return communityExpandBeanList.get(groupPosition).getChildrenDataList().get(childPosition);
    }

    /**
     * 获取组在给定位置的ID（唯一的）
     *
     * @param groupPosition 子元素组所在的位置
     * @return 返回关联组ID
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 获取给定组中给定子元素的ID(唯一的)
     *
     * @param groupPosition 子元素组所在的位置
     * @param childPosition 子元素的位置
     * @return 返回子元素关联的ID
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * @return 确定id 是否总是指向同一个对象
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * @return 返回指定组的对应的视图 （一级列表样式）
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ParentHolder parentHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.parent_item, null);
            parentHolder = new ParentHolder();
            parentHolder.tvParent = convertView.findViewById(R.id.tv_parent);
            convertView.setTag(parentHolder);
        }else {
            parentHolder = (ParentHolder) convertView.getTag();
        }
        parentHolder.tvParent.setText(communityExpandBeanList.get(groupPosition).getTitle());

        return convertView;
    }

    /**
     * @return 返回指定位置对应子视图的视图
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildrenHolder childrenHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.children_item, null);
            childrenHolder = new ChildrenHolder();
            childrenHolder.tvChild = convertView.findViewById(R.id.tv_child);
            convertView.setTag(childrenHolder);
        }else {
            childrenHolder = (ChildrenHolder) convertView.getTag();
        }

        childrenHolder.tvChild.setText(communityExpandBeanList.get(groupPosition).getChildrenDataList().get(childPosition).getSubContent());

        return convertView;
    }

    /**
     * 指定位置的子元素是否可选
     *
     * @param groupPosition 子元素组所在的位置
     * @param childPosition 子元素的位置
     * @return 返回是否可选
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    static class ParentHolder {
        TextView tvParent;
    }

    static class ChildrenHolder {
        TextView tvChild;
    }

    /**
     * 用于刷新更新后的数据
     */
    public void reFreshData(List<CommunityExpandBean> communityExpandBeanList) {
        this.communityExpandBeanList = communityExpandBeanList;
        notifyDataSetChanged();
    }

}
