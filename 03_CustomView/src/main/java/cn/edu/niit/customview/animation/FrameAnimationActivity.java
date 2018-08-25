package cn.edu.niit.customview.animation;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.edu.niit.customview.R;

public class FrameAnimationActivity extends AppCompatActivity {

    @BindView(R.id.btnStart)
    Button btnStart;
    @BindView(R.id.btnStop)
    Button btnStop;
    @BindView(R.id.frameAnim)
    ImageView frameAnim;
    @BindView(R.id.imgInsideCircle)
    ImageView imgInsideCircle;
    @BindView(R.id.imgOuterCircle)
    ImageView imgOuterCircle;

    private Animation animInsideCircle; //内圆动画
    private Animation animOuterCircle;  //外圆动画

    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_animation);
        ButterKnife.bind(this);

        //初始化旋转动画
        rotateAnimInit();
    }

    private void rotateAnimInit() {
        animInsideCircle = AnimationUtils.loadAnimation(this, R.anim.inside_rotate_anim);
        animInsideCircle.setInterpolator(new LinearInterpolator());  // 匀速动画

        animOuterCircle = AnimationUtils.loadAnimation(this, R.anim.outer_rotate_anim);
        animOuterCircle.setInterpolator(new LinearInterpolator());

        animationDrawable = (AnimationDrawable) frameAnim.getBackground();

        startAnimation();

    }

    @OnClick({R.id.btnStart, R.id.btnStop})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnStart:
                startAnimation();
                break;
            case R.id.btnStop:
                stopAnimation();
                break;
        }
    }

    private void stopAnimation() {
        imgInsideCircle.clearAnimation();
        imgOuterCircle.clearAnimation();
        animationDrawable.stop();
    }

    private void startAnimation() {
        imgInsideCircle.startAnimation(animInsideCircle);
        imgOuterCircle.startAnimation(animOuterCircle);
        animationDrawable.start();
    }
}
