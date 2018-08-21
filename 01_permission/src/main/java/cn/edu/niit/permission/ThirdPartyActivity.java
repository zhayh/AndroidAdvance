package cn.edu.niit.permission;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ThirdPartyActivity extends AppCompatActivity {

    @BindView(R.id.bt_call)
    Button btCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party);
        ButterKnife.bind(this);
    }

    @NeedsPermission({Manifest.permission.CALL_PHONE})
    void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "10086");
        intent.setData(data);
        try {
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ThirdPartyActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CALL_PHONE})
    void showWhy(final PermissionRequest request) {
        new AlertDialog.Builder(this).setMessage("提示用户为何要开启此权限")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed(); // 再次请求
                    }
                }).show();
    }

    @OnPermissionDenied({Manifest.permission.CALL_PHONE})
    void showDenied() {
        Toast.makeText(this, "用户选择拒绝时提示", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain({Manifest.permission.CALL_PHONE})
    void showNotAsk() {
        new AlertDialog.Builder(this).setMessage("该功能需要访问电话权限，不开启无法正常工作!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    @OnClick(R.id.bt_call)
    public void onClick() {
        ThirdPartyActivityPermissionsDispatcher.callWithPermissionCheck(ThirdPartyActivity.this);
    }
}
