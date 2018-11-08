package edu.niit.network;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_string, R.id.btn_json, R.id.btn_image, R.id.btn_upload_form})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.btn_string:
                intent.setClass(this, StringActivity.class);
                break;
            case R.id.btn_json:
                intent.setClass(this, JsonActivity.class);
                break;
            case R.id.btn_image:
                intent.setClass(this, DownloadActivity.class);
                break;
            case R.id.btn_upload_form:
                intent.setClass(this, UploadActivity.class);
                break;
        }
        startActivity(intent);
    }
}
