package com.example.asuspc.whosout;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

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
                Intent intent = new Intent(getApplicationContext(),NotificationService.class);
                startService(intent);
                 /*try {
                        new DatabaseTask().execute();
                        Thread.sleep(4000);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    */


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
                    files[i].delete();
                }
            }
        }
        return( path.delete() );
    }

    private void addNotification(int[] arr) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.splash_img)
                        .setContentTitle("Check the app for comers!")
                        .setContentText("Security: " + arr[2] + ", Visitor: " + arr[1]);

        Intent notificationIntent;
        if(arr[2]>=arr[1]){
            notificationIntent= new Intent(this, SecurityActivity.class);
        }
        else{
            notificationIntent= new Intent(this, VisitorActivity.class);
        }
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    private class DatabaseTask extends AsyncTask<Void, Void, Void> {
        DatabaseHelper helper;
        int[] arr;
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                helper = new DatabaseHelper();
                helper.connectDatabase();
                //while (true) {
                    arr=helper.checkNewSecurity();
                    Log.d("splash", "inside");
                    //TODO: check the condition in the splash!!
                    //Thread.sleep(2000);


                //}
            }
            catch (Exception e) {
                AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(e.getMessage());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            if (arr[0]>0) {
                addNotification(arr);
            }
        }
    }


}
