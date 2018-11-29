package edu.niit.multimedia;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CameraActivity extends AppCompatActivity {

    @BindView(R.id.img_show)
    ImageView imgShow;

    private static final int PERMISSIONS_REQUEST = 100;
    private static final int REQUEST_CODE_CAMERA = 100;

    private File imageFile;
    private Uri fileUri;
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissions = new ArrayList<>();

    private String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        applyPermission();
    }

    private void applyPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (checkSelfPermission(Manifest.permission.CAMERA) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 2);
            }

            for(String permission : permissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    mPermissions.add(permission);
                }
            }
            if (mPermissions.isEmpty()) {//未授予的权限为空，表示都授予了
                Toast.makeText(this, "已经授权", Toast.LENGTH_LONG).show();
            } else {//请求权限方法
                String[] checked = mPermissions.toArray(new String[mPermissions.size()]);
                ActivityCompat.requestPermissions(this, checked, PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[i]);
                    if (showRequestPermission) {
                        Toast.makeText(this, "权限未申请", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        takePhoto();
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (imageFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // Android 7.0以上通过FileProvider将File转化为Uri
                    fileUri = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider", imageFile);
                } else {
                    // Android 7.0以下则直接使用Uri的fromFile方法将File转化为Uri
                    fileUri = Uri.fromFile(imageFile);
                }
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);//私有目录读写权限
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        } else {
            Toast.makeText(CameraActivity.this, "不能获取摄像头", Toast.LENGTH_SHORT).show();
        }
    }

    private void createImageFile() throws IOException {
        String imageFileName = "image" + System.currentTimeMillis();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_CAMERA) {
            if(resultCode == RESULT_OK) {
                if (fileUri != null) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(
                                getContentResolver().openInputStream(fileUri));
                        if (bitmap != null) {
                            imgShow.setImageBitmap(bitmap);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
//                    imgShow.setImageURI(fileUri);
                }
            } else if(resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "取消拍照", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
