package cn.edu.niit.customview.animation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class PointView extends View {
    private static final float RADIUS = 90;
    private Point currentPoint;
    private Paint mPaint;


    public PointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(currentPoint == null) {
            currentPoint = new Point(RADIUS, RADIUS);

            float x = currentPoint.getX();
            float y = currentPoint.getY();

            canvas.drawCircle(x, y, RADIUS, mPaint);

            Point startPoint = new Point(RADIUS, RADIUS);
            Point endPoint = new Point(700, 1000);

            ValueAnimator valueAnimator = ValueAnimator.ofObject(new PointEvaluator(), startPoint,
                    endPoint);

            valueAnimator.setDuration(5000);
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator.setRepeatMode(ValueAnimator.REVERSE);

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    currentPoint = (Point) animation.getAnimatedValue();
                    invalidate();
                }
            });
            valueAnimator.start();
        } else {
            float x = currentPoint.getX();
            float y = currentPoint.getY();
            canvas.drawCircle(x, y, RADIUS, mPaint);
        }
    }
}
