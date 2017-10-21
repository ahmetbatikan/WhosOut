package com.example.asuspc.whosout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button Button_sec;
    private Button Button_vis;
    private Button Button_his;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    // Example of a call to a native method
        Button_vis = (Button)findViewById(R.id.button_visitor);
        Button_sec = (Button)findViewById(R.id.button_security);
        Button_his = (Button)findViewById(R.id.button_history);

        Button_vis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, VisitorActivity.class);
                startActivity(i);
            }
        });

        Button_sec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SecurityActivity.class);
                startActivity(i);
            }
        });

        Button_sec.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(i);
            }
        });
    }
}
