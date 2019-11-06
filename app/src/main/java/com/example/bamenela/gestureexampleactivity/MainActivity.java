package com.example.bamenela.gestureexampleactivity;

import android.annotation.TargetApi;
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

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        TouchExample view = new TouchExample(this,this);
        setContentView(view);
    }
    
}

