package com.fht.yuanzhuo;

import android.app.Application;
import android.util.Log;

import com.fht.yuanzhuo.Activity.WaitActivity;
import com.zego.zegoaudioroom.ZegoAudioRoom;
import com.zego.zegoliveroom.constants.ZegoConstants;
import com.zego.zegoliveroom.entity.ZegoExtPrepSet;

public class UserDataApp extends Application {
    private String UserToken;
    private String Username;
    private String phone;
    private String yuanzhuoId;
    private String imageurl;
    private ZegoAudioRoom zegoAudioRoom;
    private long appid = 597269392;
    private byte[] sign = {0x6a,0x22,0x73,0x1e,0x7f,
            (byte) 0xfb, (byte) 0xea, (byte) 0xc2,0x2e,0x31,0x4e,
            (byte) 0xfc, (byte) 0xc9, (byte) 0xb7,0x5f, (byte) 0xb7,
            0x3d, (byte) 0xbd, (byte) 0x86,0x37,0x41, (byte) 0xdc, (byte) 0xfd,
            (byte) 0xea,0x5d, (byte) 0xb2,0x6b,0x45, (byte) 0xdb, (byte) 0xb7,0x7f, (byte) 0xb3};

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void initSDK() {
        ZegoAudioRoom.setUser(yuanzhuoId, Username);
        ZegoAudioRoom.setUseTestEnv(true);
        ZegoAudioRoom.setBusinessType(0);
        ZegoAudioRoom.setAudioDeviceMode(ZegoConstants.AudioDeviceMode.General);

        zegoAudioRoom = new ZegoAudioRoom();
        zegoAudioRoom.initWithAppId(appid,sign,this);
        zegoAudioRoom.setLatencyMode(ZegoConstants.LatencyMode.Low3);
        zegoAudioRoom.setUserStateUpdate(true);
        zegoAudioRoom.enableAEC(true);
        zegoAudioRoom.enableAECWhenHeadsetDetected(true);
        zegoAudioRoom.enableAux(false);
        zegoAudioRoom.enableMic(false);
        Log.e("初始化SDK","成功");

    }

    public void reInitZegoSDK() {
        if (zegoAudioRoom != null) {
            zegoAudioRoom.unInit();
        }
        initSDK();
    }

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getYuanzhuoId() {
        return yuanzhuoId;
    }

    public void setYuanzhuoId(String yuanzhuoId) {
        this.yuanzhuoId = yuanzhuoId;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public ZegoAudioRoom getZegoAudioRoom() {
        return zegoAudioRoom;
    }
}
