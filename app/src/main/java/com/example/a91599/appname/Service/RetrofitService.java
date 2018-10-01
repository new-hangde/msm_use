package com.example.a91599.appname.Service;


import com.example.a91599.appname.Bean.ApiResult;
import com.example.a91599.appname.Bean.Collect;
import com.example.a91599.appname.Bean.NewsBean;
import com.example.a91599.appname.Bean.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("api/user/login")
    Call<ApiResult<User>> login(
            @Query("account") String account,
            @Query("password") String password);

    @GET("api/user/register")
    Call<ApiResult> register(@Query("username")  String username,
                              @Query("password")  String password);

    @GET("api/job/search")
    Call<ApiResult<List<NewsBean>>> getResult(
            @Query("page") int page,
            @Query("keyword") String keyword);

    @GET("api/job/all")
    Call<ApiResult<List<NewsBean>>> getNews(@Query("page") int page);

    @Multipart
    @POST("api/job/add")
    Call<ApiResult>  upload(@PartMap Map<String, RequestBody> map,
                            @Part MultipartBody.Part image);

    @GET("/api/job/addFavorite")
    Call<ApiResult> collect(@Query("token") String token,
                   @Query("job_id")int job_id);

    @GET("/api/job/isFavorite")
    Call<ApiResult> isCollected(@Query("token") String token,
                            @Query("job_id")int job_id);

    @GET("/api/job/removeFavorite")
    Call<ApiResult> cancelCollect(@Query("token") String token,
                            @Query("job_id")int job_id);

    @GET("/api/job/favorite")
    Call<ApiResult<List<Collect<NewsBean>>>> getCollected(@Query("token") String token);

}
