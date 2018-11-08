package edu.niit.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StringActivity extends AppCompatActivity {
    private static final String TAG = "StringActivity";

    private static final String JUHE_KEY = "bea8ec710e8673cdc53f7b6724203f03";
    private static final String JUHE_BASE_URL = "http://apis.juhe.cn/ip/";
    private static final String BAIDU_IP = "https://www.baidu.com";

    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_string);
        ButterKnife.bind(this);
    }


    @OnClick({R.id.btn_volley_get, R.id.btn_okhttp_get, R.id.btn_retrofit_get, R.id.btn_volley_post, R.id.btn_okhttp_post, R.id.btn_retrofit_post})
    public void onViewClicked(View view) {
        // 请求参数
        Map<String, String> params = new HashMap<>();
        params.put("ip", "59.108.54.37");
        params.put("dtype", "json");
        params.put("key", JUHE_KEY);

        String juheUrl = JUHE_BASE_URL + "ip2addr";

        switch (view.getId()) {
            case R.id.btn_volley_get:
                volleyGet(BAIDU_IP);
                break;
            case R.id.btn_volley_post:
                volleyPost(juheUrl, params);
                break;
            case R.id.btn_okhttp_get:
                okhttpGet(BAIDU_IP);
                break;
            case R.id.btn_okhttp_post:
                okhttpPost(juheUrl, params);
                break;
            case R.id.btn_retrofit_get:
                retrofitpGet(JUHE_BASE_URL, params);
                break;
            case R.id.btn_retrofit_post:
                retrofitPost(JUHE_BASE_URL, params);
                break;
        }
    }

    // retrofit的get和post方式的不同在于RetrofitService的定义使用的注解不同，此处的代码没区别
    private void retrofitPost(String url, Map<String, String> params) {
        // 1. 创建retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // 2. 创建网络请求的接口实例
        RetrofitService service = retrofit.create(RetrofitService.class);
        // 3. 封装异步网络请求
        retrofit2.Call<Ip> call = service.postIp(params.get("key"), params.get("dtype"),
                params.get("ip"));
        // 4. 发送异步网络请求
        call.enqueue(new retrofit2.Callback<Ip>() {
            // 5. 处理网络请求的回调
            @Override
            public void onResponse(retrofit2.Call<Ip> call, final retrofit2.Response<Ip> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    tvResult.setText(new Gson().toJson(response.body()));
                } else {
                    Log.d(TAG, response.message());
                    Toast.makeText(StringActivity.this, "网络请求出错" + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Ip> call, final Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(StringActivity.this, "网络请求出错" + t.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void retrofitpGet(String url, Map<String, String> params) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService service = retrofit.create(RetrofitService.class);
        retrofit2.Call<Ip> call = service.getIp(params.get("key"), params.get("dtype"),
                params.get("ip"));
        call.enqueue(new retrofit2.Callback<Ip>() {
            @Override
            public void onResponse(retrofit2.Call<Ip> call, final retrofit2.Response<Ip> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, response.toString());
                    tvResult.setText(new Gson().toJson(response.body()));
                } else {
                    Log.d(TAG, response.message());
                    Toast.makeText(StringActivity.this, "网络请求出错" + response.message(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Ip> call, final Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(StringActivity.this, "网络请求出错" + t.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    // post方式发送请求参数
    private void okhttpPost(String url, Map<String, String> params) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        // 加载请求参数
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            builder.add(key, params.get(key));
        }
        okhttp3.RequestBody formBody = builder.build();
        okhttp3.Request request = new okhttp3.Request.Builder().url(url)
                .post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.e(TAG, e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StringActivity.this, "网络请求出错" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String resp = response.body().string();
                    Log.d(TAG, resp);
                    tvResult.post(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText(resp);
                        }
                    });
                } else {
                    Log.d(TAG, response.message());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StringActivity.this, "网络请求出错" + response.message(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    // get方式
    private void okhttpGet(String url) {
        // 1. 创建请求对象
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();
        // 2. 构建请求数据
        okhttp3.Request request = new okhttp3.Request.Builder().url(url).build();
        // 3. 发出请求
        client.newCall(request).enqueue(new Callback() {
            // 4. 处理请求响应的回调
            @Override
            public void onFailure(Call call, final IOException e) {
                Log.e(TAG, e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(StringActivity.this, "网络请求出错" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String result = response.body().string();
                    Log.d(TAG, result);
                    tvResult.post(new Runnable() {
                        @Override
                        public void run() {
                            tvResult.setText(result);
                        }
                    });
                } else {
                    Log.d(TAG, response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StringActivity.this, "网络请求出错" + response.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void volleyPost(String url, final Map<String, String> params) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d(TAG, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResult.setText(response);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StringActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        queue.add(request);
    }

    private void volleyGet(String url) {
        // 1. 创建请求队列
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        // 2. 创建String请求对象
        StringRequest request = new StringRequest(Request.Method.GET, url,
                // 4. 请求成功的监听，处理请求成功响应的回调
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.d(TAG, response);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvResult.setText(response);
                            }
                        });
                    }
                },
                // 4. 请求失败的监听，处理请求成功响应的回调
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StringActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        // 3. 将请求加入请求队列
        queue.add(request);
    }
}
