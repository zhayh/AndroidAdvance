package edu.niit.network;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.niit.network.model.Ip;
import edu.niit.network.model.Weather;
import edu.niit.network.utils.HttpsUtil;
import edu.niit.network.volley.BitmapCache;

public class VolleyActivity extends AppCompatActivity {
    public static final String TAG = "VolleyActivity";

    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.btn_string)
    Button btnString;
    @BindView(R.id.btn_json)
    Button btnJson;
    @BindView(R.id.btn_image)
    Button btnImage;
    @BindView(R.id.scroll_view)
    ScrollView scrollView;
    @BindView(R.id.nv_image)
    NetworkImageView nvImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);
        ButterKnife.bind(this);

//        HttpsUtil.handleSSLHandshakeByVolley();
    }

    @OnClick({R.id.btn_string, R.id.btn_json, R.id.btn_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_string:
                scrollView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                nvImage.setVisibility(View.GONE);
                post("https://www.baidu.com", null);
                break;
            case R.id.btn_json:
                scrollView.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                nvImage.setVisibility(View.GONE);
                getJson("https://www.apiopen.top/weatherApi?city=南京");
//                getIpJson("http://ip.taobao.com/service/getIpInfo.php?ip=59.108.54.37");
                break;
            case R.id.btn_image:
                scrollView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                nvImage.setVisibility(View.GONE);
                getImage("https://www.baidu.com/img/bd_logo1.png");
                break;
        }
    }

    private void get(String url) {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        textView.setText(response);
                        Log.d(TAG, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText(error.getMessage());
                Log.e(TAG, error.getMessage(), error);
            }
        });
        // 将请求添加到请求队列中
        mQueue.add(request);
    }

    private void post(String url, final Map<String, String> params) {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                        textView.setText(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        textView.setText(error.getMessage());
                        Log.e(TAG, error.getMessage(), error);
                    }
                }) {
            // 重写getParams方法设置参数
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        // 将请求添加到请求队列中
        mQueue.add(request);
    }

    private void getJson(String url) {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        final Weather weather = new Gson().fromJson(response.toString(), Weather.class);
                        if(weather != null) {
                            textView.post(new Runnable() {
                                @Override
                                public void run() {
                                    textView.setText(weather.toString());
                                }
                            });
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(error.getMessage());
                            }
                        });
                        Log.e(TAG, error.getMessage(), error);
                    }
                });
        // 将请求添加到请求队列中
        mQueue.add(request);
    }

    private void getIpJson(String url) {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {
                        Log.d(TAG, response.toString());
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(parseByGson(response.toString()).toString());
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText(error.getMessage());
                            }
                        });
                        Log.e(TAG, error.getMessage(), error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 " +
                        "(KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36");
                return headers;
            }
        };
        // 将请求添加到请求队列中
        mQueue.add(request);
    }

    private void getImage(String url) {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(final Bitmap response) {
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
                        Log.e(TAG, error.getMessage(), error);
                        imageView.post(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageResource(R.drawable.autumn);
                            }
                        });
                    }
                });
        mQueue.add(request);
    }

    private void getImageLoad(String url) {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        imageLoader.get(url, listener);
    }

    private void getNetWorkImageView(String url) {
        RequestQueue mQueue = Volley.newRequestQueue(getApplicationContext());
        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        nvImage.setDefaultImageResId(R.mipmap.ic_launcher);
        nvImage.setErrorImageResId(R.mipmap.ic_launcher);
        nvImage.setImageUrl(url, imageLoader);
        Log.d(TAG, imageLoader.toString());
    }

    private Ip parseByGson(String json) {
        Gson gson = new Gson();

        // json反序列化
        Ip ipObj = gson.fromJson(json, Ip.class);

        // json序列化
        // String jsonStr = gson.toJson(ipObj);

        return ipObj;
    }
}
