package edu.niit.multimedia;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoViewActivity extends AppCompatActivity {

    @BindView(R.id.video_show)
    VideoView videoShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        ButterKnife.bind(this);

        videoShow.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" +
                R.raw.lesson));
        videoShow.setMediaController(new MediaController(this));
    }

    @OnClick({R.id.btn_start, R.id.btn_pause, R.id.btn_stop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start:
                videoShow.start();
                break;
            case R.id.btn_pause:
                videoShow.pause();
                break;
            case R.id.btn_stop:
                videoShow.stopPlayback();
                break;
        }
    }
}
