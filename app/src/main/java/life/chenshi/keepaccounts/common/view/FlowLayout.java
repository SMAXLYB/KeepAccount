package life.chenshi.keepaccounts.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import life.chenshi.keepaccounts.module.common.utils.ViewUtilKt;

/**
 * @author smaxlyb
 */
public class FlowLayout extends ViewGroup {
    private static final String TAG = "FlowLayout";
    private List<Rect> mChildrenBounds;
    private int itemSpacing;
    private int lineSpacing;

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        mChildrenBounds = new ArrayList<>();
        // item之间的间隔
        itemSpacing = ViewUtilKt.dp2px(20);
        // 行与行之间的间隔
        lineSpacing = ViewUtilKt.dp2px(10);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 1.获取父View的测量要求
        int widthConstraintMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthConstraintSize = MeasureSpec.getSize(widthMeasureSpec);

        // 2.以下根据父view的测量要求,剩余空间,子view的尺寸要求,计算出对子view的尺寸要求,然后子view确定自己的大小
        // padding
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int childCount = getChildCount();
        // 已使用宽度
        int widthUsed = 0;
        // 已使用高度
        int heightUsed = paddingTop;
        // 行高
        int lineHeight = 0;
        // 一行左边已经使用了的宽度,随着view数目增加会变化
        int lineWidthUsed = getPaddingLeft();
        // 除去左边已经使用的空间和右边的padding之后剩余的空间
        int lineWidthRemained = widthConstraintSize - paddingRight;

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            // 触发子view的测量
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);

            // 确定子view的测量宽高后供后续使用
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            MarginLayoutParams childLayoutParams = (MarginLayoutParams) child.getLayoutParams();
            int topMargin = childLayoutParams.topMargin;
            int bottomMargin = childLayoutParams.bottomMargin;
            int leftMargin = childLayoutParams.leftMargin;
            int rightMargin = childLayoutParams.rightMargin;

            // 是否换行
            if (widthConstraintMode != MeasureSpec.UNSPECIFIED && (child.getMeasuredWidth() > lineWidthRemained - lineWidthUsed - leftMargin - rightMargin)) {
                // 计算已使用高度
                heightUsed += lineHeight + lineSpacing;
                // 重置已使用空间
                lineWidthUsed = paddingLeft;
                lineHeight = 0;
            }

            // 3.将子View的布局参数保存,以便后续布局使用
            Rect childBound;
            // 可能会多次调用Measure方法,此处可以复用list
            if (mChildrenBounds.size() <= i) {
                childBound = new Rect();
                mChildrenBounds.add(childBound);
            } else {
                childBound = mChildrenBounds.get(i);
            }

            // 要支持margin
            childBound.set(leftMargin + lineWidthUsed, heightUsed + topMargin, leftMargin + lineWidthUsed + childWidth, topMargin + heightUsed + childHeight);

            // 4.增加宽度和高度
            lineWidthUsed += childWidth + itemSpacing + leftMargin + rightMargin;
            lineHeight = Math.max(lineHeight, topMargin + bottomMargin + childHeight);

            // 确定最宽的宽度
            widthUsed = Math.max(widthUsed, lineWidthUsed);
        }

        // 5.根据父view的要求确定测量宽高
        int measuredWidth = resolveSize(widthUsed, widthMeasureSpec);
        // 高度要加上bottomPadding和最后一行行高
        int measuredHeight = resolveSize(heightUsed + paddingBottom + lineHeight, heightMeasureSpec);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            Rect childBound = mChildrenBounds.get(i);
            child.layout(childBound.left, childBound.top, childBound.right, childBound.bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
