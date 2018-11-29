package edu.niit.multimedia;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MusicService extends Service {
    public static final int DURATION_TYPE = 1;
    public static final int CURRENT_TIME_TYPE = 2;
    public static final int START_TYPE_OPERATION = 1;
    public static final int OPEARTION_PLAY = 1;
    public static final int OPEARTION_NEXT = 2;
    public static final int OPEARTION_PREVIOUS = 3;

    private MediaPlayer player;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int startType = intent.getIntExtra("start_type", 0);
        switch(startType) {
            case START_TYPE_OPERATION:
                int operation = intent.getIntExtra("operation", OPEARTION_PLAY);
                switch (operation) {
                    case OPEARTION_PLAY:
                        playOrPauseMusic(intent);//开始和暂停播放音乐
                        break;
                    case OPEARTION_NEXT:
                        if (player != null) {
                            player.stop();
                            player=null;
                            startNewMusic(intent);
                        }
                        break;
                    case OPEARTION_PREVIOUS:
                        if (player != null) {
                            player.stop();
                            player=null;
                            startNewMusic(intent);
                        }
                        break;
                }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void playOrPauseMusic(Intent intent) {
        boolean isPause = intent.getBooleanExtra("state", true);
        if(player == null) {
            startNewMusic(intent);
            return;
        }
        if(isPause) {
            player.pause();
        } else {
            startNewMusic(intent);
        }
    }

    private void startNewMusic(Intent intent) {
        String path = intent.getStringExtra("MusicName");
        if(player == null) {
            player = new MediaPlayer();
        }
        player.reset();


        try {
            player.setDataSource(path);
            player.prepare();
            player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    player.start();
                    int time = player.getDuration();//获得所有的时间
                    Intent intent = new Intent("edu.niit.multimedia");
                    intent.putExtra("time", time);
                    intent.putExtra("type", DURATION_TYPE);
                    sendBroadcast(intent);
                    MusicThread thread = new MusicThread();
                    thread.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class MusicThread extends Thread {
        @Override
        public void run() {
            //获得当前的时长
            while (player.isPlaying()) {
                int time = player.getCurrentPosition();
                Intent intent = new Intent("edu.niit.multimedia");
                intent.putExtra("time", time);
                intent.putExtra("type", CURRENT_TIME_TYPE);
                sendBroadcast(intent);
                //每隔一秒发送一次
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
