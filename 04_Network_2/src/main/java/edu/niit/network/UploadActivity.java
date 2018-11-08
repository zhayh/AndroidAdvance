package edu.niit.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UploadActivity extends AppCompatActivity {
    private static final String TAG = "UploadActivity";

    private static final MediaType MEDIA_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_okhttp_file, R.id.btn_okhttp_form, R.id.btn_retrofit})
    public void onViewClicked(View view) {
        // 内部存储的的路径，将readme.md文件上传到模拟器的目录
        // /storage/emulated/0/Android/data/edu.niit.network/files/readme.md
        String path = getExternalFilesDir("").getAbsolutePath();
        String fileName = path + File.separator + "readme.md";

        switch (view.getId()) {
            case R.id.btn_okhttp_file:
                okhttpUpload("https://api.github.com/markdown/raw", fileName);
                break;
            case R.id.btn_okhttp_form:
                okhttpUploadForm("https://api.github.com/markdown/raw", fileName);
                break;
            case R.id.btn_retrofit:
                retrofitUpload("https://api.github.com/markdown/", fileName);
                break;
        }
    }

    private void retrofitUpload(String url, String fileName) {
        // 创建RequestBody实例
        RequestBody reqFile = RequestBody.create(MEDIA_MARKDOWN, new File(fileName));
        MultipartBody.Part partFile = MultipartBody.Part.create(reqFile);
        RequestBody name = RequestBody.create(null, "niit");

        // 创建
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        retrofit2.Call<ResponseBody> call = service.uploadFormFile(name, partFile);
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e(TAG, response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void okhttpUploadForm(String url, String fileName) {
        // 创建表单数据
        RequestBody fileBody = RequestBody.create(MEDIA_MARKDOWN, new File(fileName));
        MultipartBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", "niit")
                .addFormDataPart("file", fileName, fileBody)
                .build();

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder().url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e(TAG, response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void okhttpUpload(String url, String fileName) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder().url(url)
                .post(RequestBody.create(MEDIA_MARKDOWN, new File(fileName)))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(UploadActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Log.e(TAG, response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
