package com.fht.yuanzhuo.Fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.fht.yuanzhuo.Activity.CreateActivity;
import com.fht.yuanzhuo.Activity.JoinActivity;
import com.fht.yuanzhuo.R;

public class FragmentHome extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.homepage,container,false);

        Button joinBtn=(Button)view.findViewById(R.id.join);
        joinBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),JoinActivity.class);
                startActivity(intent);
            }
        });

        Button createBtn=(Button)view.findViewById(R.id.create);
        createBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),CreateActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}
