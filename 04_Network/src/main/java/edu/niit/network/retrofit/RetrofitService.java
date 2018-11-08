package edu.niit.network.retrofit;

import edu.niit.network.model.Ip;
import edu.niit.network.model.IpParams;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface RetrofitService {
    /**
     * @return 接收数据的类型
     * @Headers 添加请求头
     * @GET 采用get方式发送网络请求
     */
    @Headers("user-agent:Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36")
    @GET("getIpInfo.php?ip=59.108.54.37")
    Call<Ip> getIpMsg();

    @Headers("user-agent:Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36")
    @GET("{path}/getIpInfo.php?ip=59.108.54.37")
    Call<Ip> getIpMsg(@Path("path") String path);

    @Headers("user-agent:Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36")
    @GET("{path}/getIpInfo.php")
    Call<Ip> getIpMsg(@Path("path") String path, @Query("ip") String ip);

    @FormUrlEncoded
    @Headers("user-agent:Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36")
    @POST("getIpInfo.php")
    Call<Ip> postIpMsg(@Field("ip") String ip);

    @POST("getIpInfo.php")
    @Headers("user-agent:Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36")
    Call<Ip> postIpMsg(@Body IpParams ip);

    // 上传单个文件
    @POST("/form")
    @Multipart
    Call<ResponseBody> uploadFile(@Part("desc") RequestBody desc,
                                  @Part MultipartBody.Part file);

   // 上传多个文件
    @POST("/form")
    @Multipart
    Call<ResponseBody> uploadFile(@Part("description") RequestBody desc,
                                  @Part MultipartBody.Part file1,
                                  @Part MultipartBody.Part file2);

    // 下载图片
    @Streaming
    @GET
    Call<ResponseBody> downloadImg(@Url String url);
}
