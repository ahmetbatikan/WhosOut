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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;

/**
 * Created by Ayberk on 10.10.2017.
 */

public class SecurityActivity extends AppCompatActivity {
    TextView textView;
    Switch lock_switch;
    Switch lamp_switch;
    Button refreshButton;
    Button messageButton;


    private float x1,x2;
    private int lockValue;
    private int lampValue;
    private int alarmValue;
    private boolean checkDefault = true;
    private String lockCheck;
    private String str;
    private ImageView imgView_security;
    boolean fileAvaiable=false;
    private TextView textView_securityNum;
    int index=1;
    int totalPeople=0;
    boolean firstTime =true;
    int carrier=0;
    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);
        refreshButton= (Button) findViewById(R.id.button);
        messageButton= (Button) findViewById(R.id.security_message);
        lock_switch = (Switch) findViewById(R.id.lock_switch);
        lamp_switch = (Switch) findViewById(R.id.lamp_switch);
        textView_securityNum = (TextView)findViewById(R.id.textView_securityNum);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);

        File root = new File("/sdcard/Images_Whosout");
        if (root.exists()) {
            deleteDirectory(root);//root.mkdirs(); // this will create folder.
        }


        spinner.setVisibility(View.VISIBLE);
        new Send().execute();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkDefault=true;
                firstTime=true;
                spinner.setVisibility(View.VISIBLE);
                new Send().execute();

            }
        });
        messageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(SecurityActivity.this, MessageActivity.class);
                startActivity(i);
            }
        });

        lock_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(b)
                {
                    lockValue = 1;
                    spinner.setVisibility(View.VISIBLE);
                    new Send().execute();
                }
                else
                {
                    lockValue = 0;
                    spinner.setVisibility(View.VISIBLE);
                    new Send().execute();
                }
            }
        });
        lamp_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if(b)
                {
                    lampValue = 1;
                    spinner.setVisibility(View.VISIBLE);
                    new Send().execute();
                }
                else
                {
                    lampValue = 0;
                    spinner.setVisibility(View.VISIBLE);
                    new Send().execute();
                }
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
                            textView_securityNum.setText("Total detection: "+totalPeople+"     Current image: " +index);
                            Bitmap myBitmap = BitmapFactory.decodeFile(root.getAbsolutePath());

                            imgView_security = (ImageView)findViewById(R.id.imageView_security);
                            imgView_security.setImageBitmap(myBitmap);
                            fileAvaiable=true;
                        }
                    } catch (Exception e) {
                        AlertDialog alertDialog = new AlertDialog.Builder(SecurityActivity.this).create();
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

    private class Send extends AsyncTask<String,String,String>
    {
        String msg ="";

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String...strings)
        {

            try
            {
                DatabaseHelper helper = new DatabaseHelper();
                helper.connectDatabase();
                carrier=helper.checkButton();

                if(checkDefault)
                {
                    helper.getImageSecurity();
                    checkDefault=false;
                }
                else
                {
                    helper.updateDoorLock(lockValue);
                    helper.updateLamp(lampValue);
                }

            }
            catch (Exception e) {
                msg = "Connection goes wrong";
                e.printStackTrace();
                AlertDialog alertDialog = new AlertDialog.Builder(SecurityActivity.this).create();
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
            return msg;
        }

        @Override
        protected void onPostExecute(String msg)
        {

            spinner.setVisibility(View.INVISIBLE);

            try {
                File root = new File("/sdcard/Images_Whosout/image_test1.jpg");
                if(root.exists()){
                    File temp=new File("/sdcard/Images_Whosout");
                    File[] temp_files = temp.listFiles();
                    totalPeople=temp_files.length;
                    textView_securityNum.setText("Total detection: "+totalPeople+"     Current image: " +1);
                    Bitmap myBitmap = BitmapFactory.decodeFile(root.getAbsolutePath());

                    imgView_security = (ImageView)findViewById(R.id.imageView_security);
                    imgView_security.setImageBitmap(myBitmap);
                    fileAvaiable=true;
                    index=1;
                }

            } catch (Exception e) {
                AlertDialog alertDialog = new AlertDialog.Builder(SecurityActivity.this).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(e.getMessage());
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "FALSE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            if(firstTime){
                if(carrier%10==1){
                    lock_switch.setChecked(true);
                }
                if(carrier/10==1){
                    lamp_switch.setChecked(true);
                }
                firstTime=false;
                if(totalPeople==0){
                    AlertDialog alertDialog = new AlertDialog.Builder(SecurityActivity.this).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("There is no Security VIOLATION");
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



}
