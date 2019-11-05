package com.example.bamenela.gestureexampleactivity;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.ArrayList;


public class TouchExample extends View {
    private static final int MAX_POINTERS = 5;
    private float mScale = 1f;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    private Pointer[] mPointers = new Pointer[MAX_POINTERS];
    private Paint mPaint;
    private float mFontSize;
    private static ArrayList<String> listOfAllImages;

    class Pointer {
        float x = 0;
        float y = 0;
        int index = -1;
        int id = -1;
    }

    public TouchExample(Context context) {
        super(context);
        for (int i = 0; i<MAX_POINTERS; i++) {
            mPointers[i] = new Pointer();
        }

        mFontSize = 16 * getResources().getDisplayMetrics().density;
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(mFontSize);

        mGestureDetector = new GestureDetector(context, new ZoomGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Pointer p : mPointers) {
            if (p.index != -1) {
                String text = "Index: " + p.index + " ID: " + p.id;
                canvas.drawText(text, p.x, p.y, mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);

        int pointerCount = Math.min(event.getPointerCount(), MAX_POINTERS);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
                // clear previous pointers
                for (int id = 0; id<MAX_POINTERS; id++)
                    mPointers[id].index = -1;

                // Now fill in the current pointers
                for (int i = 0; i<pointerCount; i++) {
                    int id = event.getPointerId(i);
                    Pointer pointer = mPointers[id];
                    pointer.index = i;
                    pointer.id = id;
                    pointer.x = event.getX(i);
                    pointer.y = event.getY(i);
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
                for (int i = 0; i<pointerCount; i++) {
                    int id = event.getPointerId(i);
                    mPointers[id].index = -1;
                }
                invalidate();
                break;
        }
        return true;
    }

    public class ZoomGesture extends GestureDetector.SimpleOnGestureListener {
        private boolean normal = true;

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            mScale = normal ? 3f : 1f;
            mPaint.setTextSize(mScale*mFontSize);
            normal = !normal;
            invalidate();
            return true;
        }
    }

    public class ScaleGesture extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScale *= detector.getScaleFactor();
            mPaint.setTextSize(mScale*mFontSize);
            invalidate();
            return true;
        }
    }

    public static void getImagesPath(Activity activity) {
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ArrayList<String> listImages = new ArrayList<String>();
        String[] projection = { MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        String ImagePath = null;

        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            ImagePath = cursor.getString(column_index_data);
            listImages.add(ImagePath);
        }

        listOfAllImages = listImages ;
    }


    public void parseImage(ArrayList<String> images)
    {

    }

    public void changePhotosScale()
    {

    }


}
