package com.example.a91599.appname.Service;


import com.example.a91599.appname.Bean.ApiResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RegisterService {
    @GET("api/user/register")
    Call<ApiResult> getResult(@Query("username")  String username,
                              @Query("password")  String password);
}
