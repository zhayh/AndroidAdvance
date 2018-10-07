package edu.niit.multithread.handler;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.niit.multithread.R;

public class ImgLoaderActivity extends AppCompatActivity {
    @BindView(R.id.id_gridview)
    GridView mGridview;

    private ProgressDialog mDialog;
    private ImageView mImageView;

    private int mPicsSize;
    private File mImgDir;

    private List<String> mImgs;
    private ListAdapter mAdapter;

    private HashSet<String> mDirPaths = new HashSet<>();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mDialog.dismiss();
            mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    if (name.endsWith(".jpg")) {
                        return true;
                    }
                    return false;
                }
            }));

            mAdapter = new MyAdapter(getApplicationContext(), mImgs, mImgDir.getAbsolutePath());
            mGridview.setAdapter(mAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_loader);
        ButterKnife.bind(this);

        getImages();
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }
        // 显示进度条
        mDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver contentResolver = ImgLoaderActivity.this.getContentResolver();

                // 只查询jpeg和png图片
                Cursor cursor = contentResolver.query(imgUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or " +
                                MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                while (cursor.moveToNext()) {
                    // 获取图片的路径
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));


                }
            }
        }).start();

    }
}
