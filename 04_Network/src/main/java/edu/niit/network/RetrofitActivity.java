package edu.niit.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.niit.network.model.Ip;
import edu.niit.network.retrofit.RetrofitService;
import edu.niit.network.utils.HttpsUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {
    private final static String IP_BASEURL = "http://ip.taobao.com/";
    private final static String IP_SERVICE_URL = "http://ip.taobao.com/service/";
    private final static String IMAGE_URL = "https://www.baidu.com/img/";
    private static final String TAG = "RetrofitActivity";

    private final static int MSG_GET = 100;
    private final static int MSG_POST = 101;
    private final static int MSG_UPLOAD = 102;
    private final static int SHOW_IMAGE = 103;


    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.image_view)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_get, R.id.btn_post, R.id.btn_upload, R.id.btn_download})
    public void onViewClicked(View view) {

        switch (view.getId()) {
            case R.id.btn_get:
                httpGet();
                break;
            case R.id.btn_post:
                httpPost();
                break;
            case R.id.btn_upload:
                uploadFile();
                break;
            case R.id.btn_download:
                downloadImage("bd_logo1.png");
                break;
        }
    }

    private void downloadImage(final String fileUrl) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(IMAGE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        final Retrofit retrofit = HttpsUtil.handleSSHHandshakeByRetrofit(IMAGE_URL);
        RetrofitService service = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = service.downloadImg(fileUrl);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        ResponseBody body = response.body();
                        if (body != null) {
                            InputStream is = body.byteStream();
                            // 存储到本地
                            //  writeResponseBodyToDisk("text_img.png", is);

                            // 显示网络图片
                            Message msg = Message.obtain();
                            msg.what = SHOW_IMAGE;
                            msg.obj = BitmapFactory.decodeStream(is);
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.getMessage(), t);
            }
        });
    }

    public void writeResponseBodyToDisk(String imageName, InputStream is) {
        try {
            if (is == null) {
                Toast.makeText(RetrofitActivity.this, "图片源错误", Toast.LENGTH_SHORT).show();
                return;
            }
            String path = this.getFilesDir() + File.separator + "loadingImg";
            File fileDr = new File(path);
            if (!fileDr.exists()) {
                fileDr.mkdir();
            }
            File file = new File(path, imageName);
            if (file.exists()) {
                file.delete();
                file = new File(path, imageName);
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            bis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadFile() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "demo.jpg");

        // 为file建立RequestBody实例
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), file);

        // MultipartBody.Part借助文件名完成上传
        MultipartBody.Part photo = MultipartBody.Part.createFormData("image",
                "photo.jpg", requestFile);

        RequestBody desc = RequestBody.create(null, "face photo");

        // 创建service实例
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ResponseBody> call = retrofitService.uploadFile(desc, photo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("upload", "success");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("upload error", t.getMessage());
            }
        });
    }

    private void httpPost() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_SERVICE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<Ip> call = retrofitService.postIpMsg("59.108.54.37");
//         Call<Ip> call = ipService.postIpMsg(new IpParams("59.108.54.37"));
        call.enqueue(new Callback<Ip>() {
            @Override
            public void onResponse(Call<Ip> call, Response<Ip> response) {
                String country = response.body().getData().getCountry();

                Message msg = Message.obtain();
                msg.what = MSG_POST;
                msg.obj = country;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Call<Ip> call, Throwable t) {

            }
        });
    }

    private void httpGet() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IP_SERVICE_URL)   // 设置网络请求的url地址
                .addConverterFactory(GsonConverterFactory.create()) // 设置数据解析器
                .build();

        // 创建网络请求接口实例
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        // 对发送请求进行封装
        Call<Ip> call = retrofitService.getIpMsg();
//        Call<Ip> call = ipService.getIpMsg("service");
//        Call<Ip> call = ipService.getIpMsg("service", "59.108.54.37");
        // 发送异步网络请求
        call.enqueue(new Callback<Ip>() {
            // 请求成功时回调
            @Override
            public void onResponse(Call<Ip> call, Response<Ip> response) {
                // 处理返回数据
                final String country = response.body().getData().getCountry();
                Message msg = Message.obtain();
                msg.what = MSG_GET;
                msg.obj = country;
                handler.sendMessage(msg);
            }

            // 请求失败时回调
            @Override
            public void onFailure(Call<Ip> call, Throwable t) {

            }
        });
    }

    private RetrofitHandler handler = new RetrofitHandler(this);

    static class RetrofitHandler extends Handler {
        private WeakReference<RetrofitActivity> reference;

        RetrofitHandler(RetrofitActivity activity) {
            this.reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RetrofitActivity activity = reference.get();
            switch (msg.what) {
                case MSG_GET:
                case MSG_POST:
                    activity.scrollView.setVisibility(View.VISIBLE);
                    activity.imageView.setVisibility(View.GONE);
                    activity.textView.setText("请求的ip所在的国家为：" + msg.obj);
                    break;
                case SHOW_IMAGE:
                    activity.scrollView.setVisibility(View.GONE);
                    activity.imageView.setVisibility(View.VISIBLE);
                    activity.imageView.setImageBitmap((Bitmap) msg.obj);
                    break;
            }
        }
    }

}
