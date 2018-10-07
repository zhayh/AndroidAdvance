package edu.niit.multithread;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.tv_message)
    TextView tvMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_start)
    public void onClick() {
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute(100);
    }

    private class MyAsyncTask extends AsyncTask<Integer, Integer, Integer> {
        private ProgressDialog dialog = null;

        @Override
        protected Integer doInBackground(Integer... params) {
            // params保存通过execute()方法传入的所有参数
            int sleep = params[0];
            int result = 0;

            for (int i = 0; i < 101; i++) {
                try {
                    Thread.sleep(sleep);
                    result += i;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (i % 5 == 0) {
                    // 报告进度
                    publishProgress(i);
                }
                // 是否外界提出了取消的请求
                if (isCancelled()) {
                    break;
                }
            }
            // 返回处理结果
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.setMessage("已完成" + values[0] + "%");
            dialog.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            tvMessage.setText("工作已完成，结果为：" + result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setUpDialog();
        }

        private void setUpDialog() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setTitle("正在处理");
            dialog.setCancelable(true);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage("准备开始后台工作......");
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    MyAsyncTask.this.cancel(true);
                }
            });
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
            tvMessage.setText("工作已被取消");
        }
    }
}
