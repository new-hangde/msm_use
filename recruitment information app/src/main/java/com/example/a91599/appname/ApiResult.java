package com.example.a91599.appname;

import android.os.Message;

import java.util.List;

/**
 * Created by 91599 on 2018/9/3.
 */

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
        return msg;
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
