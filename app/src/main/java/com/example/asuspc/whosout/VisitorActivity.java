package com.example.asuspc.whosout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

/**
 * Created by AsusPc on 1.10.2017.
 */

public class VisitorActivity extends AppCompatActivity {
    private Button but_refresh_image;
    private Button but_custom_picture;
    private Button but_visitor_send_message;
    private ImageView img_visitor;
    private TextView visitor_num;
    boolean fileAvaiable=false;
    int index=1;
    int totalVisitor=0;
    private float x1,x2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_visitor);

        but_refresh_image = (Button)findViewById(R.id.button_refresh_image);
        but_custom_picture = (Button)findViewById(R.id.button_custom_picture);
        but_visitor_send_message = (Button)findViewById(R.id.button_send_message);
        visitor_num = (TextView)findViewById(R.id.textView_visitorNum);

        new DatabaseTask().execute();
        but_refresh_image.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new DatabaseTask().execute();
            }
        });

        but_custom_picture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
;               Intent i = new Intent(VisitorActivity.this, CustomImageActivity.class);
                startActivity(i);
            }
        });

        but_visitor_send_message.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(VisitorActivity.this, MessageActivity.class);
                startActivity(i);
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
                            visitor_num.setText("Total detection: "+totalVisitor+"     Current image: " +index);
                            Bitmap myBitmap = BitmapFactory.decodeFile(root.getAbsolutePath());

                            img_visitor = (ImageView)findViewById(R.id.imageView_visitor);
                            img_visitor.setImageBitmap(myBitmap);
                            fileAvaiable=true;
                        }
                    } catch (Exception e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(VisitorActivity.this).create();
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


    private class DatabaseTask extends AsyncTask<Void, Void, Void> {
        DatabaseHelper helper;

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                helper=new DatabaseHelper();
                helper.connectDatabase();
                helper.getImageNotSeen();
            }
            catch (Exception e){
                AlertDialog alertDialog = new AlertDialog.Builder(VisitorActivity.this).create();
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
            try {
                File root = new File("/sdcard/Images_Whosout/image_test1.jpg");
                if(root.exists()){
                    File temp=new File("/sdcard/Images_Whosout");
                    File[] temp_files = temp.listFiles();
                    totalVisitor=temp_files.length;
                    visitor_num.setText("Total detection: "+totalVisitor+"     Current image: " +1);
                    Bitmap myBitmap = BitmapFactory.decodeFile(root.getAbsolutePath());

                    img_visitor = (ImageView)findViewById(R.id.imageView_visitor);
                    img_visitor.setImageBitmap(myBitmap);
                    fileAvaiable=true;
                    index=1;
                }
            } catch (Exception e) {
                AlertDialog alertDialog = new AlertDialog.Builder(VisitorActivity.this).create();
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
    }

}
