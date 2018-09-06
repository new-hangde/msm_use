package com.example.a91599.appname.Service;

import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.NewsBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by 91599 on 2018/9/5.
 */

public interface SearchService {
    @GET("api/job/search")
    Call<ApiResult<List<NewsBean>>> getResult(@Query("keyword") String keyword);

}
