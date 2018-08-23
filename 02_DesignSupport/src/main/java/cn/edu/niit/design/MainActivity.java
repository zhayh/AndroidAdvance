package cn.edu.niit.design;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_snackbar)
    Button btnSnackbar;
    @BindView(R.id.bt_textInputLayout)
    Button btTextInputLayout;
    @BindView(R.id.btn_voice)
    FloatingActionButton btnVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_snackbar)
    public void onBtnSnackbar() {
        Snackbar.make(btnSnackbar, "标题", Snackbar.LENGTH_LONG)
                .setAction("点击事件", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "Toast", Toast.LENGTH_SHORT).show();
                    }
                }).setDuration(Snackbar.LENGTH_LONG).show();
    }

    @OnClick(R.id.bt_textInputLayout)
    public void onBtTextInputLayoutClicked() {
        Intent intent=new Intent(MainActivity.this,TextInputLayoutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_voice)
    public void onBtnVoiceClicked() {
    }
}
