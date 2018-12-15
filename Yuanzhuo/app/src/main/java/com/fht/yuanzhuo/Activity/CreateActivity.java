package com.fht.yuanzhuo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fht.yuanzhuo.R;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_create);

        Button confirmCreate=(Button)findViewById(R.id.confirmCreate);
        final EditText roomNum=findViewById(R.id.roomNum);
        final EditText roomName=findViewById(R.id.roomName);
        confirmCreate.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this,WaitActivity.class);
                intent.putExtra("roomNum",roomNum.getText().toString());
                intent.putExtra("role","1");
                //intent.putExtra("amount",amount);
                startActivity(intent);
                finish();
            }
        });
    }
}
