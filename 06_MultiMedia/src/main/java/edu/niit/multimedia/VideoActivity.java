package edu.niit.multimedia;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    @BindView(R.id.video_show)
    SurfaceView videoShow;

    private SurfaceHolder surfaceHolder;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        ButterKnife.bind(this);

        surfaceHolder = videoShow.getHolder();  // 通过surfaceView获得
        surfaceHolder.setFixedSize(320, 300);
        surfaceHolder.addCallback(this);        // 设置surfaceHolder的callback
    }

    @OnClick({R.id.btn_start, R.id.btn_pause, R.id.btn_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                player.start();
                break;
            case R.id.btn_pause:
                player.pause();
                break;
            case R.id.btn_stop:
                player.stop();
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player = MediaPlayer.create(VideoActivity.this, R.raw.lesson);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setDisplay(surfaceHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(player.isPlaying()) {
            player.stop();
        }
        player.release();
    }
}
