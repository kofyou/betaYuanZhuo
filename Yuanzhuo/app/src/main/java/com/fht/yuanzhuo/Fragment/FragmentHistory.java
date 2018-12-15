package com.fht.yuanzhuo.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.fht.yuanzhuo.R;
import com.fht.yuanzhuo.Userclass.History;


import java.util.ArrayList;
import java.util.List;

public class FragmentHistory extends Fragment{
    private List<History> history = new ArrayList<History>();//可变数组list
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vhistorypage,container,false);
        //定义用例测试
        history.add(new History("软工实践第1次会议","2018年5月12日12:00","123"));
        history.add(new History("实习会议2","2018年6月12日12:00","123"));
        history.add(new History("实习会议3","2018年7月22日12:00","123"));
        history.add(new History("软工实践第2次会议","2018年7月29日14:00","123"));
        history.add(new History("软工实践第3次会议","2018年9月11日10:00","123"));

        ListView lv=view.findViewById(R.id.lv);//找控件*/
        lv.setAdapter(new MyListAdapter());
        return view;
    }
    private class MyListAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return history.size();//显示的个数
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override//获取一个view，用来显示listview，会作为listview的一个条目出现
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View historyView = LayoutInflater.from(getActivity()).inflate(
                    R.layout.history, null);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT     );
            TextView rName = historyView.findViewById(R.id.rName);
            String rname = history.get(position).getRoomName();
            rName.setText(rname);


            TextView Htime = historyView.findViewById(R.id.Htime);
            String htime = history.get(position).getTime();
            Htime.setText(htime);

            layoutParams.height = 100;
            historyView.setLayoutParams(layoutParams);
            return historyView;
        }
    }
}
