package com.example.a91599.appname.Service;


import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LoginService {
    @GET("api/user/login")
    Call<ApiResult<List<User>>> getResult(
            @Query("account") String account,
            @Query("password") String password);
}
