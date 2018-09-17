package com.example.a91599.appname.Service;

import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.NewsBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ShowService {
    @GET("api/job/all")
    Call<ApiResult<List<NewsBean>>> getNews(@Query("page") int page);
}
