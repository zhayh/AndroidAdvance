package cn.edu.niit.customview.animation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.niit.customview.R;
import cn.edu.niit.customview.viewslide.ViewSlideActivity;

public class ViewAnimationActivity extends AppCompatActivity {

    @BindView(R.id.tv_view)
    TextView tvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_animation);
        ButterKnife.bind(this);

        Animation translateAnimation = AnimationUtils.loadAnimation(this, R.anim.view_animation);
        tvView.startAnimation(translateAnimation);

        // 在代码中设置
//        Animation translateAnimation = new TranslateAnimation(0, 500, 0, 500);
//        translateAnimation.setDuration(3000);
//        tvView.startAnimation(translateAnimation);


    }

    // 退出动画
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @OnClick(R.id.tv_view)
    public void onViewClicked() {
        // 启动动画
        Intent intent = new Intent(this, ViewSlideActivity.class);
        startActivity(intent);

        // 淡入淡出的动画效果
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
