package com.example.a91599.appname;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by 91599 on 2018/9/3.
 */

public interface GitHubService {
    @GET("api/job/all")
    Call<ApiResult<List<NewsBean>>> getNew();
}
