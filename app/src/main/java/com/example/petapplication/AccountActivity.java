package com.example.petapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AccountActivity extends AppCompatActivity {

    TextView user_name_presentation;
    EditText user_name,user_phone,user_email,user_location;
    Button logout_in;
    RequestQueue requestQueue;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        user_name_presentation = findViewById(R.id.user_name_presentation);
        user_name = findViewById(R.id.user_name);
        user_phone = findViewById(R.id.user_phone);
        user_email = findViewById(R.id.user_email);
        user_location = findViewById(R.id.user_location);
        logout_in = findViewById(R.id.ready);

        requestQueue = Volley.newRequestQueue(this);


        SharedPreferences preferences = getSharedPreferences("SESION_USER", Context.MODE_PRIVATE);
        userId = preferences.getInt("UserId",0);
        get_user_data();

        //logout

        logout_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    public void get_user_data(){
        String url = Utils.Constants.URL+"/login.php"+"?userId="+userId;

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject =new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("user");
                            JSONObject object=jsonArray.getJSONObject(0);


                            user_name_presentation.setText(object.getString("fullname"));
                            user_name.setText(object.getString("fullname"));
                            user_phone.setText(object.getString("phone"));
                            user_email.setText(object.getString("email"));
                            user_location.setText(object.getString("addresses"));

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en la conexion ", Toast.LENGTH_LONG).show();
                    }
                }
        ) ;
        requestQueue.add(request);
    }


    public void logout(){
        SharedPreferences preferences = getSharedPreferences("SESION_USER", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("UserId",0);
        editor.apply();
        finish();
    }



}
