package edu.niit.network.utils;

import android.text.TextUtils;

import org.apache.http.NameValuePair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class UrlConnManager {
    public static HttpURLConnection getConnection(String urlStr, String method) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接超时为15s
            connection.setConnectTimeout(15000);
            // 设置读取超市为15s
            connection.setReadTimeout(15000);
            // 设置请求方式，可以为GET或POST
            connection.setRequestMethod(method);
            // 设置Http Header
            connection.setRequestProperty("Connection", "Keep-Alive");
            // 设置模拟浏览器访问
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; " +
                    "x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
            // 设置是否从HttpURLConnection读入，默认为true
            connection.setDoInput(true);
            // 设置是否向HttpURLConnection输出，默认为false，对于POST请求需设为true
            if("POST".equals(method)) {
                connection.setDoOutput(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void postParams(OutputStream output, List<NameValuePair> params)
            throws IOException {
        StringBuilder mStringBuilder = new StringBuilder();
        for (NameValuePair pair : params) {
            if (!TextUtils.isEmpty(mStringBuilder)) {
                mStringBuilder.append("&");
            }
            mStringBuilder.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            mStringBuilder.append("=");
            mStringBuilder.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, "UTF-8"));
        writer.write(mStringBuilder.toString());
        writer.flush();
        writer.close();
    }
}
