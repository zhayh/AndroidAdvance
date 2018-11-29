package edu.niit.multimedia;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SoundPoolActivity extends AppCompatActivity {
    private SoundPool soundPool;
    private SparseIntArray soundIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_pool);
        ButterKnife.bind(this);

        try {
            // 初始化SoundPool
            initSoundPool();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSoundPool() throws IOException {
        soundIds = new SparseIntArray();

        soundPool = new SoundPool(5, AudioManager.STREAM_SYSTEM, 5);
        soundIds.put(1, soundPool.load(this, R.raw.duang, 1));
        soundIds.put(2, soundPool.load(this, R.raw.biaobiao, 1));
        soundIds.put(3, soundPool.load(getAssets().openFd("biaobiao.mp3"), 1));
        soundIds.put(4, soundPool.load(getAssets().openFd("duang.mp3"), 1));
        soundIds.put(5, soundPool.load(this, R.raw.biaobiao, 1));
    }

    @OnClick({R.id.btn_play1, R.id.btn_play2, R.id.btn_play3, R.id.btn_play4, R.id.btn_play5, R.id.btn_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_play1:
                soundPool.play(soundIds.get(1), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_play2:
                soundPool.play(soundIds.get(2), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_play3:
                soundPool.play(soundIds.get(3), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_play4:
                soundPool.play(soundIds.get(4), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_play5:
                soundPool.play(soundIds.get(5), 1, 1, 0, 0, 1);
                break;
            case R.id.btn_release:
                soundPool.release();
                break;
        }
    }
}
