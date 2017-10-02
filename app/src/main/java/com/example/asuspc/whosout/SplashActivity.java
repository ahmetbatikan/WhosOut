package com.example.asuspc.whosout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by AhmetBatıkanUnal on 1.10.2017.
 */

public class SplashActivity extends AppCompatActivity {
    private ImageView img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        img = (ImageView)findViewById(R.id.splash_image);
    }

    @Override
    protected void onStart(){
        super.onStart();
        // TODO: Arrange activity invocation
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);


    }
}
