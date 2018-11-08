package edu.niit.multithread.async;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.niit.multithread.R;

public class DownloadActivity extends AppCompatActivity {
    private static final String DOWNLOAD_URL = "https://desk-fd.zol-img.com.cn/t_s1920x1080c5/g5/M00/07/07/ChMkJlXw8QmIO6kEABYKy-RYbJ4AACddwM0pT0AFgrj303.jpg";

    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.download_image)
    ImageView downloadImage;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_download})
    public void onClicked(View view) {
        new DownloadImage().execute(DOWNLOAD_URL);
    }

    private class DownloadImage extends AsyncTask<String, Bitmap, Bitmap> {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(DownloadActivity.this, "Waiting...", "下载图片");
            dialog.show();
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            return downloadImage(url);
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

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            dialog.hide();

            if (bitmap != null) {
                downloadImage.setImageBitmap(bitmap);
            }
        }
    }
}
