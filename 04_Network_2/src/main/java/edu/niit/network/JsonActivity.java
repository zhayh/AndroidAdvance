package edu.niit.network;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

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
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JsonActivity extends AppCompatActivity {
    private static final String TAG = "JsonActivity";

    private static final String JUHE_KEY = "bea8ec710e8673cdc53f7b6724203f03";
    private static final String JUHE_BASE_URL = "http://apis.juhe.cn/ip/";

    // 指定MIME类型
    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_volley, R.id.btn_okhttp, R.id.btn_retrofit})
    public void onViewClicked(View view) {
        Map<String, String> params = new HashMap<>();
        params.put("ip", "59.108.54.37");
        params.put("dtype", "json");
        params.put("key", JUHE_KEY);

        String juheUrl = JUHE_BASE_URL + "ip2addr";

        switch (view.getId()) {
            case R.id.btn_volley:
                getVolleyJson(juheUrl, params);
                break;
            case R.id.btn_okhttp:
                getOkHttpJson(juheUrl, params);
                break;
            case R.id.btn_retrofit:
                getRetrofitJson(JUHE_BASE_URL, params);
                break;
        }
    }

    private void getRetrofitJson(String url, Map<String, String> params) {
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
                    tvResult.setText(response.body().toString());
                } else {
                    Log.d(TAG, response.message());
                    Toast.makeText(JsonActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Ip> call, final Throwable t) {
                Log.e(TAG, t.toString());
                Toast.makeText(JsonActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getOkHttpJson(String url, Map<String, String> params) {
        // 1. 构建form表单的请求字符串
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            builder.add(key, params.get(key));
        }
        // 2. 创建请求对象
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        // 3. 构建请求body
        okhttp3.RequestBody formBody = builder.build();
        // 4. 构建请求数据
        final okhttp3.Request request = new okhttp3.Request.Builder().url(url).post(formBody).build();
        // 5. 发出请求
        client.newCall(request).enqueue(new Callback() {
            // 5. 处理请求响应的回调
            @Override
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String str = response.body().string();

                    Log.d(TAG, str);
                    tvResult.post(new Runnable() {
                        @Override
                        public void run() {
                            // 使用Gson将返回字符串转为Ip对象
                            Ip ip = new Gson().fromJson(str, Ip.class);
                            tvResult.setText(ip.toString());
                        }
                    });
                } else {
                    Log.d(TAG, response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(JsonActivity.this, "网络请求出错" + response.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, final IOException e) {
                Log.e(TAG, e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(JsonActivity.this, "网络请求出错" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getVolleyJson(String url, Map<String, String> params) {
        // 1. 组装get方式的查询字符串
        // http://apis.juhe.cn/ip/ip2addr?key=...&dtype=...&ip=...
        StringBuilder strBuilder = new StringBuilder("?");
        Set<String> keys = params.keySet();
        for (String key : keys) {
            strBuilder.append(key).append("=").append(params.get(key)).append("&");
        }
        url += strBuilder.substring(0, strBuilder.length() - 1);
        // 2. 创建请求队列
        RequestQueue queue = Volley.newRequestQueue(this);
        // 3. 创建json请求对象
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    // 5. 请求成功的监听，处理请求成功响应的回调
                    @Override
                    public void onResponse(final JSONObject response) {
                        if (response != null) {
                            Log.d(TAG, response.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Ip ip = new Gson().fromJson(response.toString(), Ip.class);
                                    tvResult.setText(ip.toString());
                                }
                            });
                        } else {
                            Log.d(TAG, "response为null");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(JsonActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                },
                // 5. 请求失败的监听，处理请求失败响应的回调
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(JsonActivity.this, "网络请求出错", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
        // 4. 将请求加入请求队列
        queue.add(request);
    }
}
