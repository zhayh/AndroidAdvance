package cn.edu.niit.customview.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.edu.niit.customview.R;

public class TitleBar extends RelativeLayout {
    @BindView(R.id.iv_titlebar_left)
    ImageView ivTitlebarLeft;
    @BindView(R.id.tv_titlebar_title)
    TextView tvTitlebarTitle;
    @BindView(R.id.iv_titlebar_right)
    ImageView ivTitlebarRight;
    @BindView(R.id.layout_titlebar_rootlayout)
    RelativeLayout layoutTitlebarRootlayout;

    private int mColor = Color.BLUE;
    private int mTextColor = Color.WHITE;
    private String titleName;

    public TitleBar(Context context) {
        super(context);
        initView(context);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        initTypeArray(context, attrs);
        initView(context);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initTypeArray(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_custom_title, this, true);
        ButterKnife.bind(this);

        // 设置背景色
        layoutTitlebarRootlayout.setBackgroundColor(mColor);
        // 设置标题文字颜色
        tvTitlebarTitle.setTextColor(mTextColor);
        setTitle(titleName);
    }

    private void initTypeArray(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        mColor = typedArray.getColor(R.styleable.TitleBar_title_bg, Color.BLUE);
        mTextColor = typedArray.getColor(R.styleable.TitleBar_title_text_color, Color.WHITE);
        titleName = typedArray.getString(R.styleable.TitleBar_title_text);
        typedArray.recycle();
    }


    public void setTitle(String titleName) {
        if (!TextUtils.isEmpty(titleName)) {
            tvTitlebarTitle.setText(titleName);
        }
    }

    public void setLeftListener(OnClickListener onClickListener) {
        ivTitlebarLeft.setOnClickListener(onClickListener);
    }

    public void setRightListener(OnClickListener onClickListener) {
        ivTitlebarRight.setOnClickListener(onClickListener);
    }

}
