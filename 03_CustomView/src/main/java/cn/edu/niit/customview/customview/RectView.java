package cn.edu.niit.customview.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import cn.edu.niit.customview.R;

public class RectView extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mColor = Color.RED;

    public RectView(Context context) {
        super(context);
        initDraw();
    }

    public RectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectView);
        //提取RectView属性集合的rect_color属性，如果没设置默认值为Color.RED
        mColor = typedArray.getColor(R.styleable.RectView_rect_color, Color.RED);
        // 回收typeArray
        typedArray.recycle();
        initDraw();
    }

    public RectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDraw();
    }

    private void initDraw() {
        mPaint.setColor(mColor);
        mPaint.setStrokeWidth(1.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 处理padding属性
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        canvas.drawRect(0 + paddingLeft, 0 + paddingTop,
                width + paddingRight, height + paddingBottom, mPaint);
    }

    // 解决match_parent和wrap_content的宽高一样的问题，给RectView设置默认的宽高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(400, 400);
        } else if(widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(400, heightSpecSize);
        } else if(heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, 400);  // 单位是px
        }
    }
}
