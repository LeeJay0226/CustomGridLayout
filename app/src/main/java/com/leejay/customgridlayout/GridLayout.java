package com.leejay.customgridlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

/**
 * Created by leejay on 15/02/02.
 * 九宫格布局，可以合并几个item作为一个item
 */

public class GridLayout extends ViewGroup {

    private int mGaps = 0;
    private int mColumns = 1;
    private int mRows = 1;
    private boolean[][] mFlags;

    @IntDef({HORIZONTAL, VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {

    }

    private int mOrientation;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;


    public GridLayout(Context context) {
        this(context, null);
    }

    public GridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridLayout, defStyle, 0);
        if (a != null) {
            mColumns = a.getInt(R.styleable.GridLayout_gridlayout_columns, mColumns);
            mRows = a.getInt(R.styleable.GridLayout_gridlayout_rows, mRows);
            if (1 > mColumns) {
                mColumns = 1;
            }
            if (1 > mRows) {
                mRows = 1;
            }
            mFlags = new boolean[mRows][mColumns];
            mGaps = a.getDimensionPixelSize(R.styleable.GridLayout_gridlayout_gaps, mGaps);
            mOrientation = a.getInt(R.styleable.GridLayout_gridlayout_orientation, HORIZONTAL);
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int totalHeight = 0;
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                final int avgWidth = (totalWidth - getPaddingLeft() - getPaddingRight() - mGaps * (mColumns - 1)) / mColumns;
                totalHeight = getPaddingTop() + getPaddingBottom() + (avgWidth + mGaps) * mRows - mGaps;
                if (MeasureSpec.AT_MOST == heightMode) {
                    totalHeight = Math.min(totalHeight, MeasureSpec.getSize(heightMeasureSpec));
                }
                break;
            case MeasureSpec.EXACTLY:
                totalHeight = MeasureSpec.getSize(heightMeasureSpec);
                break;
        }
        setMeasuredDimension(totalWidth, totalHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (!changed) {
            return;
        }
        int count = getChildCount();
        for (boolean[] mFlag : mFlags) {
            Arrays.fill(mFlag, false);
        }

        r -= (getPaddingRight() + l);
        b -= (getPaddingBottom() + t);
        l = getPaddingLeft();
        t = getPaddingTop();
        final int avgWidth = (r - l - mGaps * (mColumns - 1)) / mColumns;
        final int avgHeight = (b - t - mGaps * (mRows - 1)) / mRows;
        final int gaps = mGaps;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                GridLayoutParams lp = (GridLayoutParams) child.getLayoutParams();
                final int rowSpan = lp.rowSpan;
                final int colSpan = lp.colSpan;
                Position pos = findPosition(rowSpan, colSpan);
                if (null != pos) {
                    int left = (avgWidth + gaps) * pos.colIndex + l;
                    int top = (avgHeight + gaps) * pos.rowIndex + t;
                    int right = left + colSpan * (avgWidth + gaps) - gaps;
                    int bottom = top + rowSpan * (avgHeight + gaps) - gaps;
                    if (r - right <= gaps) {
                        right = r;
                    }
                    if (b - bottom <= gaps) {
                        bottom = b;
                    }
                    child.layout(left, top, right, bottom);
                    fillFlags(pos, rowSpan, colSpan);
                }
            }
        }
    }

    private void fillFlags(Position pos, int rowSpan, int colSpan) {
        int rowIndex = pos.rowIndex;
        int colIndex = pos.colIndex;
        for (int r = 0; r < rowSpan; r++) {
            for (int c = 0; c < colSpan; c++) {
                mFlags[rowIndex + r][colIndex + c] = true;
            }
        }
    }

    private Position findPosition(int rowSpan, int colSpan) {
        final int rows = mRows;
        final int cols = mColumns;
        final boolean[][] flags = mFlags;
        if (mOrientation == HORIZONTAL) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (!flags[r][c]) {
                        boolean b = true;
                        for (int ri = 0; ri < rowSpan; ri++) {
                            for (int cj = 0; cj < colSpan; cj++) {
                                if (r + ri >= rows || c + cj >= cols || flags[r + ri][c + cj]) {
                                    b = false;
                                    break;
                                }
                            }
                        }
                        if (b) {
                            return new Position(r, c);
                        }
                    }
                }
            }
        } else {
            for (int c = 0; c < cols; c++) {
                for (int r = 0; r < rows; r++) {
                    if (!flags[r][c]) {
                        boolean b = true;
                        for (int ri = 0; ri < rowSpan; ri++) {
                            for (int cj = 0; cj < colSpan; cj++) {
                                if (r + ri >= rows || c + cj >= cols || flags[r + ri][c + cj]) {
                                    b = false;
                                    break;
                                }
                            }
                        }
                        if (b) {
                            return new Position(r, c);
                        }
                    }
                }
            }
        }
        return null;
    }

    class Position {
        int rowIndex;
        int colIndex;

        Position(int rowIndex, int colIndex) {
            this.colIndex = colIndex;
            this.rowIndex = rowIndex;
        }
    }

    /**
     * Should the layout be a column or a row.
     *
     * @param orientation Pass {@link #HORIZONTAL} or {@link #VERTICAL}. Default
     *                    value is {@link #HORIZONTAL}.
     * @attr ref R.styleable#GridLayout_gridlayout_orientation
     */
    public void setOrientation(@OrientationMode int orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            requestLayout();
        }
    }

    public int getmOrientation() {
        return mOrientation;
    }

    public int getmGaps() {
        return mGaps;
    }

    public void setmGaps(int mGaps) {
        this.mGaps = mGaps;
    }

    public int getmColumns() {
        return mColumns;
    }

    public void setmColumns(int mColumns) {
        this.mColumns = mColumns;
    }

    public int getmRows() {
        return mRows;
    }

    public void setmRows(int mRows) {
        this.mRows = mRows;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new GridLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new GridLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new GridLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public static class GridLayoutParams extends ViewGroup.LayoutParams {
        public int rowSpan = 1;
        public int colSpan = 1;

        public GridLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.GridLayout, 0, 0);
            if (a != null) {
                rowSpan = a.getInteger(R.styleable.GridLayout_gridItem_rowSpan, rowSpan);
                colSpan = a.getInteger(R.styleable.GridLayout_gridItem_colSpan, colSpan);
                a.recycle();
            }
        }

        public GridLayoutParams(LayoutParams source) {
            super(source);
        }

        public GridLayoutParams(int rowSpan, int colSpan) {
            super(0, 0);
            this.rowSpan = rowSpan;
            this.colSpan = colSpan;
        }
    }
}
