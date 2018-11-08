package edu.niit.multithread.async;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.niit.multithread.R;

public class AsyncTaskActivity extends AppCompatActivity {
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.tv_message)
    TextView tvMessage;
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.download_image)
    ImageView downloadImage;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_start})
    public void onClicked(View view) {
        new MyAsyncTask().execute(100);
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

            progressBar.setProgress(values[0]);

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
            dialog = new ProgressDialog(AsyncTaskActivity.this);
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

            progressBar.setProgress(0);
        }
    }

    private static class StaticAsycTask extends AsyncTask<Integer, Integer, Integer> {
        private ProgressDialog dialog;
        private WeakReference<AsyncTaskActivity> activityRef;

        StaticAsycTask(AsyncTaskActivity activity) {
            this.activityRef = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            AsyncTaskActivity activity = activityRef.get();
            dialog = ProgressDialog.show(activity, "Waiting...", "下载图片");
            dialog.show();
        }

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

            AsyncTaskActivity activity = activityRef.get();
            dialog.setMessage("已完成" + values[0] + "%");
            dialog.setProgress(values[0]);

            activity.progressBar.setProgress(values[0]);

        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);

            AsyncTaskActivity activity = activityRef.get();
            activity.tvMessage.setText("工作已完成，结果为：" + result);
            dialog.dismiss();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            AsyncTaskActivity activity = activityRef.get();
            dialog.dismiss();
            activity.tvMessage.setText("工作已被取消");

            activity.progressBar.setProgress(0);
        }
    }
}
