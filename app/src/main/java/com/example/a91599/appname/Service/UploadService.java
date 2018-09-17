package com.example.a91599.appname.Service;

import com.example.a91599.appname.Bean.ApiResult;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by 91599 on 2018/9/16.
 */

public interface UploadService {
    @Multipart
    @POST("api/job/add")
    Call<ApiResult>  upload(@PartMap Map<String, RequestBody> map,
                            @Part MultipartBody.Part image);
}
