package com.example.bamenela.gestureexampleactivity;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;


public class TouchExample extends View {
    private static final int MAX_POINTERS = 5;
    private float mScale = 1f;
    private GestureDetector mGestureDetector;
    private ScaleGestureDetector mScaleGestureDetector;

    private Pointer[] mPointers = new Pointer[MAX_POINTERS];
    private Paint mPaint;
    private float mFontSize;
    private Context context;
    private static ArrayList<String> listOfAllImages;
    private static ArrayList<Bitmap> listOfAllBitmaps;

    class Pointer {
        float x = 0;
        float y = 0;
        int index = -1;
        int id = -1;
    }

    public TouchExample(Context context,Activity activity) {
        super(context);
        for (int i = 0; i<MAX_POINTERS; i++) {
            mPointers[i] = new Pointer();
        }

        mFontSize = 16 * getResources().getDisplayMetrics().density;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(mFontSize);

        mGestureDetector = new GestureDetector(context, new ZoomGesture());
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGesture());
        getImagesPath(activity);
        parseImage();
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

        int posX=0,posY=0;
        BitmapDrawable drawable;
        Iterator it = listOfAllBitmaps.iterator();
        while (it.hasNext()){
            drawable=new BitmapDrawable((Bitmap) it.next());
            System.out.println(drawable.getBitmap());
            drawable.setBounds(posX,posY,posX+200,200+posY);
            drawable.draw(canvas);
            posX+=200;
            if(posX>=400){
                posX=0;
                posY+=200;
            }
        }
//1: draw(0,0,200,200)
        //2: draw(200,0,400,200)
        //3: draw(0,200,200,400)
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


    public void parseImage()
    {
        Iterator list = listOfAllImages.iterator();
        ArrayList<Bitmap> myBitmaps=new ArrayList<Bitmap>();
        while(list.hasNext()){
            File imgFile = new File(list.next().toString());
            if(imgFile.exists()){
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                Bitmap myBitmap=BitmapFactory.decodeFile(imgFile.getAbsolutePath(),options);
                myBitmaps.add(myBitmap);
            }
        }
        listOfAllBitmaps=myBitmaps;

        System.out.println(myBitmaps);
    }

    public void changePhotosScale()
    {

    }


}
