package com.mete.template;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.HashMap;

public class PaintView extends View {

    public static final float TOUCH_TOLERANCE = 0;

    public static int brushSize;
    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Paint paintScreen;
    private Paint paintLine;
    private HashMap<Integer, Path> pathMap;
    private HashMap<Integer, Point> previousPointMap;

    void init() {
        paintScreen = new Paint();
        paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.BLACK);
        paintLine.setStrokeCap(Paint.Cap.ROUND);
        paintLine.setStrokeWidth(7);
        paintLine.setStyle(Paint.Style.STROKE);

        pathMap = new HashMap<>();
        previousPointMap = new HashMap<>();

    }

    public PaintView(Context context) {
        super(context);
        init();
    }

    public PaintView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        bitmap = Bitmap.createBitmap(getWidth(),getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap,0,0,paintScreen);

        for (Integer key:pathMap.keySet()) {
            canvas.drawPath(pathMap.get(key),paintLine);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getActionMasked(); //event type
        int actionIndex = event.getActionIndex(); //location of the finger

        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_UP) {
            touchStarted(event.getX(actionIndex),
                    event.getY(actionIndex),
                    event.getPointerId(actionIndex));
        }else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
            touchEnded(event.getPointerId(actionIndex));
        }else{
            touchMoved(event);
        }

        invalidate(); //redraw the screen

        return true;
    }

    private void touchMoved(MotionEvent event) {

        for (int i = 0 ; i < event.getPointerCount() ; i++) {
            int pointerId = event.getPointerId(i);
            int pointerIndex = event.findPointerIndex(pointerId);

            if (pathMap.containsKey(pointerId)) {
                float newX = event.getX(pointerId);
                float newY = event.getY(pointerId);

                Path path = pathMap.get(pointerId);
                Point point = previousPointMap.get(pointerId);

                //calculate the distance
                float deltaX = Math.abs(newX - point.x);
                float deltaY = Math.abs(newY - point.y);

                //if the disctance is enough
                if(deltaX >= TOUCH_TOLERANCE || deltaY >= TOUCH_TOLERANCE) {
                    //move the path to new location
                    path.quadTo(point.x , point.y ,
                            (newX + point.x) / 2 ,
                            (newY + point.y ) / 2);

                    //store the new results
                    point.x = (int)newX;
                    point.y = (int)newY;

                }
            }
        }

    }

    public void clear( ) {
        pathMap.clear(); // removers all the paths
        previousPointMap.clear();
        bitmap.eraseColor(Color.WHITE);
        invalidate();//refresh the screen
    }

    private void touchEnded(int pointerId) {
        Path path = pathMap.get(pointerId);//get the corresponding path
        bitmapCanvas.drawPath(path,paintLine); // draw bitmap canvas
    }

    private void touchStarted(float x, float y, int pointerId) {
        Path path; //store the path for given touch
        Point point; //last point touched

        if (pathMap.containsKey(pointerId)){
            path = pathMap.get(pointerId);
            point=previousPointMap.get(pointerId);
        }else{
            path = new Path();
            pathMap.put(pointerId,path);
            point = new Point();
            previousPointMap.put(pointerId,point);
        }

        path.moveTo(x,y);
        point.x = (int) x;
        point.y = (int) y;
    }

}
