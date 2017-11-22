package com.example.asuspc.whosout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by AsusPc on 1.10.2017.
 */

public class CustomImageActivity extends AppCompatActivity {

    private Button but_begin_video;
    private WebView web_video;
    private EditText url_data;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custompicture);

        url_data =(EditText) findViewById(R.id.editText_custompicture_coordinate);
        but_begin_video = (Button)findViewById(R.id.button_custompicture_takepicture);
        web_video = (WebView)findViewById(R.id.web_id);

        but_begin_video.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WebSettings webSettings = web_video.getSettings();
                webSettings.setJavaScriptEnabled(true);
                web_video.loadUrl("https://www.mgm.gov.tr/tahmin/turkiye.aspx/");
            }
        });


    }



}

