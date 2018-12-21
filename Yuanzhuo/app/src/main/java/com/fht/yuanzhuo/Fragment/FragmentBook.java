package com.fht.yuanzhuo.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.fht.yuanzhuo.Activity.DetailActivity;
import com.fht.yuanzhuo.Activity.FriendActivity;
import com.fht.yuanzhuo.Adapter.MyBaseExpandableListAdapter;
import com.fht.yuanzhuo.R;
import com.fht.yuanzhuo.UserDataApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;
import static java.lang.Boolean.TRUE;

public class FragmentBook extends Fragment{
    private List<String> groupArray;
    private List<List<String>> childArray;
    private List<String> tempArray;
    private Context mContext;
    private ExpandableListView exlist;
    private MyBaseExpandableListAdapter myAdapter = null;
    private Toolbar myToolBar;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
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
        MenuItem item2 =  menu.findItem(R.id.newGroup);
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
                //向服务器发送数据
                // query; 返回用户id
                String url = "http://134.175.124.41:23333/yuanzhuo/phonenum/"+ query;
                OkHttpClient okHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .addHeader("X-USER-TOKEN", ((UserDataApp)getActivity().getApplication()).getUserToken())
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String jsonString = response.body().string();
                        Log.e("body",jsonString);
                        JSONObject jsonCont = null;
                        try {
                            jsonCont = new JSONObject(jsonString);
                            String status = jsonCont.getString("status");
                            if("200".equals(status)) {

                                int ismyself=jsonCont.getJSONObject("data").getInt("ismyself");
                                if(ismyself==1)
                                {
                                    Intent intent = new Intent(getContext(),DetailActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    String userID=jsonCont.getJSONObject("data").getString("yuanzhuoid");
                                    Intent intent = new Intent(getContext(),FriendActivity.class);
                                    intent.putExtra("userID",userID);
                                    startActivity(intent);
                                }
                            }else if(status.equals("1001")){

                            }else if(status.equals("1002")){

                            }else if(status.equals("1003")){

                            }else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.newGroup:
                builder = new AlertDialog.Builder(mContext);
                final LayoutInflater inflater = FragmentBook.this.getLayoutInflater();
                final View view_custom= inflater.inflate(R.layout.calertdialog, null,false);

                builder.setView(view_custom);
                builder.setCancelable(false);
                alert = builder.create();                   //显示对话框
                view_custom.findViewById(R.id.cancleName).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                view_custom.findViewById(R.id.confirmName).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText groupName=view_custom.findViewById(R.id.groupName);
                        String gName=groupName.getText().toString();
                        Toast.makeText(getContext(), gName+"???", Toast.LENGTH_SHORT).show();
                        groupArray.add(gName);

                        tempArray = new ArrayList<>();
                        childArray.add(tempArray);
                        myAdapter.notifyDataSetChanged();
                        //  向服务器发送数据
                        alert.dismiss();
                    }
                });
                alert.show();
                break;

            default:
                break;
        }



        return super.onOptionsItemSelected(item);
    }
}
