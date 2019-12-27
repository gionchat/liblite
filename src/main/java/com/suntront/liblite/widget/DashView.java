package com.suntront.liblite.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.suntront.liblite.R;

/**
 * Copyright (C), 2015-2019, suntront
 * FileName: DashView
 * Author: Jeek
 * Date: 2019/12/4 8:46
 * Description: ${DESCRIPTION}
 */
public class DashView extends View {

    private static final String TAG = DashView.class.getSimpleName();

    private static final int DEFAULT_DASH_WIDTH = 4;
    private static final int DEFAULT_LINE_WIDTH = 4;
    private static final int DEFAULT_LINE_HEIGHT = 2;
    private static final int DEFAULT_LINE_COLOR = 0xff888888;

    //虚线方向（横向）
    private static final int ORIENTATION_HORIZONTAL = 0;
    //虚线方向（竖向）
    private static final int ORIENTATION_VERTICAL = 1;
    //默认方向为水平方向
    private static final int DEFAULT_DASH_ORIENTATION = ORIENTATION_HORIZONTAL;
    // 间距宽度
    private float dashWidth = DEFAULT_DASH_WIDTH;
    // 虚线宽度
    private float lineWidth = DEFAULT_LINE_WIDTH;
    // 虚线高度
    private float lineHeight = DEFAULT_LINE_HEIGHT;
    //虚线颜色
    private int lineColor = DEFAULT_LINE_COLOR;
    //虚线方向
    private int dashOrientation;

    private Paint mPaint;
    private int widthSize;
    private int heightSize;

    public DashView(Context context) {
        this(context, null);
    }

    public DashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(1);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DashView);
        dashWidth = typedArray.getDimension(R.styleable.DashView_dashWidth, DEFAULT_DASH_WIDTH);
        lineWidth = typedArray.getDimension(R.styleable.DashView_lineWidth, DEFAULT_LINE_WIDTH);
        lineHeight = typedArray.getDimension(R.styleable.DashView_lineHeight, DEFAULT_LINE_HEIGHT);
        lineColor = typedArray.getColor(R.styleable.DashView_lineColor, DEFAULT_LINE_COLOR);
        dashOrientation =
                typedArray.getInteger(R.styleable.DashView_dashOrientation, DEFAULT_DASH_ORIENTATION);
        mPaint.setColor(lineColor);
        mPaint.setStrokeWidth(lineHeight);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        heightSize = MeasureSpec.getSize(heightMeasureSpec - getPaddingTop() - getPaddingBottom());
        if (dashOrientation == ORIENTATION_HORIZONTAL) {
            setMeasuredDimension(widthSize, (int) lineHeight);
        } else {
            setMeasuredDimension((int) lineHeight, heightSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (dashOrientation) {
            case ORIENTATION_VERTICAL:
                drawVerticalLine(canvas);
                break;
            default:
                drawHorizontalLine(canvas);
        }
    }

    /**
     * 画横向虚线
     */
    private void drawHorizontalLine(Canvas canvas) {
        float totalWidth = 0;
        canvas.save();
        float[] pts = {0, 0, lineWidth, 0};
        canvas.translate(0, lineHeight / 2);
        while (totalWidth <= widthSize) {
            canvas.drawLines(pts, mPaint);
            canvas.translate(lineWidth + dashWidth, 0);
            totalWidth += lineWidth + dashWidth;
        }
        canvas.restore();
    }

    /**
     * 画竖向虚线
     */
    private void drawVerticalLine(Canvas canvas) {
        float totalWidth = 0;
        canvas.save();
        float[] pts = {0, 0, 0, lineWidth};
        canvas.translate(lineHeight / 2, 0);
        while (totalWidth <= heightSize) {
            canvas.drawLines(pts, mPaint);
            canvas.translate(0, lineWidth + dashWidth);
            totalWidth += lineWidth + dashWidth;
        }
        canvas.restore();
    }
}

