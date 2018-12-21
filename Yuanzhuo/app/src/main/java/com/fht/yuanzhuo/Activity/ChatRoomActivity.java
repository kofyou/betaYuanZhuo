package com.fht.yuanzhuo.Activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.fht.yuanzhuo.Adapter.ContentAdapter;
import com.fht.yuanzhuo.Adapter.ContentModel;
import com.fht.yuanzhuo.R;
import com.fht.yuanzhuo.UserDataApp;
import com.fht.yuanzhuo.Userclass.PDFPagerAdapter2;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.zego.zegoaudioroom.ZegoAudioRoom;
import com.zego.zegoaudioroom.ZegoAudioRoomDelegate;
import com.zego.zegoaudioroom.ZegoAudioStream;
import com.zego.zegoaudioroom.ZegoAudioStreamType;
import com.zego.zegoaudioroom.ZegoLoginAudioRoomCallback;
import com.zego.zegoaudioroom.callback.ZegoRoomMessageDelegate;
import com.zego.zegoliveroom.entity.ZegoBigRoomMessage;
import com.zego.zegoliveroom.entity.ZegoConversationMessage;
import com.zego.zegoliveroom.entity.ZegoRoomMessage;
import com.zego.zegoliveroom.entity.ZegoUserState;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

import static java.lang.Boolean.TRUE;


public class ChatRoomActivity extends AppCompatActivity implements DownloadFile.Listener,View.OnClickListener,PopupMenu.OnMenuItemClickListener{

    private List<ContentModel> list = new ArrayList<ContentModel>();/*侧边栏列表*/
    private DrawerLayout drawerLayout;
    private Button meetModel;
    private Button exit;
    private ListView listView;
    private ContentAdapter contentAdapter;
    private ImageView leftMenu;


    /*下面是用户名字符串数组*/
    private List<String> userName = new ArrayList<>();
//    private  String[] userName={"用户1","用户2","用户3","用户4","用户5"};

    int userNumber=userName.size();/*用户数量*/

    boolean[] isChanged2={true,true,true,true,true};/*颜色选择标签*/

    ImageView[] mImage = new ImageView[5];/*图标颜色*/

    private Boolean isMaster = false;
    private Boolean hasLogin = false;
    private Integer movePage = 0;
    private Integer PageNum = 0;
    private String sendData;
    private Queue<String> sendDatas = new LinkedList<String>();
//    private String[] sendDatas = new String[50];
    private Boolean sending = false;
    private Integer num = 0;

    private ImageView myCanvas;
    private Bitmap[] baseBitmaps;
    private Canvas canvas;
    private Paint paint;
    private ZegoAudioRoom zegoAudioRoom;
    private RelativeLayout pdf_root;
    private RemotePDFViewPager remotePDFViewPager;

    private String mUrl = "http://134.175.124.41/pdfFile/TEST.pdf";
    private PDFPagerAdapter2 adapter;

    AlertDialog alert = null;
    AlertDialog.Builder builder = null;
    /*下面是转换pdf用的*/
    static Image image;
    static byte[] bArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_chat_room);


        Intent intent=getIntent();
        String role    = intent.getStringExtra("role");
        String roomNum = intent.getStringExtra("roomNum");
        zegoAudioRoom = ((UserDataApp)getApplication()).getZegoAudioRoom();
        if(role.equals("1")){
            isMaster = true;
            zegoAudioRoom.enableMic(true);
        }else {
            isMaster = false;
        }
        zegoAudioRoom.setUserStateUpdate(true);
        zegoAudioRoom.enableAEC(true);
        zegoAudioRoom.enableAux(false);
        zegoAudioRoom.enableAECWhenHeadsetDetected(true);
        login(roomNum);



//        加载pdf和画笔
        initView();
        setDownloadListener();

        /*点击弹出侧边栏*/
        leftMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);

                disableDraw();
                initColor();
                initColorFlag();
            }
        });


        if (isMaster) {
            /*点击模式切换*/
            meetModel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //创建弹出式菜单对象（最低版本11）
                    PopupMenu popup = new PopupMenu(ChatRoomActivity.this, v);//第二个参数是绑定的那个view
                    //获取菜单填充器
                    MenuInflater inflater = popup.getMenuInflater();
                    //填充菜单
                    inflater.inflate(R.menu.menu_item, popup.getMenu());
                    //绑定菜单项的点击事件
                    popup.setOnMenuItemClickListener(ChatRoomActivity.this);
                    //显示(这一行代码不要忘记了)
                    popup.show();
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    ImageView button6=view.findViewById(R.id.xiangxia);
                    theOnClick(button6);/*点击后弹出选择主讲和禁言选项*/
                }
            });
        }else {
            meetModel.setVisibility(View.GONE);
        }



        if(role.equals("1")){
            exit.setText("解散房间");
        }
        else{
            exit.setText("退出");
        }
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isMaster){
                    builder = new AlertDialog.Builder(ChatRoomActivity.this);
                    alert = builder
                            .setMessage("要保存pdf文件到本地吗？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(ChatRoomActivity.this, "你点击了取消按钮~", Toast.LENGTH_SHORT).show();
                                    zegoAudioRoom.sendRoomMessage(1, 2, "over", new ZegoRoomMessageDelegate() {
                                        @Override
                                        public void onSendRoomMessage(int i, String s, long l) {
                                            if (hasLogin) {
                                                logout();
                                                adapter.close();
                                            }
                                            Intent intent = new Intent(ChatRoomActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(ChatRoomActivity.this, "你点击了确定按钮~", Toast.LENGTH_SHORT).show();
                                    storePdf();
                                    zegoAudioRoom.sendRoomMessage(1, 2, "over", new ZegoRoomMessageDelegate() {
                                        @Override
                                        public void onSendRoomMessage(int i, String s, long l) {
                                            if (hasLogin) {
                                                logout();
                                                adapter.close();
                                            }
                                            Intent intent = new Intent(ChatRoomActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }).create();             //创建AlertDialog对象
                    alert.show();                    //显示对话框
                }else{
                    if (hasLogin) {
                        logout();
                        adapter.close();
                    }
                    Intent intent = new Intent(ChatRoomActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });



    }


    public void login(String roomNum){
        zegoAudioRoom.loginRoom(roomNum, new ZegoLoginAudioRoomCallback() {
            @Override
            public void onLoginCompletion(int i) {
                if(i != 0){
                    Toast.makeText(ChatRoomActivity.this,"进入房间失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
        setupCallbacks();
    }

    private void setupCallbacks() {
        zegoAudioRoom.setAudioRoomDelegate(new ZegoAudioRoomDelegate() {
            @Override
            public void onKickOut(int i, String s) {

            }

            @Override
            public void onDisconnect(int i, String s) {

            }

            @Override
            public void onStreamUpdate(ZegoAudioStreamType zegoAudioStreamType, ZegoAudioStream zegoAudioStream) {

            }

            @Override
            public void onUserUpdate(ZegoUserState[] zegoUserStates, int i) {
                for(int x = 0;x<zegoUserStates.length;x++){
                    list.add(new ContentModel(zegoUserStates[x].userName,x));
                }
                contentAdapter.notifyDataSetChanged();
            }

            @Override   //用长度跳转
            public void onUpdateOnlineCount(String s, int i) {
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onRecvRoomMessage(String s, ZegoRoomMessage[] zegoRoomMessages) {
                for (int x = 0 ; x < zegoRoomMessages.length ;  x++){
                    if(zegoRoomMessages[x].messageType == 100 && zegoRoomMessages[x].messageCategory == 100){//翻页广播
                        remotePDFViewPager.setCurrentItem( Integer.parseInt(zegoRoomMessages[x].content),true);
                        updateLayout();
                        myCanvas.setImageBitmap(baseBitmaps[movePage]);
                        Toast.makeText(ChatRoomActivity.this,"主讲人翻到"+zegoRoomMessages[x].content+"页",Toast.LENGTH_SHORT).show();
                    }else if(zegoRoomMessages[x].messageType == 1 && zegoRoomMessages[x].messageCategory == 2 && zegoRoomMessages[x].content.equals("over")){//解散房间广播
                            Toast.makeText(ChatRoomActivity.this,"主讲人结束了会议",Toast.LENGTH_SHORT).show();
                            if (hasLogin) {
                                logout();
                                adapter.close();
                            }
                            Intent intent = new Intent(ChatRoomActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                    }else if(zegoRoomMessages[x].messageType == 1 && zegoRoomMessages[x].messageCategory == 2 &&zegoRoomMessages[x].content.equals("silent")){//主讲模式
                        zegoAudioRoom.enableMic(false);
                        Toast.makeText(ChatRoomActivity.this,"模式切换为主讲模式",Toast.LENGTH_SHORT).show();
                    }else if(zegoRoomMessages[x].messageType == 1 && zegoRoomMessages[x].messageCategory == 2 &&zegoRoomMessages[x].content.equals("free")){//自由模式
                        zegoAudioRoom.enableMic(true);
                        Toast.makeText(ChatRoomActivity.this,"模式切换为自由模式",Toast.LENGTH_SHORT).show();
                    }else  if(zegoRoomMessages[x].messageType == 1 && zegoRoomMessages[x].messageCategory == 4){//画笔监听
                        Log.e("打印坐标",zegoRoomMessages[x].content.trim());
                        if (baseBitmaps[movePage] == null) {
                            baseBitmaps[movePage] = Bitmap.createBitmap(myCanvas.getWidth(),
                                    myCanvas.getHeight(), Bitmap.Config.ARGB_8888);
                            canvas = new Canvas(baseBitmaps[movePage]);
                            canvas.drawColor(Color.TRANSPARENT);
                        }
                        if(paint == null){
                            paint = new Paint();
                        }
                        paint.setStrokeWidth(5);
                        paint.setColor(R.color.colorBlack);
                        String[] points = zegoRoomMessages[x].content.trim().split("-");
                        for(int j = 0 ;j < points.length ; j++){
                            String[] point = points[j].trim().split(",");
                            float fromx = Float.valueOf(point[0]);
                            float fromy = Float.valueOf(point[1]);
                            float tox  = Float.valueOf(point[2]);
                            float toy =Float.valueOf(point[3]);
                            canvas.drawLine(fromx,fromy,tox,toy, paint);
                            myCanvas.setImageBitmap(baseBitmaps[movePage]);
                        }
                    }
                }
            }

            @Override
            public void onRecvConversationMessage(String s, String s1, ZegoConversationMessage zegoConversationMessage) {

            }

            @Override
            public void onRecvBigRoomMessage(String s, ZegoBigRoomMessage[] zegoBigRoomMessages) {

            }

            @Override
            public void onRecvCustomCommand(String s, String s1, String s2, String s3) {

            }

            @Override
            public void onStreamExtraInfoUpdated(ZegoAudioStream[] zegoAudioStreams, String s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (hasLogin) {
            logout();
            adapter.close();
        }
        super.onBackPressed();
    }

    private void logout() {
        zegoAudioRoom.enableAux(false);     // 停止伴音
        zegoAudioRoom.logoutRoom();
        hasLogin = false;
        removeCallbacks();
    }

    private void removeCallbacks() {
        zegoAudioRoom.setAudioRoomDelegate(null);
        zegoAudioRoom.setAudioPublisherDelegate(null);
        zegoAudioRoom.setAudioPlayerDelegate(null);
        zegoAudioRoom.setAudioLiveEventDelegate(null);
        zegoAudioRoom.setAudioRecordDelegate(null);
        zegoAudioRoom.setAudioDeviceEventDelegate(null);
        zegoAudioRoom.setAudioPrepareDelegate(null);
        zegoAudioRoom.setAudioAVEngineDelegate(null);
    }
    private View.OnTouchListener touch = new View.OnTouchListener() {
        // 定义手指开始触摸的坐标
        float startX;
        float startY;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                // 用户按下动作
                case MotionEvent.ACTION_DOWN:
                    // 第一次绘图初始化内存图片，指定背景为白色
                    if(baseBitmaps[movePage] == null){
                        baseBitmaps[movePage] = Bitmap.createBitmap(myCanvas.getWidth(),
                                myCanvas.getHeight(), Bitmap.Config.ARGB_8888);
                    }
                    canvas = new Canvas(baseBitmaps[movePage]);
                    canvas.drawColor(Color.TRANSPARENT);
                    // 记录开始触摸的点的坐标
                    startX = event.getX();
                    startY = event.getY();
                    break;
                // 用户手指在屏幕上移动的动作
                case MotionEvent.ACTION_MOVE:
                    // 记录移动位置的点的坐标
                    float stopX = event.getX();
                    float stopY = event.getY();
                    //根据两点坐标，绘制连线
                    if(num >= 10){
                        sendDatas.offer(sendData);
                        num = 0;
                        sendData = null;
                    }

                    if(sendData  == null){
                        sendData = startX + "," + startY + "," + stopX + "," + stopY;
                        num++;
                    }else {
                        sendData += "-" + startX + "," + startY + "," + stopX + "," + stopY;
                        num++;
                    }
                    canvas.drawLine(startX, startY, stopX, stopY, paint);
                    // 更新开始点的位置
                    startX = event.getX();
                    startY = event.getY();

                    // 把图片展示到ImageView中
                    myCanvas.setImageBitmap(baseBitmaps[movePage]);
                    break;
                case MotionEvent.ACTION_UP:
                    if(sendData != null){
                        sendDatas.offer(sendData);
                        num = 0;
                        sendData = null;
                    }
                    sendBiji();
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    private void sendBiji(){
        if(!sending){
            sending = true;
            if(!sendDatas.isEmpty()){
                Boolean issend =  zegoAudioRoom.sendRoomMessage(1, 4,sendDatas.element(), new ZegoRoomMessageDelegate() {
                    @Override
                    public void onSendRoomMessage(int i, String s, long l) {
                        sending = false;
                        sendBiji();
                    }
                });
                if(!issend){
                    sending = false;
                    sendBiji();
                }else {
                    sendDatas.poll();
                }
            }else {
                    sending = false;
            }
        }
    }

    protected void initView() {
        pdf_root = findViewById(R.id.remote_pdf_root);
        myCanvas = findViewById(R.id.myCanvas);
        listView = findViewById(R.id.left_listview);
        leftMenu = findViewById(R.id.leftmenu);
        drawerLayout = findViewById(R.id.drawerlayout);
        meetModel=findViewById(R.id.meetingModel);
        exit=findViewById(R.id.exit);
        initData(userNumber,userName);
        contentAdapter = new ContentAdapter(this, list);
        listView.setAdapter(contentAdapter);
        mImage[0] = findViewById(R.id.blue);
        mImage[1] = findViewById(R.id.green);
        mImage[2] = findViewById(R.id.orange);
        mImage[3] = findViewById(R.id.red);
        mImage[4] = findViewById(R.id.yellow);
        for(int i=0;i<5;i++)
        {
            mImage[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeColor(v);
                }
            });
        }
    }

    protected void setDownloadListener() {
        final DownloadFile.Listener listener = this;
        remotePDFViewPager = new RemotePDFViewPager(this, mUrl, listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);
    }

    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter2(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        PageNum = remotePDFViewPager.getAdapter().getCount();
        baseBitmaps = new Bitmap[PageNum];
        remotePDFViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                movePage = i;
                if (isMaster) {
                    zegoAudioRoom.sendRoomMessage(100, 100,String.valueOf(movePage) , new ZegoRoomMessageDelegate() {
                        @Override
                        public void onSendRoomMessage(int i, String s, long l) {

                        }
                    });
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        updateLayout();
    }

    private void updateLayout() {
        pdf_root.removeAllViewsInLayout();
        pdf_root.addView(remotePDFViewPager, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onFailure(Exception e) {
        Toast.makeText(this,"pdf下载失败,请重新进入房间", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
    }

    @Override
    protected void onDestroy() {
        if (hasLogin) {
            logout();
            adapter.close();
        }
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {

    }

    /*点击用户列表的按钮后弹出菜单*/
    public void theOnClick(View v){
        PopupMenu popup = new PopupMenu(ChatRoomActivity.this, v);//第二个参数是绑定的那个view
        //获取菜单填充器
        MenuInflater inflater = popup.getMenuInflater();
        //填充菜单
        inflater.inflate(R.menu.user_menu, popup.getMenu());
        //绑定菜单项的点击事件
        popup.setOnMenuItemClickListener(ChatRoomActivity.this);
        //显示(这一行代码不要忘记了)
        popup.show();
    }

    //弹出式菜单的单击事件处理////////////////////////////////////////////////////////////////////////模式切换的弹出式菜单，点击触发模式切换
    public boolean onMenuItemClick(MenuItem item) {
        // TODO Auto-generated method stub
        Toast.makeText(this,item.getTitle(), Toast.LENGTH_SHORT).show();
        if(item.getTitle().toString().equals("自由模式")){//xms
            zegoAudioRoom.sendRoomMessage(1, 2, "free", new ZegoRoomMessageDelegate() {
                @Override
                public void onSendRoomMessage(int i, String s, long l) {}
            });
        }else if(item.getTitle().toString().equals("主讲模式")){
            zegoAudioRoom.sendRoomMessage(1, 2, "silent", new ZegoRoomMessageDelegate() {
                @Override
                public void onSendRoomMessage(int i, String s, long l) { }
            });

        }
        return false;
    }



    /*初始化侧边栏*/
    private void initData(int userNumber,List<String> userName) {
        for(int j=0;j<userNumber;j++)
        {
            list.add(new ContentModel(userName.get(j),j));
        }
    }


    /*选择画笔颜色*/
    public void changeColor(View v)
    {
        initColor();
        switch (v.getId())
        {
            case R.id.blue:
                if(isChanged2[0])
                {
                    mImage[0].setImageResource(R.mipmap.bluepen2);
                    initColorFlag();
                    isChanged2[0] = false;
                    enableDraw(R.color.blue);
                }else{
                    mImage[0].setImageResource(R.mipmap.bluepen);
                    initColorFlag();
                    disableDraw();
                }
                break;
            case R.id.green:
                if(isChanged2[1])
                {
                    mImage[1].setImageResource(R.mipmap.greenpen2);
                    initColorFlag();
                    isChanged2[1] = false;
                    enableDraw(R.color.green);
                }else{
                    mImage[1].setImageResource(R.mipmap.greenpen);
                    initColorFlag();
                    disableDraw();
                }
                break;
            case R.id.orange:
                if(isChanged2[2])
                {
                    mImage[2].setImageResource(R.mipmap.orangepen2);
                    initColorFlag();
                    isChanged2[2] = false;
                    enableDraw(R.color.orange);
                }else{
                    mImage[2].setImageResource(R.mipmap.orangepen);
                    initColorFlag();
                    disableDraw();
                }
                break;
            case R.id.red:
                if(isChanged2[3])
                {
                    mImage[3].setImageResource(R.mipmap.redpen2);
                    initColorFlag();
                    isChanged2[3] = false;
                    enableDraw(R.color.red);
                }else{
                    mImage[3].setImageResource(R.mipmap.redpen);
                    initColorFlag();
                    disableDraw();
                }
                break;
            case R.id.yellow:
                if(isChanged2[4])
                {
                    mImage[4].setImageResource(R.mipmap.yellowpen2);
                    initColorFlag();
                    isChanged2[4] = false;
                    enableDraw(R.color.yellow);
                }else{
                    mImage[4].setImageResource(R.mipmap.yellowpen);
                    initColorFlag();
                    disableDraw();
                }
                break;
            default:
                break;
        }
    }

    /*将所有颜色的flag都设置为true*/
    public  void initColorFlag()
    {
        for(int i=0;i<5;i++)
        {
            isChanged2[i]=true;
        }
    }

    /*初始化画笔颜色图片*/
    public void initColor()
    {
        mImage[0].setImageResource(R.mipmap.bluepen);
        mImage[1].setImageResource(R.mipmap.greenpen);
        mImage[2].setImageResource(R.mipmap.orangepen);
        mImage[3].setImageResource(R.mipmap.redpen);
        mImage[4].setImageResource(R.mipmap.yellowpen);
    }

    protected void enableDraw(int colorIndex){
        paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(ContextCompat.getColor(this, colorIndex));
        if(baseBitmaps[movePage] == null){
            baseBitmaps[movePage] = Bitmap.createBitmap(myCanvas.getWidth(),
                    myCanvas.getHeight(), Bitmap.Config.ARGB_8888);
            canvas = new Canvas(baseBitmaps[movePage]);
            canvas.drawColor(Color.TRANSPARENT);
        }
        myCanvas.setImageBitmap(baseBitmaps[movePage]);
        myCanvas.setOnTouchListener(touch);
    }

    protected void disableDraw(){
        paint = null;
        myCanvas.setOnTouchListener(null);
        Bitmap baseBitmap = Bitmap.createBitmap(myCanvas.getWidth(),
                    myCanvas.getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(baseBitmap);
        canvas.drawColor(Color.TRANSPARENT);

        myCanvas.setImageBitmap(baseBitmap);
    }

    protected void storePdf(){
        new Thread() {
            public void run() {
                try
                {
                    File sdDir = null;
                    boolean sdCardExist = Environment.getExternalStorageState().equals(
                            android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
                    if (sdCardExist) {
                        sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
                    }
                    String FILE = sdDir.toString()+"/finallyvol1.pdf";
                    Log.e("file path",FILE);
                    final Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(FILE,TRUE));
                    document.open();

                    int[] positionInsiede=adapter.getPositionInside();
                    for(int i=0;i<PageNum;i++) //一次
                    {

                        //convert bitmap to bytearray
                        Bitmap tmpBitmap1=adapter.getBitmap(i);
                        Bitmap tmpBitmap2=baseBitmaps[i];
                        if(i==0)
                        {
                            Log.e("back",tmpBitmap1.getWidth()+" "+tmpBitmap1.getHeight());
                            Log.e("fore",tmpBitmap2.getWidth()+" "+tmpBitmap2.getHeight());
                        }

                        Bitmap tmpBitmap=combineBitmap(tmpBitmap1,tmpBitmap2,positionInsiede[2],positionInsiede[3]);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        tmpBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        bArray = baos.toByteArray();
                        addImage(document);
                    }
                    document.close();
                }

                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public static Bitmap combineBitmap(Bitmap background, Bitmap foreground,int newBackWidth,int newBackHeight)
    {
        if (background == null)
        {
            return null;
        }
        if (foreground == null )
        {
            return background;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        int fgWidth = foreground.getWidth();
        int fgHeight = foreground.getHeight();
        Log.e("back w h ;fore w h",bgWidth+" "+bgHeight+" "+fgWidth+" "+fgHeight);
        Bitmap newmap = Bitmap.createBitmap(fgWidth, fgHeight, Bitmap.Config.ARGB_8888);
//        Bitmap newmap = Bitmap.createBitmap(bgWidth, bgHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(newmap,0,0,null);
//        canvas.drawBitmap(background, 0, 0, null);

        // 计算缩放比例
        float scaleWidth = ((float) newBackWidth) / bgWidth;
        float scaleHeight = ((float) newBackHeight) / bgHeight;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbackground = Bitmap.createBitmap(background, 0, 0, bgWidth, bgHeight, matrix,
                true);

        canvas.drawBitmap(newbackground, (fgWidth-720) / 2,
                (fgHeight-458) / 2, null);

//        canvas.drawBitmap(foreground, (bgWidth - fgWidth) / 2,
//                (bgHeight - fgHeight) / 2, null);
        canvas.drawBitmap(foreground, 0, 0, null);

        canvas.save();
        canvas.restore();
        return newmap;
    }

    private static void addImage(Document document)
    {

        try
        {
            Log.e("line",String.valueOf(22));
            image = Image.getInstance(bArray);  ///Here i set byte array..you can do bitmap to byte array and set in image...

            Log.e("line",String.valueOf(22.5));
        }
        catch (BadElementException e)
        {
            Log.e("line",String.valueOf(23));
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (MalformedURLException e)
        {Log.e("line",String.valueOf(24));
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {Log.e("line",String.valueOf(25));
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // image.scaleAbsolute(150f, 150f);
        try
        {
            Log.e("line",String.valueOf(26));
            image.scaleToFit(PageSize.A4.getWidth(),PageSize.A4.getHeight());
            document.add(image);
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

