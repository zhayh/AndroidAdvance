package edu.niit.network;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.niit.network.model.Ip;
import edu.niit.network.model.IpData;
import edu.niit.network.utils.UrlConnManager;

public class MainActivity extends AppCompatActivity {
    public static final int IP_RESPONSE = 1;
    private static final String TAG = "MainActivity";

    @BindView(R.id.text_view)
    TextView textView;
    @BindView(R.id.button)
    Button button;

    private Handler mHandler;
    private  Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mHandler = new MyHandler(this);
        params = new HashMap<>();
        params.put("ip", "59.108.54.37");

        Log.d(TAG, "onCreate");
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = httpPost("http://ip.taobao.com/service/getIpInfo.php", params);
                Message msg = Message.obtain();
                msg.what = IP_RESPONSE;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        }).start();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                final String result = httpPost("http://ip.taobao.com/service/getIpInfo.php", params);
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MainActivity.this.textView.setText(result);
//                    }
//                });
//            }
//        }).start();
    }

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> reference;

        MyHandler(MainActivity activity) {
            this.reference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            MainActivity activity = reference.get();
            if (activity == null) {
                return;
            }
            if (msg.what == IP_RESPONSE) {
                activity.textView.setText("接收到的网络数据为：" + msg.obj);
            }
        }
    }

    private String httpPost(String urlStr, Map<String, String> params) {
        String result = null;
        List<NameValuePair> pairs = new ArrayList<>();

        try {
            for(String key : params.keySet()) {
                pairs.add(new BasicNameValuePair(key, params.get(key)));
            }

            HttpURLConnection connection = UrlConnManager.getConnection(urlStr, "POST");
            UrlConnManager.postParams(connection.getOutputStream(), pairs);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = convertStreamToString(connection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private String httpGet(String urlStr, Map<String, String> params) {
        String result = null;
        StringBuilder builder = new StringBuilder();
        try {
            for(String key : params.keySet()) {
                builder.append(key).append("=").append(params.get(key)).append("&");
            }
            urlStr += "?" + builder.substring(0, builder.length() - 1);
            HttpURLConnection connection = UrlConnManager.getConnection(urlStr, "GET");

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = convertStreamToString(connection.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @NonNull
    private String convertStreamToString(InputStream input) throws IOException {
        String line;
        StringBuilder response = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while ((line = reader.readLine()) != null) {
            response.append(line).append("\n");
        }
        reader.close();
        return response.toString();
    }

}
