package edu.niit.network.okhttp;

import android.content.Context;
import android.os.Handler;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpEngine {
    private static OkHttpEngine instance;
    private OkHttpClient httpClient;
    private Handler handler;

    public static OkHttpEngine getInstance(Context context) {
        if (instance == null) {
            synchronized (OkHttpEngine.class) {
                if (instance == null) {
                    instance = new OkHttpEngine(context);
                }
            }
        }
        return instance;
    }

    private OkHttpEngine(Context context) {
        File sdCache = context.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(sdCache.getAbsoluteFile(), cacheSize));
        httpClient = builder.build();
        handler = new Handler();
    }

    public void getAsyncHttp(String url, ResultCallback callback) {
        Request request = new Request.Builder().url(url).build();
        Call call = httpClient.newCall(request);
        dealResult(call, callback);
    }

    private void dealResult(final Call call, final ResultCallback callback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedCallback(call.request(), e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                sendSuccessCallback(response, callback);
            }

            private void sendSuccessCallback(final Response response, final ResultCallback callback) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            try {
                                callback.onResponse(response);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }

            private void sendFailedCallback(final Request request, final IOException e, final ResultCallback callback) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onError(request, e);
                        }
                    }
                });
            }
        });
    }
}
