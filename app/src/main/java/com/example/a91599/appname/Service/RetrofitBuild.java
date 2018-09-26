package com.example.a91599.appname.Service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitBuild {
    public RetrofitService service(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://job.zhuyefeng.com/")  //要访问的主机地址，注意以 /（斜线） 结束，不然可能会抛出异常
                .addConverterFactory(GsonConverterFactory.create()) //添加Gson
                .client(RetrofitUtils.getInstance().addTimeOut(30).addHttpLog().build())
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        return service;
    }
}
