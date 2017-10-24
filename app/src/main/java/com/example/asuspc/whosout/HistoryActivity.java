package com.example.asuspc.whosout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by AsusPc on 21.10.2017.
 */

public class HistoryActivity extends AppCompatActivity {

    private Button but_refresh_image_history;
    private ImageView img_visitor;
    boolean fileAvaiable=false;
    int index=1;
    int totalVisitor=0;
    private float x1,x2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history);

        but_refresh_image_history = (Button)findViewById(R.id.button_refresh_history);

        File root = new File("/sdcard/Images_Whosout");
        if (root.exists()) {
            deleteDirectory(root);//root.mkdirs(); // this will create folder.
        }

        new DatabaseTask().execute();

        but_refresh_image_history.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatabaseTask().execute();
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if(fileAvaiable) {
                    if (Math.abs(deltaX) > 150) {
                        if(x1>x2){
                            // Toast.makeText(this, "left2right swipe", Toast.LENGTH_SHORT).show ();
                            index++;
                        } else {
                            // consider as something else - a screen tap for example
                            index--;
                        }
                    }
                    try {
                        File root = new File("/sdcard/Images_Whosout/image_test"+index+".jpg");
                        if(root.exists()){
                         //   visitor_num.setText("Total detection: "+totalVisitor+"     Current image: " +index);
                            Bitmap myBitmap = BitmapFactory.decodeFile(root.getAbsolutePath());

                            img_visitor = (ImageView)findViewById(R.id.imageView_history);
                            img_visitor.setImageBitmap(myBitmap);
                            fileAvaiable=true;
                        }
                    } catch (Exception e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(HistoryActivity.this).create();
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
                }
                break;
        }
        return super.onTouchEvent(event);
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

    private class DatabaseTask extends AsyncTask<Void, Void, Void> {
        DatabaseHelper helper;

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                helper=new DatabaseHelper();
                helper.connectDatabase();
                helper.getImageAll();
            }
            catch (Exception e){
                AlertDialog alertDialog = new AlertDialog.Builder(HistoryActivity.this).create();
                alertDialog.setTitle("Alert1");
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
            try {
                File root = new File("/sdcard/Images_Whosout/image_test1.jpg");
                if(root.exists()){
                    File temp=new File("/sdcard/Images_Whosout");
                    File[] temp_files = temp.listFiles();
                    totalVisitor=temp_files.length;
                  //  visitor_num.setText("Total detection: "+totalVisitor+"     Current image: " +1);
                    Bitmap myBitmap = BitmapFactory.decodeFile(root.getAbsolutePath());

                    img_visitor = (ImageView)findViewById(R.id.imageView_history);
                    img_visitor.setImageBitmap(myBitmap);
                    fileAvaiable=true;
                    index=1;
                }
            } catch (Exception e) {
                AlertDialog alertDialog = new AlertDialog.Builder(HistoryActivity.this).create();
                alertDialog.setTitle("Alert2");
                alertDialog.setMessage(e.getMessage());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        }
    }

}
