package edu.niit.materialdesign;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MyDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;
    private int orientation;

    // 在构造方法中加载系统自带的分割线，也就是ListView的分割线
    public MyDecoration(Context context, int orientation) {
        this.orientation = orientation;
        int[] attrs = new int[]{android.R.attr.listDivider};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        divider = typedArray.getDrawable(0);
        typedArray.recycle();
    }

    // 重写getItemOffsets()方法，设置item周边的偏移量
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(orientation == RecyclerView.HORIZONTAL) {
            outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
        } else {
            outRect.set(0, 0, 0, divider.getIntrinsicHeight());
        }

    }

    // 重写onDraw()方法，实现装饰的回调方法，该方法可以在装饰区域任意画画
    // 本例实现线性分割的分割线，
    // 当布局是vertical时在item的下边偏移出一条线的高度，
    // 当布局是horizontal时在item的右边偏移出一条线的宽度
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if(orientation == RecyclerView.HORIZONTAL) {
            drawVertical(c, parent, state);
        } else {
            drawHorizontal(c, parent, state);
        }
    }

    // 画垂直分割线
    private void drawVertical(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();

        int top = parent.getPaddingTop();
        int bottom = parent.getHeight() - parent.getPaddingBottom();
        for(int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int left = child.getRight() + params.rightMargin;
            int right = left + divider.getIntrinsicWidth();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }

    // 画水平分割线
    private void drawHorizontal(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for(int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
