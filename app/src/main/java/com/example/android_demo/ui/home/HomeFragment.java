package com.example.android_demo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.android_demo.MainViewModel;
import com.example.android_demo.R;
import com.example.android_demo.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    List<HashMap<String,Object>> list = new ArrayList<>();

    public List<HashMap<String,Object>> putData(){
        HashMap<String,Object> map1 = new HashMap<>();
        map1.put("name", "代码随想录");
        map1.put("id", "coder");
        map1.put("age", "2岁");
        map1.put("tel", "18322228898");
        map1.put("pic", R.drawable.sui);
        HashMap<String,Object> map2 = new HashMap<>();
        map2.put("name", "李四");
        map2.put("id", "jyw8886");
        map2.put("age", "27岁");
        map2.put("tel", "13922278898");
        map2.put("pic",  R.drawable.sui);
        HashMap<String,Object> map3 = new HashMap<>();
        map3.put("name", "王五");
        map3.put("id", "bmw8899");
        map3.put("age", "27岁");
        map3.put("tel", "13319780706");
        map3.put("pic",  R.drawable.sui);
        HashMap<String,Object> map4 = new HashMap<>();
        map4.put("name", "听雨");
        map4.put("id", "wh445306");
        map4.put("age", "28岁");
        map4.put("tel", "17322228898");
        map4.put("pic",  R.drawable.sui);
        HashMap<String,Object> map5 = new HashMap<>();
        map5.put("name", "若兰");
        map5.put("id", "jyw8886");
        map5.put("age", "27岁");
        map5.put("tel", "13922278898");
        map5.put("pic",  R.drawable.sui);
        HashMap<String,Object> map6 = new HashMap<>();
        map6.put("name", "海子");
        map6.put("id", "bmw8899");
        map6.put("age", "27岁");
        map6.put("tel", "13319780706");
        map6.put("pic",  R.drawable.sui);
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.add(map5);
        list.add(map6);
        return list;
    }
    MainViewModel mainViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ListView listview = binding.lvTest;

        putData();

        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),list, R.layout.community_item_layout,
                new String[]{"name","id","age","pic","tel"},
                new int[]{R.id.txtCommunityName,R.id.txtCommunityID,R.id.txtUserAge,R.id.imgHead,R.id.txtUserTel});
        listview.setAdapter(simpleAdapter);

        //尝试获取共享数据viewmodel
        mainViewModel=new ViewModelProvider(getActivity()).get(MainViewModel.class);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}