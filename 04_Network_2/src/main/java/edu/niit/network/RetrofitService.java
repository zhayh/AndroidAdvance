package edu.niit.network;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RetrofitService {
    @GET("ip2addr")
    Call<Ip> getIp(@Query("key") String key, @Query("dtype") String dtype, @Query("ip") String ip);

    @FormUrlEncoded
    @POST("ip2addr")
    Call<Ip> postIp(@Field("key") String key, @Field("dtype") String dtype, @Field("ip") String ip);

    // 下载图片
    @GET
    @Streaming
    Call<ResponseBody> downloadImage(@Url String url);

    // 上传带文件的表单
    @Multipart
    @POST("raw")
    Call<ResponseBody> uploadFormFile(@Part("name") RequestBody name,
                                      @Part MultipartBody.Part file);
}
