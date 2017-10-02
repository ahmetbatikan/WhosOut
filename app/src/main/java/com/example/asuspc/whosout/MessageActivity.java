package com.example.asuspc.whosout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by AsusPc on 1.10.2017.
 */

public class MessageActivity extends AppCompatActivity {

    private  Button Button_mes;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Example of a call to a native method
        Button_mes = (Button)findViewById(R.id.button_message_sendmessage);

        Button_mes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });
    }
}
