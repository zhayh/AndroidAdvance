package cn.edu.niit.customview.viewslide;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AnimationUtils;

import cn.edu.niit.customview.R;

public class ViewSlideActivity extends AppCompatActivity {

    @butterknife.BindView(R.id.custom_view)
    CustomView customView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_slide);
        butterknife.ButterKnife.bind(this);

        //使用属性动画使view滑动
//        ObjectAnimator.ofFloat(customView, "translationX", 0, 300)
//                .setDuration(1000).start();

        //使用View动画使view滑动
//        customView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.translate));

        // 使用scrollTo进行平滑移动
//        customView.smoothScrollTo(-400, 0);
    }
}
