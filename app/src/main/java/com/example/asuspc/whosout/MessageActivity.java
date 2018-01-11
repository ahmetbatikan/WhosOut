package com.example.asuspc.whosout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AsusPc on 1.10.2017.
 */

public class MessageActivity extends AppCompatActivity {

    private Button Button_mes;
    private Button Button_check;
    private EditText editText_mes;
    private TextView sending_msg;
    private TextView receive_msg;
    boolean process_type;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        Button_mes = (Button)findViewById(R.id.button_message_sendmessage);
        Button_check = (Button)findViewById(R.id.temp_check_msg);
        editText_mes = (EditText) findViewById(R.id.editText_message);
        sending_msg = (TextView)findViewById(R.id.output_msg);
        receive_msg = (TextView)findViewById(R.id.input_msg);


        Button_mes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                process_type=true;
                sending_msg.setText(sending_msg.getText()+"\n* "+editText_mes.getText());
                new DatabaseTask().execute();
            }
        });

        Button_check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                process_type=false;
                new DatabaseTask().execute();
            }
        });
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {  //her 60 sn de bir bildirimGonder(); metodu çağırılır.
            @Override
            public void run() {
                process_type=false;
                new DatabaseTask().execute();
            }
        }, 0, 3000);
    }

    private class DatabaseTask extends AsyncTask<Void, Void, Void> {
        DatabaseHelper helper;
        String temp_txt="";
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                helper=new DatabaseHelper();
                helper.connectDatabase();
                if(process_type){
                    if(("" +editText_mes.getText())!="") {
                        helper.sendMessages("" + editText_mes.getText());
                    }
                }
                else{
                    temp_txt=helper.receiveMessages();
                }
            }
            catch (Exception e){
                AlertDialog alertDialog = new AlertDialog.Builder(MessageActivity.this).create();
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
                if(process_type){
                    editText_mes.setText("");
                }
                else{
                    receive_msg.setText(""+receive_msg.getText()+temp_txt);
                }

            } catch (Exception e) {
                AlertDialog alertDialog = new AlertDialog.Builder(MessageActivity.this).create();
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
