package com.fht.yuanzhuo.Userclass;

public class History {
    private String roomName;
    private String time;
    private String fileUrl;

    public History(String roomName,String time,String fileUrl){
        this.roomName = roomName;
        this.time = time;
        this.fileUrl = fileUrl;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getTime() {
        return time;
    }

    public String getFileUrl() {
        return fileUrl;
    }
}
