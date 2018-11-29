package edu.niit.multimedia;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AudioActivity extends AppCompatActivity {

    @BindView(R.id.tv_music_name)
    TextView tvMusicName;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.btn_pause)
    Button btnPause;
    @BindView(R.id.btn_stop)
    Button btnStop;
    @BindView(R.id.btn_record)
    Button btnRecord;

    private MediaPlayer player;
    private boolean isRelease = true;   //判断MediaPlayer是否释放

    private MediaRecorder mediaRecorder;
    private boolean isStart = false;   // 录音时的判断

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        ButterKnife.bind(this);

        // 获取mp3文件的作者、时长等信息
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(this, Uri.parse("android.resource://" +
                getPackageName() + "/" +
                R.raw.yesterday));
        tvMusicName.setText(mmr.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_TITLE));
        tvAuthor.setText("歌手：" + mmr.extractMetadata(
                MediaMetadataRetriever.METADATA_KEY_ARTIST));

        int duration = Integer.valueOf(mmr.extractMetadata(MediaMetadataRetriever
                .METADATA_KEY_DURATION));
        tvDuration.setText("播放时长：" + convertMSecondToTime(duration));
        tvCurrentTime.setText("");
        seekbar.setMax(duration);
    }

    private String convertMSecondToTime(long duration) {
        // 1. 使用duration创建时间对象
        Date date = new Date(duration);
        // 2. 创建时间格式化对象 yyyy-MM-dd hh:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        // 3. 对date进行格式化，转为字符串并返回
        return sdf.format(date);
    }

    @OnClick({R.id.btn_start, R.id.btn_pause, R.id.btn_stop, R.id.btn_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                if (isRelease) {
                    player = MediaPlayer.create(this, R.raw.yesterday);
                    isRelease = false;
                }
                player.start();
                btnStart.setEnabled(false);
                btnPause.setEnabled(true);
                btnStop.setEnabled(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!isRelease && player.isPlaying()) {
                            try {
                                Thread.sleep(1000);
                                AudioActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isRelease) {
                                            return;
                                        }
                                        seekbar.setProgress(player.getCurrentPosition());
                                        tvCurrentTime.setText(convertMSecondToTime(
                                                player.getCurrentPosition()));
                                    }
                                });
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                break;
            case R.id.btn_pause:
                player.pause();
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                btnStop.setEnabled(false);
                break;
            case R.id.btn_stop:
                player.reset();
                player.release();
                isRelease = true;
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                btnStop.setEnabled(false);
                seekbar.setProgress(0);
                tvCurrentTime.setText("");
                break;
            case R.id.btn_record:
                if (!isStart) {
                    startRecord();
                    btnRecord.setText("停止录音");
                    isStart = false;
                } else {
                    stopRecord();
                    btnRecord.setText("开始录音");
                    isStart = true;
                }
//                isStart = !isStart;
                break;
        }
    }

    private void startRecord() {
        if (mediaRecorder == null) {
            // 创建目录
            File dir = new File(getFilesDir(), "sounds");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            // 创建一个录音文件
            File soundFile = new File(dir, System.currentTimeMillis() + ".amr");
            if (!soundFile.exists()) {
                try {
                    soundFile.createNewFile();

                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); // 音频输入源
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_WB); // 设置输出格式
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB); // 设置编码格式
                    mediaRecorder.setOutputFile(soundFile.getAbsolutePath());
                    mediaRecorder.prepare();
                    mediaRecorder.start();  // 开始录制
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void stopRecord() {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    public String convertMSecendToTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        Date date = new Date(time);
        String times = sdf.format(date);
        return times;
    }
}
