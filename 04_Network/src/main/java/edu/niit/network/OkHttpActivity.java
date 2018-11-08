package edu.niit.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.niit.network.model.Ip;
import edu.niit.network.model.IpData;
import edu.niit.network.okhttp.OkHttpEngine;
import edu.niit.network.okhttp.ResultCallback;
import edu.niit.network.utils.HttpsUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpActivity extends AppCompatActivity {
    private static final String TAG = "OkHttpActivity";

    // 指定MIME类型
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.image_view)
    ImageView imageView;

    private OkHttpClient mClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        ButterKnife.bind(this);

        mClient = HttpsUtil.handleSSLHandshakeByOkHttp();
//        mClient = new OkHttpClient.Builder()
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .readTimeout(15, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
//                .build();
    }

    @OnClick({R.id.btn_get, R.id.btn_post, R.id.btn_upload, R.id.btn_download})
    public void onViewClicked(View view) {
        imageView.setVisibility(View.GONE);
        scrollView.setVisibility(View.VISIBLE);
        textView.setText("");

        String path = getFilesDir().getAbsolutePath();
        switch (view.getId()) {
            case R.id.btn_get:
                getAsyncHttp("http://ip.taobao.com/service/getIpInfo.php?ip=59.108.54.37");
//                getAsyncHttp("https://www.baidu.com");
//                getAsyncHttp("https://www.apiopen.top/weatherApi?city=南京");
//                getAsyncHttpByEngine("https://www.apiopen.top/weatherApi?city=南京");
                break;
            case R.id.btn_post:
                postForm("https://www.apiopen.top/weatherApi");
//                postJson("https://www.apiopen.top/weatherApi", "\"city\",\"南京\"");
//                postString("https://api.github.com/markdown/raw");
                break;
            case R.id.btn_upload:
//                postFile("https://api.github.com/markdown/raw", path + "/readme.md");
                postMultipart("https://api.imgur.com/3/image", path + "/demo.jpg");
                break;
            case R.id.btn_download:
                imageView.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);
                downloadFile("https://www.baidu.com/img/bd_logo1.png", path);
                break;
        }
    }

    private void getSyncHttp(String url) {
        // 构建请求对象Request
        Request request = new Request.Builder().url(url).build();
        // 发出请求
        try (Response response = mClient.newCall(request).execute()) {
            //如果请求不成功
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            //从HTTP响应的body中取出响应头和响应正文 return response.body().string();
            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                Log.d(TAG, responseHeaders.name(i) + ": " + responseHeaders.value(i));
            }
            String str = response.body().string();
            Log.d(TAG, str);
            textView.setText(str);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    private void getAsyncHttp(String url) {
        Request request = new Request.Builder().url(url)
                .header("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36")
                .addHeader("Accept", "application/json")
                .method("GET", null)
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String server = response.header("Server");
                    if (server != null) {
                        Log.d(TAG, server);
                    }
                    // 提取Http body数据
                    final String str = response.body().string();
                    Log.d(TAG, str);

                    Ip ip = new Gson().fromJson(str, Ip.class);
                    final IpData data = ip != null ? ip.getData() : null;
                    if (data != null) {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(data.getIp() + ", country: " + data.getCountry());
                                textView.setText(str);
                            }
                        });
                    }
                } else {
                    textView.setText(response.body().string());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // 出现了未捕获的异常
                e.printStackTrace();
                Log.e(TAG, e.getMessage(), e);
            }
        });
    }

    private void postForm(String url) {
        RequestBody formBody = new FormBody.Builder().add("city", "南京").build();
        Request request = new Request.Builder().url(url).post(formBody).build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                Log.d(TAG, str);
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(str);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    // 上传String，不建议发送超过1M的文本信息
    private void postString(String url) {
        String postBody = "Releases\n"
                + "\n"
                + " * _1.0_ May 6, 2018\n"
                + " * _1.1_ June 15, 2018\n"
                + " * _1.2_ August 11, 2018\n";

        Request request = new Request.Builder().url(url)
                .post(RequestBody.create(MARKDOWN, postBody))
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String str = response.body().string();
                    Log.d(TAG, str);
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(str);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    private void postJson(String url, String json) {
        // 构建请求body
        RequestBody body = RequestBody.create(JSON, json);
        // 构建请求数据
        final Request request = new Request.Builder().url(url).post(body).build();
        // 发出请求
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String str = response.body().string();
                    Log.d(TAG, str);
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(str);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

    // 上传文件
    public void postFile(String url, String fileName) {
        File file = new File(fileName);
        Request request = new Request.Builder().url(url)
                .post(RequestBody.create(MARKDOWN, file))
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String str = response.body().string();
                    Log.d(TAG, str);
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(str);
                        }
                    });
                } else {
                    Log.d(TAG, response.message());
                }
            }
        });
    }

    // 异步上传Multipart文件
    private static final MediaType JPG = MediaType.parse("image/jpeg");

    private void postMultipart(String url, String fileName) {
        RequestBody fileBody = RequestBody.create(JPG, fileName);
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "头像")
                .addFormDataPart("file", fileName, fileBody)
                .build();
        Request request = new Request.Builder().url(url).post(body).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, response.body().string());
            }
        });
    }

    private void downloadFile(String url, final String savePath) {
        final Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "response success");

                            InputStream input = response.body().byteStream();
                            final Bitmap bitmap = BitmapFactory.decodeStream(input);
//                            imageView.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    imageView.setImageBitmap(bitmap);
//                                }
//                            });
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });

//                            Message msg = Message.obtain();
//                            msg.what = 1;
//                            msg.obj = bitmap;
//                            handler.sendMessage(msg);
                } else {
                    Log.d(TAG, response.message());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, e.getMessage());
            }
        });
    }

//    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    imageView.setImageBitmap((Bitmap) msg.obj);
//                    break;
//            }
//        }
//    };

    private void saveFile(Response response, String savePath) {
        try {
            savePath += "/baidu.png";
            byte[] bytes = response.body().bytes();
            FileOutputStream output = new FileOutputStream(new File(savePath));
            output.write(bytes);
            output.flush();
            output.close();
            Log.d(TAG, "文件下载成功");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    private void getAsyncHttpByEngine(String url) {
        OkHttpEngine.getInstance(OkHttpActivity.this).getAsyncHttp(url, new ResultCallback() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                textView.setText(str);
            }
        });
    }
}
