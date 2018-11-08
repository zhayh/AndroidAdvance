package edu.niit.multithread.handler;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.niit.multithread.R;

public class DownloadActivity extends AppCompatActivity {
    private static final String IMAGE_URL = "https://desk-fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/07/07/ChMkJlXw8QmIO6kEABYKy-RYbJ4AACddwM0pT0AFgrj303.jpg";
    private static final int START_DOWNLOAD = 0x100;
    private static final int FINISH_DOWNLOAD = 0x101;
    private static final int FAILURE_DOWNLOAD = 0x102;

    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.download_image)
    ImageView downloadImage;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private DownloadHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);

        handler = new DownloadHandler(this);
    }

    @OnClick({R.id.btn_download})
    public void onClicked(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(START_DOWNLOAD);
                Bitmap bitmap = downloadImage(IMAGE_URL);
                if(bitmap != null) {
                    Message msg = Message.obtain();
                    msg.what = FINISH_DOWNLOAD;
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } else {
                    handler.sendEmptyMessage(FAILURE_DOWNLOAD);
                }
            }
        }).start();
    }

    private static class DownloadHandler extends Handler {
        private ProgressDialog dialog;
        private WeakReference<DownloadActivity> reference;
        DownloadHandler(DownloadActivity activity) {
            this.reference = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            DownloadActivity activity = reference.get();
            if(activity == null) {
                removeCallbacksAndMessages(null);
                return ;
            }
            switch (msg.what) {
                case START_DOWNLOAD:
                    dialog = ProgressDialog.show(activity, "Waiting...", "下载图片");
                    dialog.show();
                    break;
                case FINISH_DOWNLOAD:
                    dialog.dismiss();
                    activity.downloadImage.setImageBitmap((Bitmap) msg.obj);
                    break;
                case FAILURE_DOWNLOAD:
                    dialog.dismiss();
                    Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    private Bitmap downloadImage(String strUrl) {
        InputStream stream = null;
        Bitmap bitmap = null;

        try {
            URL url = new URL(strUrl);
            stream = url.openConnection().getInputStream();
            bitmap = BitmapFactory.decodeStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
