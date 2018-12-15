package com.fht.yuanzhuo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fht.yuanzhuo.R;

public class JoinActivity extends AppCompatActivity {
    private String roomNum;
    private EditText roomNumCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_join);
        Button confirmJoin=(Button)findViewById(R.id.confirmJoin);
        roomNumCon =findViewById(R.id.roomNum);

        confirmJoin.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                roomNum = roomNumCon.getText().toString().trim();
                if(roomNum.isEmpty()){
                    Toast.makeText(JoinActivity.this,"房间号不能为空",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(JoinActivity.this,WaitActivity.class);
                    intent.putExtra("roomNum",roomNum);
                    intent.putExtra("role","0");
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
