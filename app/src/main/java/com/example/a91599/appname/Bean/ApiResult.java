package com.example.a91599.appname.Bean;

import android.os.Message;

import java.util.List;

 public class ApiResult<T> {
    private int code;
    private String msg;
    private String time;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg != null ? msg : "";
    }

    public void setMsg(String  msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccessful(){
        return code == 1;
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", msg=" + msg +
                ", time='" + time + '\'' +
                ", data=" + data +
                '}';
    }
}
