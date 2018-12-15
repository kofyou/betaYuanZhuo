package com.fht.yuanzhuo.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.fht.yuanzhuo.Fragment.FragmentBook;
import com.fht.yuanzhuo.Fragment.FragmentHistory;
import com.fht.yuanzhuo.Fragment.FragmentHome;
import com.fht.yuanzhuo.Fragment.FragmentMine;
import com.fht.yuanzhuo.R;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //UI Object
    private TextView txt_topbar;
    private TextView txt_channel;
    private TextView txt_message;
    private TextView txt_better;
    private TextView txt_setting;
    private FrameLayout ly_content;

    private FragmentHome fragmentHome;
    private FragmentHistory fragmentHistory;
    private FragmentBook fragmentBook;
    private FragmentMine fragmentMine;
    private FragmentManager fManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

//        //底部导航栏
        fManager = getSupportFragmentManager();
        bindViews();
        txt_channel.performClick();
    }



//    //UI组件初始化与事件绑定
    private void bindViews() {
        txt_channel =  findViewById(R.id.txt_channel);
        txt_message = findViewById(R.id.txt_message);
        txt_better =  findViewById(R.id.txt_better);
        txt_setting =  findViewById(R.id.txt_setting);
        ly_content = findViewById(R.id.ly_content);

        txt_channel.setOnClickListener(this);
        txt_message.setOnClickListener(this);
        txt_better.setOnClickListener(this);
        txt_setting.setOnClickListener(this);
    }

    //重置所有文本的选中状态
    private void setSelected(){
        txt_channel.setSelected(false);
        txt_message.setSelected(false);
        txt_better.setSelected(false);
        txt_setting.setSelected(false);
    }

    //隐藏所有Fragment
    private void hideAllFragment(FragmentTransaction fragmentTransaction){
        if(fragmentHome != null)fragmentTransaction.hide(fragmentHome);
        if(fragmentHistory != null)fragmentTransaction.hide(fragmentHistory);
        if(fragmentBook != null)fragmentTransaction.hide(fragmentBook);
        if(fragmentMine != null)fragmentTransaction.hide(fragmentMine);
    }


    @Override
    public void onClick(View v) {
        FragmentTransaction fTransaction = fManager.beginTransaction();
        hideAllFragment(fTransaction);
        switch (v.getId()){
            case R.id.txt_channel:
                setSelected();
                txt_channel.setSelected(true);
                if(fragmentHome == null){
                    fragmentHome = new FragmentHome();
                    fTransaction.add(R.id.ly_content,fragmentHome);
                }else{
                    fTransaction.show(fragmentHome);
                }
                break;
            case R.id.txt_message:
                setSelected();
                txt_message.setSelected(true);
                if(fragmentHistory == null){
                    fragmentHistory = new FragmentHistory();
                    fTransaction.add(R.id.ly_content, fragmentHistory);
                }else{
                    fTransaction.show(fragmentHistory);
                }
                break;
            case R.id.txt_better:
                setSelected();
                txt_better.setSelected(true);
                if(fragmentBook == null){
                    fragmentBook = new FragmentBook();
                    fTransaction.add(R.id.ly_content, fragmentBook);
                }else{
                    fTransaction.show(fragmentBook);
                }
                break;
            case R.id.txt_setting:
                setSelected();
                txt_setting.setSelected(true);
                if(fragmentMine == null){
                    fragmentMine = new FragmentMine();
                    fTransaction.add(R.id.ly_content, fragmentMine);
                }else{
                    fTransaction.show(fragmentMine);
                }
                break;
        }
        fTransaction.commit();
    }


}

