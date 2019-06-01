package com.example.petapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class RecomendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomend);
        //sending toolbar
        final Toolbar toolbar = findViewById(R.id.recomend_activity_toolbar);
        Button btnloadUrl1 = findViewById(R.id.btnloadUrl1);
        Button btnloadUrl2 = findViewById(R.id.btnloadUrl2);
        Button btnloadUrl3 = findViewById(R.id.btnloadUrl3);
        Button btnloadUrl4 = findViewById(R.id.btnloadUrl4);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });

        }



        btnloadUrl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://mascotas.facilisimo.com/sintomas-de-que-tu-perro-se-siente-solo_2337457.html";
                Intent webviewIntent = new Intent(getApplication(),WebviewActivity.class);
                webviewIntent.putExtra("loadUrl",url);
                startActivity(webviewIntent);
            }
        });

        btnloadUrl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://mascotas.facilisimo.com/4-beneficios-de-jugar-con-tu-perro_2274467.html";
                Intent webviewIntent = new Intent(getApplication(),WebviewActivity.class);
                webviewIntent.putExtra("loadUrl",url);
                startActivity(webviewIntent);
            }
        });


        btnloadUrl3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://mascotas.facilisimo.com/que-necesita-mi-gato-para-vivir-en-buenas-condiciones_2255020.html";

                Intent webviewIntent = new Intent(getApplication(),WebviewActivity.class);
                webviewIntent.putExtra("loadUrl",url);
                startActivity(webviewIntent);
            }
        });


        btnloadUrl4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "www.google.com";
                Intent webviewIntent = new Intent(getApplication(),WebviewActivity.class);
                webviewIntent.putExtra("loadUrl",url);
                startActivity(webviewIntent);
            }
        });



    }//finish onCreate
}
