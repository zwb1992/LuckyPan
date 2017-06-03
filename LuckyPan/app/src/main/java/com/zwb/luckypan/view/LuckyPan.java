package com.zwb.luckypan.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
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
    private Bitmap[] bitmaps;
    private boolean isInit = false;
    private Paint paint;
    private Rect rect;

    public LuckyPan(Context context) {
        this(context, null);
    }

    public LuckyPan(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyPan(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
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
        rect = new Rect(0, 0, getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        isInit = true;
        bitmaps = new Bitmap[imgs.length];
        for (int i = 0; i < bitmaps.length; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), imgs[0]);
        }
        draw();
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
            canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, getMeasuredHeight() / 2 - 5 / 2.0f, paint);
//        canvas.drawBitmap(bitmaps[0], 0, 0, null);
        canvas.drawBitmap(bitmaps[0], null, rect, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }

    }

}
