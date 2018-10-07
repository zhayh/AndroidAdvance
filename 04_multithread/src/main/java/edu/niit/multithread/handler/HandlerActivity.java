package edu.niit.multithread.handler;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.niit.multithread.R;

public class HandlerActivity extends AppCompatActivity {

    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.tv_message)
    TextView tvMessage;

    private Handler mHandler;
    private ProgressDialog mDialog;
    private Thread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mHandler = new TestHandler(this) ;
    }

    @OnClick(R.id.btn_start)
    public void onClick() {
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int result = 0;
                boolean isCancel = false;
                mHandler.sendEmptyMessage(0);
                for (int i = 0; i < 101; i++) {
                    try {
                        Thread.sleep(100);
                        result += i;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        isCancel = true;
                        break;
                    }
                    if (i % 5 == 0) {
                        Message msg = Message.obtain();
                        msg.what = 1;
                        msg.arg1 = i;
                        mHandler.sendMessage(msg);
                    }
                }
                if(!isCancel) {
                    Message msg = Message.obtain();
                    msg.what = 2;
                    msg.arg1 = result;
                    mHandler.sendMessage(msg);
                }
            }
        });
        mThread.start();
    }

    private void setUpDialog() {
        mDialog = new ProgressDialog(HandlerActivity.this);
        mDialog.setTitle("正在处理");
        mDialog.setCancelable(true);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.setMessage("准备开始后台工作......");
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                mHandler.sendEmptyMessage(3);
                mThread.interrupt();
            }
        });
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
    }

    private static class TestHandler extends Handler {
        private final WeakReference<HandlerActivity> mActivity;

        public TestHandler(HandlerActivity activity) {
            this.mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            HandlerActivity activity = mActivity.get();
            super.handleMessage(msg);
            if(activity == null) {
                return;
            }

            switch (msg.what) {
                case 0:
                    activity.setUpDialog();
                    break;
                case 1:
                    activity.mDialog.setMessage("已完成" + msg.arg1 + "%");
                    activity.mDialog.setProgress(msg.arg1);
                    break;
                case 2:
                    activity.tvMessage.setText("工作已完成，结果为：" + msg.arg1);
                    activity.mDialog.dismiss();
                    break;
                case 3:
                    activity.tvMessage.setText("工作已被取消");
                    activity.mDialog.dismiss();
                    break;
            }
        }
    }
}
