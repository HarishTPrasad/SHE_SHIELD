package com.kalios.sheshield;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class splashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(1000);//1000 = 1sec
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(splashScreen.this, dashboard.class);
                    startActivity(intent);
                    finish();
                }
            }
        };thread.start();
    }
}