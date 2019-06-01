package com.example.petapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.petapplication.MultiPartRequest.VolleyMultipartRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    Animation LayoutAnimation;
    ProgressDialog progressDialog;
    EditText et_user;
    EditText et_password;
    Button login ;
    int userId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_user = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);
        login = findViewById(R.id.login);
        TextView create_account = findViewById(R.id.create_account);
        final RelativeLayout data_content_login = findViewById(R.id.data_content_login);
        final RelativeLayout content_register = findViewById(R.id.content_register);
        final Button register_button = findViewById(R.id.register_button);
        Spinner spinner = findViewById(R.id.spinner);


        String[] letra = {"User", "Asociacion"};
        spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, letra));

        LayoutAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_layout);

        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                data_content_login.setVisibility(View.GONE);
                content_register.setVisibility(View.VISIBLE);
                content_register.setAnimation(LayoutAnimation);
                content_register.animate().start();
                LayoutAnimation.start();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create_user();
                //*
                //  content_register.setVisibility(View.GONE);
                //                data_content_login.setVisibility(View.VISIBLE);
                //                data_content_login.setAnimation(LayoutAnimation);
                //                LayoutAnimation.start();
                // */

            }
        });

        requestQueue = Volley.newRequestQueue(this);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Comprobando conexion a internet
                ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        login();
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        login();
                    }
                } else {
                   Toast.makeText(getApplication(),"Comprueba tu conexion a internet",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void create_user() {
        String url = Utils.Constants.URL;

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Exito", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "Error al procesar los datos", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("user_type", "Asociacion");
                params.put("fullname", "OTRO NOMBRE");
                params.put("user", "otrouser");
                params.put("pass", "99798798");
                params.put("phone", "77765675675");
                params.put("email", "sss@shdjsdsds.com");
                params.put("addresses", "otra direccion");
                params.put("descriptions", "oiusfiosdfsfsdoifudsfsf sdfsdfkshdfk ");
                params.put("thumb", "No imagenskjhsfkjshdkjsfhjsk skjfsfhsjkfhskjfs ");
                return params;
            }
        };
        requestQueue.add(request);
    }



    private void login(){
        String url = Utils.Constants.URL+"/login.php";

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("logging in...");
        progressDialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        if (response.equals("false")){
                            Toast.makeText(getApplicationContext(), "Usuario invalido", Toast.LENGTH_SHORT).show();
                        }else{
                            try {
                                JSONObject jsonObject =new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("user");
                                JSONObject object=jsonArray.getJSONObject(0);
                                userId = object.getInt("userId");

                                //Saving userId
                                SharedPreferences preferences = getSharedPreferences("SESION_USER", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("UserId",userId);
                                editor.apply();

                                //Starting sesion
                                finish();
                                Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_SHORT).show();
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), "Error en la conexion", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user",et_user.getText().toString().trim());
                params.put("pass", et_password.getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(request);
    }


}