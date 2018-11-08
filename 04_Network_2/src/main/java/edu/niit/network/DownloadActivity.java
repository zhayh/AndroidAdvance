package edu.niit.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;

public class DownloadActivity extends AppCompatActivity {
    private static final String TAG = "DownloadActivity";
    private static final String IMAGE_URL = "https://www.baidu.com/img/bd_logo1.png";
    private static final String IMAGE_BASE_URL = "https://www.baidu.com/img/";

    @BindView(R.id.image_view)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_volley, R.id.btn_okhttp, R.id.btn_retrofit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_volley:
                volley(IMAGE_URL);
                break;
            case R.id.btn_okhttp:
                okhttp(IMAGE_URL);
                break;
            case R.id.btn_retrofit:
                retrofit("bd_logo1.png");
                break;
        }
    }

    private void retrofit(String url) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(IMAGE_BASE_URL).build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        retrofit2.Call<ResponseBody> call = service.downloadImage(url);

        // Callback在UI线程，所以需要用子线程执行网络数据获取
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, final retrofit2.Response<ResponseBody> response) {
                Log.d(TAG, response.toString());
                if (response.isSuccessful()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                byte[] bytes = response.body().bytes();
                                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        imageView.setImageBitmap(bitmap);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, e.toString());
                            }
                        }
                    }).start();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(DownloadActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void okhttp(String url) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.e(TAG, e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(DownloadActivity.this, "网络请求出错" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                Log.d(TAG, response.toString());
                if (response.isSuccessful()) {
                    try {
                        byte[] bytes = response.body().bytes();
                        final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(DownloadActivity.this, "网络请求出错" + response.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void volley(String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(final Bitmap response) {
                        Log.d(TAG, response.toString());
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(response);
                            }
                        });
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageResource(R.drawable.flower);
                            }
                        });
                    }
                });
        queue.add(request);
    }

    // Volley的ImageLoader下载图片
    private void getImageLoad(String url) {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(url, listener);
    }

//    private void getNetWorkImageView(String url) {
//        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
//        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
//        nvImage.setDefaultImageResId(R.mipmap.ic_launcher);
//        nvImage.setErrorImageResId(R.mipmap.ic_launcher);
//        nvImage.setImageUrl(url, imageLoader);
//        Log.d(TAG, imageLoader.toString());
//    }
}
