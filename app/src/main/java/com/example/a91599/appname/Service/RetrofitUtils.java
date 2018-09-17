package com.example.a91599.appname.Service;

import android.util.Log;

import com.example.a91599.appname.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class RetrofitUtils {
    private static RetrofitUtils mOkHttpUtils;
    //解决优化查询超时问题 默认10s
    private static final int DEFAULT_TIMEOUT = 30; //此处默认超时时间为30s
    private TimeUnit mTimeUnitSECONDS = TimeUnit.SECONDS;
    private OkHttpClient.Builder build = new OkHttpClient.Builder();

    private RetrofitUtils() {
    }

    public static RetrofitUtils getInstance() {
        if (mOkHttpUtils == null) {
            mOkHttpUtils = new RetrofitUtils();
        }
        return mOkHttpUtils;
    }

    public OkHttpClient build() {
        return build.build();
    }

    /***
     * 添加超时时间
     *
     * @param timeOut 超时时间  单位秒
     */
    public RetrofitUtils addTimeOut(int timeOut) {
        if (timeOut <= 0) timeOut = DEFAULT_TIMEOUT;
        build.connectTimeout(timeOut, mTimeUnitSECONDS)
                .writeTimeout(timeOut, mTimeUnitSECONDS)
                .readTimeout(timeOut, mTimeUnitSECONDS);
        return this;
    }

    /***
     * 添加http请求log  包括请求url 请求参数  返回的参数 等信息。
     *
     */
    public RetrofitUtils addHttpLog() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (BuildConfig.DEBUG) {
                    Log.i("httpLog", "message:" + message);
                }
            }
        });
        loggingInterceptor.setLevel(level);
        //OkHttp进行添加拦截器loggingInterceptor
        build.addInterceptor(loggingInterceptor);
        return this;
    }

}

