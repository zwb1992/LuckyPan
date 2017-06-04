package com.zwb.luckypan.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zwb.luckypan.R;

/**
 * Created by zwb
 * Description
 * Date 2017/6/3.
 */

public class LuckyPan extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceHolder;
    private int[] imgs = new int[]{R.mipmap.ipad, R.mipmap.bike, R.mipmap.xiaolian, R.mipmap.umbrella, R.mipmap.xiaomi, R.mipmap.xiaolian};
    private int[] colors = new int[]{R.color.yellor, R.color.gray, R.color.yellor, R.color.gray, R.color.yellor, R.color.gray};
    private String[] contents = new String[]{"IPAD", "自行车", "恭喜发财", "雨伞", "小米", "恭喜发财"};
    private Bitmap[] bitmaps;
    private boolean isInit = false;
    private Paint mPaint;
    private Paint mTextPaint;
    private RectF rect;
    private int mCount = 6;
    private float mStartAngle = 0;
    private float textSize = 16;
    private int mRadius;

    public LuckyPan(Context context) {
        this(context, null);
    }

    public LuckyPan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyPan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, getResources().getDisplayMetrics());
        mTextPaint.setTextSize(textSize);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int size = Math.min(w, h);
        setMeasuredDimension(size, size);
        rect = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mRadius = getMeasuredWidth() / 2;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isInit = true;
        bitmaps = new Bitmap[imgs.length];
        for (int i = 0; i < bitmaps.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), imgs[i]);
        }
        new Thread() {
            @Override
            public void run() {
                if (isInit) {
                    long start = System.currentTimeMillis();
                    draw();
                    long end = System.currentTimeMillis();
                    if (end - start < 50) {
                        try {
                            //确保能够休眠50毫秒以上再绘制
                            Thread.sleep(50 - end + start);
                        } catch (Exception e) {

                        }
                    }
                    mStartAngle++;
                }
            }
        }.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isInit = false;
    }

    /**
     * 画图
     */
    private void draw() {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.GRAY);
            float sweepAngle = 360.0f / mCount;
            float tempAngle = mStartAngle;
            for (int i = 0; i < mCount; i++) {
                mPaint.setColor(ContextCompat.getColor(getContext(), colors[i]));
                drawArea(rect, tempAngle, sweepAngle, canvas);
                drawText(rect, tempAngle, sweepAngle, contents[i], canvas);
                drawIcon(tempAngle,canvas,bitmaps[i]);
                tempAngle += sweepAngle;
            }
// canvas.drawBitmap(bitmaps[0], 0, 0, null);
//        canvas.drawBitmap(bitmaps[0], null, rect, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

    }

    /**
     * 绘制扇形区域
     */
    private void drawArea(RectF rect, float startAngle, float sweepAngle, Canvas canvas) {
        canvas.drawArc(rect, startAngle, sweepAngle, true, mPaint);
    }

    /**
     * 绘制文字
     *
     * @param canvas 画布
     */
    private void drawText(RectF rect, float startAngle, float sweepAngle, String text, Canvas canvas) {
        Path path = new Path();
        path.addArc(rect, startAngle, sweepAngle);
        float areaWidth = (float) (2 * mRadius * Math.PI / mCount);
        float textWidth = mTextPaint.measureText(text);
        float dx = (areaWidth - textWidth) / 2;//水平的偏移量
        float dy = mRadius / 6;//垂直偏移量为半径的一半
        canvas.drawTextOnPath(text, path, dx, dy, mTextPaint);
    }

    /**
     * 绘制图片
     *
     * @param startAngle
     * @param canvas
     */
    private void drawIcon(float startAngle, Canvas canvas,Bitmap bitmap) {
        int imgWidth = mRadius / 4;//图片的宽度为半径的四分之一

        //角度为起始角度加上每个区域角度的一半
        float angle = (float) ((startAngle + 360.0f / mCount / 2) * Math.PI / 180);
        int x = (int) (mRadius + mRadius / 2 * Math.cos(angle));
        int y = (int) (mRadius + mRadius / 2 * Math.sin(angle));

        //确定图片的位置 x,y 为图片的中心
        Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth / 2, y + imgWidth / 2);
        canvas.drawBitmap(bitmap,null,rect,null);
    }

}
