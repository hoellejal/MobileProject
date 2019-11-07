package com.example.tpactivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Creates a new view with the context of the application and the activity */
        TouchExample view = null;
        try {
            view = new TouchExample(this.getBaseContext(),this);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        /* Sets this view as content view */
        setContentView(view);
    }
    
}
