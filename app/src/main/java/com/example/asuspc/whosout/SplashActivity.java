package com.example.asuspc.whosout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

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
                File root = new File("/sdcard/Images_Whosout");
                if (root.exists()) {
                    deleteDirectory(root);//root.mkdirs(); // this will create folder.
                }
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 1000);


    }
    public static boolean deleteDirectory(File path) {
        if( path.exists() ) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }
            for(int i=0; i<files.length; i++) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                }
                else {
                    //    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }
}
