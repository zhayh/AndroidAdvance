package cn.edu.niit.customview.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class HorizontalView extends ViewGroup {
    private int lastInterceptX = 0;
    private int lastInterceptY = 0;
    private int lastX;
    private int lastY;

    private int childWidth = 0;
    private int currentIndex = 0;
    private Scroller mScroller;
    private VelocityTracker tracker;

    public HorizontalView(Context context) {
        super(context);
        init();
    }

    public HorizontalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        mScroller = new Scroller(getContext());
        tracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heigtSize = MeasureSpec.getSize(heightMeasureSpec);

        // 测量所有子元素
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        // 处理wrap_content的情况
        if (getChildCount() == 0) {
            setMeasuredDimension(0, 0);
        } else if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            View childOne = getChildAt(0);
            int childWidth = childOne.getMeasuredWidth();
            int childHeight = childOne.getMeasuredHeight();
            setMeasuredDimension(childWidth * getChildCount(), childHeight);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            int childWidth = getChildAt(0).getMeasuredWidth();
            setMeasuredDimension(childWidth * getChildCount(), heigtSize);
        } else if (heightMode == MeasureSpec.AT_MOST) {
            int childHeight = getChildAt(0).getMeasuredHeight();
            setMeasuredDimension(widthSize, childHeight);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int left = 0;
        View child;

        // 遍历布局的子元素
        for (int i = 0; i < childCount; i++) {
            child = getChildAt(i);
            if (child.getVisibility() != View.GONE) {
                int width = child.getMeasuredWidth();
                childWidth = width;
                child.layout(left, 0, left + width, child.getMeasuredHeight());
                left += width;
            }
        }
    }

    // 处理滑动冲突
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercept = false;
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercept = false;

                // 如果动画还没有执行完成，则打断
                if(!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - lastInterceptX;
                int deltaY = y - lastInterceptY;
                intercept = Math.abs(deltaX) - Math.abs(deltaY) > 0;
                break;
            case MotionEvent.ACTION_UP:
                intercept = false;
                break;
        }
        lastX = x;
        lastY = y;
        lastInterceptX = x;
        lastInterceptY = y;
        return intercept;
    }

    // 拦截水平滑动
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        tracker.addMovement(event);
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - lastX;  // 跟随手指滑动
                scrollBy(-deltaX, 0);
                break;
            case MotionEvent.ACTION_UP:
                // 相对于当前view滑动的距离
                int distance = getScrollX() - currentIndex;
                if (Math.abs(distance) > childWidth / 2) {
                    if (distance > 0) {
                        currentIndex++;
                    } else {
                        currentIndex--;
                    }
                } else {
                    // 调用该方法计算1000ms内滑动的平均速度
                    tracker.computeCurrentVelocity(1000);
                    float xV = tracker.getXVelocity();  // 获取到水平方向上的速度

                    if(Math.abs(xV) > 50) {
                        if(xV > 0) {
                            currentIndex++;
                        } else {
                            currentIndex--;
                        }
                    }
                }
                currentIndex = currentIndex < 0 ? 0 : currentIndex > getChildCount() - 1 ?
                        getChildCount() - 1 : currentIndex;
                smoothScrollTo(currentIndex * childWidth, 0);
                // 重置速度计算器
                tracker.clear();
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    private void smoothScrollTo(int destX, int destY) {
        mScroller.startScroll(getScrollX(), getScrollY(),
                destX - getScrollX(), destY - getScrollY());
        invalidate();
    }
}
