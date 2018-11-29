package edu.niit.multimedia;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MusicBroadcastReceiver extends BroadcastReceiver {
    public static final String MUSIC_TIME_ACTION = "edu.niit.multimedia";

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity activity = (MainActivity) context;
        Log.d("MusicBroadcastReceiver", "接收到一个广播");
        int type = intent.getIntExtra("type", 0);
        switch (type) {
            case MusicService.DURATION_TYPE:
                int time = intent.getIntExtra("time", 0);
                Log.d("data", "这是总时间"+time);
                Date dateDuration = new Date(time);
                SimpleDateFormat formatDuration = new SimpleDateFormat("mm:ss");
                activity.seekbar.setMax(time);
                activity.tvDuration.setText(formatDuration.format(dateDuration));
                break;
            case MusicService.CURRENT_TIME_TYPE:
                int currenttime=intent.getIntExtra("time", 0);
                Date dateCurrentTime = new Date(currenttime);
                SimpleDateFormat formatCurrent = new SimpleDateFormat("mm:ss");
                Log.d("data", "这是当前时间"+currenttime);
                activity.seekbar.setProgress(currenttime);
                activity.tvCurrentTime.setText(formatCurrent.format(dateCurrentTime));
                break;
            default:
                break;
        }
    }
}
