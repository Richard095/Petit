package com.example.petapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.example.petapplication.MultiPartRequest.AppHelper;
import com.example.petapplication.MultiPartRequest.VolleyMultipartRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GiveAdoptitionActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    EditText pet_name,pet_weigth,pet_description;

    CheckBox checkbox_status1,checkbox_status2,checkbox_status3,checkbox_status4;
    ImageButton upload_photo;
    TextView images_name;
    Button send_data;
    Spinner spinner,spinner_gender;
    ProgressDialog progressDialog;
    String  userId;
    String pet_type,gender_pet;
    String vacunado="0",desparasitado="0",sano="0",esterilizado="0";
    String post_status="1";
    boolean SELECTED = false;

    String urlImage1,urlImage2,urlImage3;
    String PICTURE_NAME1,PICTURE_NAME2,PICTURE_NAME3;
    Drawable imageDrawable1,imageDrawable2,imageDrawable3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_adoptition);

        //References volley
        requestQueue = Volley.newRequestQueue(GiveAdoptitionActivity.this);

        //statements
        pet_name = findViewById(R.id.pet_name);
        pet_weigth = findViewById(R.id.pet_weigth);
        pet_description = findViewById(R.id.pet_description);
        checkbox_status1 = findViewById(R.id.checkbox_status1);
        checkbox_status2 = findViewById(R.id.checkbox_status2);
        checkbox_status3 = findViewById(R.id.checkbox_status_3);
        checkbox_status4 = findViewById(R.id.checkbox_status4);
        upload_photo = findViewById(R.id.upload_photo);
        send_data = findViewById(R.id.send_data);
        images_name = findViewById(R.id.images_name);
        spinner = findViewById(R.id.spinner_specie);
        spinner_gender = findViewById(R.id.spinner_gender);

        //preparing data spinners

        String[] letra = {"Perro", "Gato","Conejo"};
        spinner.setAdapter(new ArrayAdapter<>(GiveAdoptitionActivity.this, android.R.layout.simple_spinner_item, letra));
        String[] text = {"Macho", "Hembra"};
        spinner_gender.setAdapter(new ArrayAdapter<>(GiveAdoptitionActivity.this, android.R.layout.simple_spinner_item, text));

        //checks_status and spinner
        get_check_values();
        get_spinner_values();

       upload_photo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), AlbumSelectActivity.class);
               intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 3);
               startActivityForResult(intent, Constants.REQUEST_CODE);
           }
       });

       send_data.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               //Status internet
               if (SELECTED){
                   ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
                   NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                   if (activeNetwork != null) {
                       if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                           Thread thread = new Thread() {
                               @Override
                               public void run() {

                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           upload();
                                       }
                                   });
                               }
                           };
                           thread.start();

                       } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                           Thread thread = new Thread() {
                               @Override
                               public void run() {

                                   runOnUiThread(new Runnable() {
                                       @Override
                                       public void run() {
                                           upload();
                                       }
                                   });
                               }
                           };
                           thread.start();
                       }
                   } else {
                       Toast.makeText(GiveAdoptitionActivity.this, "Comprueba tu conexion a internet", Toast.LENGTH_SHORT).show();
                   }
               }else {
                   Toast.makeText(GiveAdoptitionActivity.this, "Completa el registro", Toast.LENGTH_SHORT).show();
               }

           }

       });


       //getting userId

        SharedPreferences preferences = getSharedPreferences("SESION_USER", Context.MODE_PRIVATE);
        int UserId = preferences.getInt("UserId",0);
        userId = Integer.toString(UserId);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            StringBuffer stringBuffer = new StringBuffer();
            for (int i = 0, l = images.size(); i < l; i++) {
                stringBuffer.append(images.get(i).path + "\n");
            }
            urlImage1 = images.get(0).path;
            urlImage2 = images.get(1).path;
            urlImage3 = images.get(2).path;
            PICTURE_NAME1 = urlImage1.substring(urlImage1.lastIndexOf('/') + 1);
            PICTURE_NAME2 = urlImage2.substring(urlImage2.lastIndexOf('/') + 1);
            PICTURE_NAME3 = urlImage3.substring(urlImage3.lastIndexOf('/') + 1);

            images_name.setText("Imagenes listas!");

            imageDrawable1 = Drawable.createFromPath(urlImage1);
            imageDrawable2 = Drawable.createFromPath(urlImage2);
            imageDrawable3 = Drawable.createFromPath(urlImage3);


            send_data.setEnabled(true);
            SELECTED = true;
        }
    }



    //getting values from checkbox
    public void get_check_values(){
        checkbox_status1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_status1.isChecked()){
                    vacunado = "1";
                }else{ vacunado = "0";  }
            }
        });
        checkbox_status2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_status2.isChecked()){
                    desparasitado = "1";
                }else{ desparasitado = "0"; }
            }
        });
        checkbox_status3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_status3.isChecked()){
                    sano = "1";
                }else{ sano = "0"; }
            }
        });
        checkbox_status4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkbox_status4.isChecked()){
                    esterilizado = "1";
                }else{ esterilizado = "0"; Toast.makeText(getApplicationContext(),"status: "+esterilizado,Toast.LENGTH_LONG).show(); }
            }
        });



    }
    //getting values from spinner
    public void get_spinner_values(){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent1, View view1, int pos, long id) {
                Object item1 = parent1.getItemAtPosition(pos);
                pet_type = item1.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                gender_pet = item.toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


    //Posting a new pet..
    private void upload() {

        final String petname = pet_name.getText().toString().trim();
        final String pet_weight = pet_weigth.getText().toString().trim();
        final String descriptions = pet_description.getText().toString().trim();


        progressDialog = new ProgressDialog(GiveAdoptitionActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String url = Utils.Constants.URL+"/create_post.php";
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                progressDialog.hide();
                String resultResponse = new String(response.data);
                Log.d("RESPONSERESULT",resultResponse);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("petname",petname);
                params.put("pet_type",pet_type);
                params.put("pet_weight", String.valueOf(pet_weight));
                params.put("gender_pet",gender_pet);
                params.put("vaccinated", vacunado);
                params.put("dewormed", desparasitado);
                params.put("healthy", sano);
                params.put("sterilized", esterilizado);
                params.put("descriptions",descriptions);
                params.put("post_status", post_status);
                params.put("postedUserId", String.valueOf(userId));
                return params;
            }
            @Override
            protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("image1", new DataPart(PICTURE_NAME1, AppHelper.getFileDataFromDrawable(getBaseContext(), imageDrawable1), "image/jpeg"));
                params.put("image2", new DataPart(PICTURE_NAME2, AppHelper.getFileDataFromDrawable(getBaseContext(), imageDrawable2), "image/jpeg"));
                params.put("image3", new DataPart(PICTURE_NAME3, AppHelper.getFileDataFromDrawable(getBaseContext(), imageDrawable3), "image/jpeg"));

                return params;
            }
        };
        requestQueue.add(multipartRequest);
    }
}
