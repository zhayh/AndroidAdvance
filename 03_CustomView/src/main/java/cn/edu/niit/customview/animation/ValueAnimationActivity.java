package cn.edu.niit.customview.animation;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.BounceInterpolator;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.niit.customview.R;

public class ValueAnimationActivity extends AppCompatActivity {

    @BindView(R.id.btn_value)
    Button btnValue;
    @BindView(R.id.point_view_2)
    PointView2 pointView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_value_animation);
        ButterKnife.bind(this);

//        initCustomObjectAttr();
//        initValueAnimator2();

        // ViewPropertyAnimator
        btnValue.animate().alpha(0f).x(500).y(500);
        btnValue.animate().alpha(0f).setDuration(1000).setInterpolator(new BounceInterpolator());
    }

    // 在代码中设置Animator参数并启动
    private void initValueAnimator1() {
        // 步骤1. 设置动画属性的初始值和结束值
        ValueAnimator valueAnimator = ValueAnimator.ofInt(btnValue.getLayoutParams().width, 1000);

        // 步骤2. 设置动画的播放属性
        valueAnimator.setDuration(500); // 设置时长
        valueAnimator.setStartDelay(500);  // 设置动画延迟播放时间
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);  // 设置重复次数，infinite为无限重复
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);  // 设置重复播放动画模式

        // 步骤3， 将改变的值手动赋值给对象的属性值，通过动画的更新监听器
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int currentValue = (int) animation.getAnimatedValue();

                // 步骤4：将改变后的值赋给对象的属性值
                btnValue.getLayoutParams().width = currentValue;

                // 步骤5：刷新视图，即重新绘制，从而实现动画效果
                btnValue.requestLayout();
            }
        });
        // 步骤6. 启动动画
        valueAnimator.start();
    }

    // 根据/res/animator文件夹中的xml设置启动动画
    private void initValueAnimator2() {
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.set_value_animation);
        animator.setTarget(btnValue);
        animator.start();
    }

    //
    private void initObjectAnimator1() {
        // 动画效果：透明度, 常规 - 全透明 - 常规
        ObjectAnimator alpha = ObjectAnimator.ofFloat(btnValue, "alpha", 1f, 0f, 1f);
        alpha.setDuration(5000);
        alpha.start();

        // 旋转
        ObjectAnimator rotation = ObjectAnimator.ofFloat(btnValue, "rotation", 0f, 360f);
        // 平移
        float currentX = btnValue.getX();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(btnValue, "translationX", currentX,
                300, currentX);
        // 缩放
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(btnValue, "scaleX", 1f, 3f, 1f);

        // 组合动画
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(translationX).with(rotation).before(scaleX);
        animatorSet.setDuration(5000);
        animatorSet.start();
    }

    private void initObjectAnimator2() {
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.set_object_animation);
        animator.setTarget(btnValue);
        animator.start();
    }

    private void initCustomObjectAttr() {
        ObjectAnimator animator = ObjectAnimator.ofObject(pointView2, "color",
                new ColorEvaluator(), "#0000ff", "#ff0000");
        animator.setDuration(8000);
        animator.start();
    }
}
