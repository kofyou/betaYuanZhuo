package com.fht.yuanzhuo.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fht.yuanzhuo.R;
import com.fht.yuanzhuo.UserDataApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class DetailActivity extends AppCompatActivity {
    private TextView Unickname;
    private TextView Usex;
    private TextView Ubirthday;
    private TextView Usign;
    private TextView Uzone;
    private ImageView imag1;


    private String status = new String();
    private String message = new String();
    private JSONObject jsonCont = null;
    @SuppressLint("HandlerLeak")
    private Handler uiHandler = new Handler() {
        // 覆写这个方法，接收并处理消息。
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 200:
                    try {
                        Ubirthday.setText(jsonCont.getJSONObject("data").getString("birthday"));
                        Usex.setText(jsonCont.getJSONObject("data").getString("sex"));
                        Usign.setText(jsonCont.getJSONObject("data").getString("sign"));
                        Unickname.setText(jsonCont.getJSONObject("data").getString("nickaneme"));
                        Uzone.setText(jsonCont.getJSONObject("data").getString("zone"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0:
                    Toast.makeText(DetailActivity.this,"账号或者密码出错",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(DetailActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_detail);

        Ubirthday = findViewById(R.id.Ubirthday);
        Unickname = findViewById(R.id.Unickname);
        Usex = findViewById(R.id.Usex);
        Usign  = findViewById(R.id.Usign);
        Uzone = findViewById(R.id.Uzone);
        imag1 = findViewById(R.id.img1);

        String url = "http://134.175.124.41:23333/yuanzhuo/user/"+ ((UserDataApp)getApplication()).getYuanzhuoId();
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("X-USER-TOKEN",((UserDataApp)getApplication()).getUserToken())
                .get()//默认就是GET请求，可以不写
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonString = response.body().string();
                Log.e("body",jsonString);

                try {
                    Message msg = new Message();
                    jsonCont = new JSONObject(jsonString);
                    status = jsonCont.getString("status");
                    message = jsonCont.getString("message");
                    if(status.equals("200")) {
                        msg.what = 200;
                    }else {
                        msg.what = 0;
                    }
                    uiHandler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }});
    }
}
