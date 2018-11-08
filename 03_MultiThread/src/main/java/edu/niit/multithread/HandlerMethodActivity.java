package edu.niit.multithread;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HandlerMethodActivity extends AppCompatActivity {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.button1)
    Button button1;
    @BindView(R.id.button2)
    Button button2;
    @BindView(R.id.button3)
    Button button3;
    @BindView(R.id.button4)
    Button button4;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_stop)
    Button btnStop;

    private Handler handler = new Handler();
    private Runnable updateThread = new Runnable() {
        @Override
        public void run() {
            tvContent.append("\n update thread");
            handler.postDelayed(updateThread, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_method);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.btn_start, R.id.btn_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button1:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        button1.setText("runOnUiThread方式更新");
                        tvContent.setText("runOnUiThread方式更新TextView的内容");
                    }
                });
                break;
            case R.id.button2:
                button2.post(new Runnable() {
                    @Override
                    public void run() {
                        button2.setText("View.post方式更新");
                        tvContent.setText("View.post方式更新TextView的内容");
                    }
                });
                break;
            case R.id.button3:
                button3.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        button3.setText("View.postDelayed方式延时3秒更新");
                        tvContent.setText("View.postDelayed方式延时3秒更新TextView的内容");
                    }
                }, 3000);
                break;
            case R.id.button4:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        button4.setText("handler.post方式更新");
                        tvContent.setText("handler.post方式更新TextView的内容");
                    }
                });
                break;
            case R.id.btn_start:
                handler.post(updateThread);
                break;
            case R.id.btn_stop:
                handler.removeCallbacks(updateThread);
        }
    }
}
