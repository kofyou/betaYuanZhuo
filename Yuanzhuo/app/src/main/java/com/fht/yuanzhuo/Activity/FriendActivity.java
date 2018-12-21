package com.fht.yuanzhuo.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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
import static java.lang.Boolean.FALSE;

public class FriendActivity extends AppCompatActivity {
    private TextView Unickname;
    private TextView Usex;
    private TextView Ubirthday;
    private TextView Usign;
    private TextView Uzone;
    private ImageView imag1;
    private Button addAndDelete;

    private String status = new String();
    private String message = new String();
    private JSONObject jsonCont = null;
    private int isFriend;
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
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
                        isFriend    = jsonCont.getJSONObject("data").getInt("isfriend");
                        if(isFriend==1)
                        {
                            addAndDelete.setText("删除联系人");
                        }
                        else
                        {
                            addAndDelete.setText("添加联系人");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0:
                    Toast.makeText(FriendActivity.this,"账号或者密码出错",Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(FriendActivity.this,"请求失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //xms
        Intent intent=getIntent();
        String userID=intent.getStringExtra("userID");

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_friend);

        Ubirthday = findViewById(R.id.Ubirthday);
        Unickname = findViewById(R.id.Unickname);
        Usex = findViewById(R.id.Usex);
        Usign  = findViewById(R.id.Usign);
        Uzone = findViewById(R.id.Uzone);
        imag1 = findViewById(R.id.img1);
        addAndDelete =findViewById(R.id.addAndDelete);
        addAndDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFriend==1)
                {
                    alert = null;
                    builder = new AlertDialog.Builder(FriendActivity.this);
                    alert = builder
                            .setMessage("确认要删除该联系人吗？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(FriendActivity.this, "你点击了取消按钮~", Toast.LENGTH_SHORT).show();

                                    alert.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(FriendActivity.this, "你点击了确定按钮~", Toast.LENGTH_SHORT).show();


                                    alert.dismiss();
                                }
                            })
                            .create();             //创建AlertDialog对象
                    alert.show();                    //显示对话框
                }
                else {
                    // xms 这里要用用户数据
                    final String[] fruits = new String[]{"苹果", "雪梨", "香蕉", "葡萄", "西瓜"};
                    String choice;
                    alert = null;
                    builder = new AlertDialog.Builder(FriendActivity.this);
                    alert = builder
                            .setTitle("请选择分组")
                            .setSingleChoiceItems(fruits, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(), "你选择了" + fruits[which], Toast.LENGTH_SHORT).show();
                                    //choice=fruits[which];
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(FriendActivity.this, "你点击了取消按钮~", Toast.LENGTH_SHORT).show();
                                    alert.dismiss();
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(FriendActivity.this, "你点击了确定按钮~", Toast.LENGTH_SHORT).show();

                                    alert.dismiss();
                                }
                            }).create();
                    alert.show();
                }
            }
        });




        String url = "http://134.175.124.41:23333/yuanzhuo/user/"+ userID;   //xms
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
