package com.example.bamenela.gestureexampleactivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<String> images=getImagesPath(this);
    }
/*
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if (resultCode == Activity.RESULT_OK){
            if(requestCode == GALLERY_REQUEST_CODE){
                imageView.setImageURI(data.getData());
            }
        }
    } */


    public static ArrayList<String> getImagesPath(Activity activity) {
        Uri uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String[] projection = { MediaStore.MediaColumns.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        String ImagePath = null;

        int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        while (cursor.moveToNext()) {
            ImagePath = cursor.getString(column_index_data);
            listOfAllImages.add(ImagePath);
        }

        return listOfAllImages;
    }
}
