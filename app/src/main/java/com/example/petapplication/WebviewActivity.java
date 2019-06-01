package com.example.petapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

public class WebviewActivity extends AppCompatActivity {

    String LOAD_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        WebView web_viewId = findViewById(R.id.web_viewId);
        Bundle extras = this.getIntent().getExtras();
        if (extras != null){
             LOAD_URL= extras.getString("loadUrl");
        }
        web_viewId.loadUrl(LOAD_URL);
    }

}
