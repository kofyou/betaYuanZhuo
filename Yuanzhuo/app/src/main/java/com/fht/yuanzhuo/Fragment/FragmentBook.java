package com.fht.yuanzhuo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.support.v7.app.AppCompatActivity;


import com.fht.yuanzhuo.Adapter.MyBaseExpandableListAdapter;
import com.fht.yuanzhuo.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentBook extends Fragment{
    private List<String> groupArray;
    private List<List<String>> childArray;
    private List<String> tempArray;
    private Context mContext;
    private ExpandableListView exlist;
    private MyBaseExpandableListAdapter myAdapter = null;
    private Toolbar myToolBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vbookpage,container,false);
        mContext = getActivity();
        exlist =  view.findViewById(R.id.exlist);

        groupArray=new ArrayList<>();
        childArray=new ArrayList<>();

        groupArray.add("朋友");
        groupArray.add("圆桌骑士");

        tempArray = new ArrayList<>();
        tempArray.add("张三");
        tempArray.add("李四");
        childArray.add(tempArray);

        tempArray = new ArrayList<>();
        tempArray.add("魏论文");
        tempArray.add("黄建新");
        childArray.add(tempArray);

        myAdapter = new MyBaseExpandableListAdapter(mContext,groupArray,childArray);
        exlist.setAdapter(myAdapter);

        myToolBar=view.findViewById(R.id.friendToolBar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolBar);
        setHasOptionsMenu(true);
        return view;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        ((AppCompatActivity) getActivity()).getMenuInflater().inflate(R.menu.searchmenu,menu);  //search_menu是在menu里定义的，
        MenuItem item = menu.findItem(R.id.searchViewMenu); //search_menu.xml的一个对应的item的id
        final SearchView searchView  = (SearchView) item.getActionView();
        //一进入便自动获得焦点
        searchView.setIconified(true);
        //true为让SearchView显示为一个 搜索图标，点击才展开输入框
        searchView.setIconifiedByDefault(true);
        //显示提交按钮
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("输入手机号搜索");//显示提示
        //设置SearchView的 EditTxt， search_src_text为自带的id标志
        SearchView.SearchAutoComplete st = searchView.findViewById(R.id.search_src_text);
        st.setHintTextColor(getResources().getColor(android.R.color.white)); //设置银色
        st.setTextColor(getResources().getColor(android.R.color.white));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { //搜索提交
//                key = query;
//                mBookInfos.clear();
//                search(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        exlist.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
//                Intent intent = new Intent(getActivity(), PlayActivity.class);
//                intent.setData(Uri.parse(myAdapter.getList().get(groupPosition).videos.get(childPosition).url));
//                startActivity(intent);
//                Intent intent = new Intent(FragmentBook.this,FragmentMine.class);
//                startActivity(intent);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }
}
