package edu.niit.multimedia;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.music_list)
    RecyclerView musicList;
    @BindView(R.id.tv_duration)
    TextView tvDuration;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;
    @BindView(R.id.seekbar)
    SeekBar seekbar;
    @BindView(R.id.btn_play)
    ImageButton btnPlay;

    private MusicFilter musicFilter;
    private MusicListAdapter mAdapter;
    private ArrayList<File> mFiles;
    private File[] mPreFiles;
    private MusicBroadcastReceiver musicReceiver;
    private boolean isPause = true;
    private int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        applyPermission();

        // 注册广播
        musicReceiver = new MusicBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("edu.niit.multimedia");
        registerReceiver(musicReceiver, filter);
    }

    private void applyPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                //判断是否勾选禁止后不再询问
                boolean showRequestPermission = ActivityCompat
                        .shouldShowRequestPermissionRationale(this, permissions[0]);
                if (showRequestPermission) {
                    Toast.makeText(this, "权限未申请", Toast.LENGTH_SHORT).show();
                }
            } else {
                initData();
                initView();
            }
        }
    }

    private void initView() {
        //将mp3文件添加到RecyclerView中
        mAdapter = new MusicListAdapter(this, mFiles);
        musicList.setLayoutManager(new LinearLayoutManager(this));
        musicList.setAdapter(mAdapter);
        mAdapter.setSelectedBackground(currentPos);
        mAdapter.setOnItemClickListener(new MusicListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mAdapter.setSelectedBackground(position);
                currentPos = position;
                if(!isPause) {

                }
                isPause = true;
                setPlayOrPauseButtonImage();
            }
        });
    }

    private void initData() {
        //获得音乐的文件列表
        File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        Log.d("data", "音乐的路径是：" + musicDir.getAbsolutePath());
        //将MP3文件筛选出来
        musicFilter = new MusicFilter();
        mFiles = new ArrayList<>();
        mPreFiles = musicDir.listFiles();
        for (File file : mPreFiles) {
            if (musicFilter.accept(musicDir, file.getName())) {
                mFiles.add(file);
            }
        }
    }

    @OnClick({R.id.btn_previous, R.id.btn_play, R.id.btn_next})
    public void onViewClicked(View view) {
        Intent intent = new Intent(MainActivity.this, MusicService.class);
        switch (view.getId()) {
            case R.id.btn_previous:
                intent.putExtra("start_type", MusicService.START_TYPE_OPERATION);
                intent.putExtra("operation", MusicService.OPEARTION_PREVIOUS);
                if (currentPos > 0) {
                    currentPos = currentPos - 1;
                }
                intent.putExtra("MusicName", mFiles.get(currentPos).getAbsolutePath());
                Log.d("currentPosition", "播放上一首时， 当前播放的文件的位置！" + currentPos);
                startService(intent);
                isPause = false; //设置暂停的状态
                setPlayOrPauseButtonImage();//设置播放（暂停）按钮的图片
                break;
            case R.id.btn_play:
                isPause = !isPause;//定义全局变量，记录暂停状态。
                Log.d("pause", "点击按钮后的暂停状态" + isPause);
                intent.putExtra("start_type", MusicService.START_TYPE_OPERATION);
                intent.putExtra("MusicName", mFiles.get(currentPos).getAbsolutePath());
                intent.putExtra("operation", MusicService.OPEARTION_PLAY);
                intent.putExtra("state", isPause);
                startService(intent);
                setPlayOrPauseButtonImage();
                break;
            case R.id.btn_next:
                currentPos = currentPos + 1;//全局变量，记录当前歌曲播放的位置
                intent.putExtra("start_type", MusicService.START_TYPE_OPERATION);
                intent.putExtra("operation", MusicService.OPEARTION_NEXT);
                intent.putExtra("MusicName", mFiles.get(currentPos).getAbsolutePath());
                startService(intent);
                isPause = false; //设置暂停的状态
                setPlayOrPauseButtonImage();//设置播放（暂停）按钮的图片
                Log.d("currentPosition", "播放下一首时， 当前播放的文件的位置！" + currentPos);
                break;
        }
    }

    /*
    设置开始（暂停按钮的图片）
     */
    private void setPlayOrPauseButtonImage() {
        if (!isPause) {
            btnPlay.setBackgroundResource(R.drawable.btn_pause);
        } else if (isPause) {
            btnPlay.setBackgroundResource(R.drawable.btn_play);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(musicReceiver);
    }

    class MusicFilter implements FilenameFilter {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".mp3");
        }
    }
}
