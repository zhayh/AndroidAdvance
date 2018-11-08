package edu.niit.multithread.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import edu.niit.multithread.R;

public class SplashActivity extends Activity {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

         handler = new Handler();

         handler.postDelayed(new Runnable() {
             @Override
             public void run() {
                 Intent intent = new Intent(SplashActivity.this, HandlerActivity.class);
                 startActivity(intent);

                 SplashActivity.this.finish();
             }
         }, 5000);
    }
}
