package com.example.petapplication;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.petapplication.SlideImages.IntroViewPagerAdapter;
import com.example.petapplication.SlideImages.ScreenItem;
import com.example.petapplication.model.FavdbHelper;
import com.example.petapplication.model.FavoritesContract;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.github.javiersantos.materialstyleddialogs.enums.Duration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static android.Manifest.permission.CALL_PHONE;

public class PetdetailActivity extends AppCompatActivity {


    public static final int CALL_PHONE_REQUEST_CODE =0;

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    List<ScreenItem> mList;

    RequestQueue requestQueue;

    String PHONE_NUMBER,USER,urlImage,image;
    int petpostId=0, USERID=0;
    TextView weight,vaccinated,dewormed,healthy,sterilized,descriptions;
    String petname,det_weight,det_vaccinated,det_dewormed,det_healthy,det_sterilized,det_descriptions;
    String contact_fullname,contact_phone,contact_email ,contact_address,contact_description;


    ImageView add_to_favorites;
    ArrayList<Integer> favs = new ArrayList<>();
    ArrayList<Integer> arrayFavs = new ArrayList<>();
    TextView text_verify;
    Switch on_off;
    Button adopt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petdetail);
        requestQueue = Volley.newRequestQueue(this);
        tabIndicator = findViewById(R.id.tab_indicator);

        mList = new ArrayList<>();
        screenPager =findViewById(R.id.screen_viewpager);
        weight = findViewById(R.id.weight);
        vaccinated = findViewById(R.id.vaccinated);
        dewormed = findViewById(R.id.dewormed);
        healthy = findViewById(R.id.healthy);
        sterilized = findViewById(R.id.sterilized);
        descriptions = findViewById(R.id.descriptions);
        adopt = findViewById(R.id.adopt);
        add_to_favorites = findViewById(R.id.add_to_favorites);
        text_verify = findViewById(R.id.text_verify);
        on_off = findViewById(R.id.on_off);
        View divider = findViewById(R.id.divider);



        //getting  data from MainActivity
        Bundle extras = this.getIntent().getExtras();
        if (extras != null){
            String YES = "Si", NO = "No";
            petpostId = extras.getInt("petpostId");
            petname = extras.getString("pet_name");
            det_weight = extras.getString("petweight");
            det_vaccinated = extras.getString("vaccinated");
            det_dewormed = extras.getString("dewormed");
            det_healthy = extras.getString("healthy");
            det_sterilized = extras.getString("sterilized");
            det_descriptions = extras.getString("descriptions");


            USER = Integer.toString(extras.getInt("fromUser"));
            USERID = extras.getInt("USERID");

            if (det_vaccinated.equals("1")){det_vaccinated=YES;}else if(det_vaccinated.equals("0")){det_vaccinated=NO;}
            if (det_dewormed.equals("1")){det_dewormed=YES;}else if(det_dewormed.equals("0")){det_dewormed=NO;}
            if (det_healthy.equals("1")){det_healthy=YES;}else if(det_healthy.equals("0")){det_healthy=NO;}
            if (det_sterilized.equals("1")){det_sterilized=YES;}else if(det_sterilized.equals("0")){det_sterilized=NO;}
            showDetails();

            if (USERID != 0){
                adopt.setVisibility(View.GONE);
                add_to_favorites.setVisibility(View.GONE);
                text_verify.setVisibility(View.VISIBLE);
                on_off.setVisibility(View.VISIBLE);
                divider.setVisibility(View.VISIBLE);
            }

        }
        //sending toolbar
        final Toolbar toolbar = findViewById(R.id.detail_toolbar);
        toolbar.setTitle(petname);
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

        get_favorites();
        for (int i = 0;i<arrayFavs.size() ;i++){
            if (arrayFavs.get(i) == petpostId){
                add_to_favorites.setImageResource(R.drawable.ic_bookmark_24dp);
            }
        }


        //Verifying internet status
        ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                get_images();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                get_images();

            }
        } else {
            Toast.makeText(getApplication(),"Comprueba tu conexion a internet",Toast.LENGTH_SHORT).show();
        }


        //Adopt pet now
        adopt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new MaterialStyledDialog.Builder(PetdetailActivity.this)
                        .setTitle("Bien hecho")
                        .setIcon(R.drawable.ic_done_white)
                        .setDescription("¡Estas apunto de salvar a una mascota!")
                        .withDialogAnimation(true)
                        .setPositiveText("Confirmar")
                        .setNegativeText("Cancelar")
                        .withDialogAnimation(true, Duration.FAST)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick( MaterialDialog dialog, DialogAction which) {

                                showCustomDialog();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Toast.makeText(getApplication(),"Vale puedes seguir buscando!",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        //Adding to favorites
        add_to_favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                get_favorites();
                if (arrayFavs.isEmpty()){
                    add_favorites(petpostId);
                    add_to_favorites.setImageResource(R.drawable.ic_bookmark_24dp);
                }else {
                   get_favorites();
                    for (int i = 0; i < arrayFavs.size(); i++) {
                        if (arrayFavs.get(i) == petpostId) {
                            delete(petpostId);
                            add_to_favorites.setImageResource(R.drawable.ic_bookmark_border_blue_24dp);
                        } else if (arrayFavs.get(i) != petpostId) {
                            add_favorites(petpostId);
                            add_to_favorites.setImageResource(R.drawable.ic_bookmark_24dp);
                        }
                    }
                }
                get_favorites();
            }
        });
        get_user_contact();

    }//finish onCreate

    public void showDetails(){
        weight.setText(det_weight+" AÑos");
        vaccinated.setText(det_vaccinated);
        dewormed.setText(det_dewormed);
        healthy.setText(det_healthy);
        sterilized.setText(det_sterilized);
        descriptions.setText(det_descriptions);
    }

    public void get_images(){
        String url = Utils.Constants.URL+"/images.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            try {
                                JSONObject jsonObject =new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("images");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject object=jsonArray.getJSONObject(i);

                                    urlImage = object.getString("urlThumb");
                                    image =  urlImage.substring(urlImage.lastIndexOf('/') + 1);
                                    image =  Utils.Constants.URL+"/uploads/"+image;
                                    mList.add(new ScreenItem(image));

                                }

                            }catch (JSONException e){
                                e.printStackTrace();
                            }

                            introViewPagerAdapter = new IntroViewPagerAdapter(getApplicationContext(),mList);
                            screenPager.setAdapter(introViewPagerAdapter);
                            tabIndicator.setupWithViewPager(screenPager);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error al en la conexion", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fromPostId",String.valueOf(petpostId));
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void showCustomDialog() {
        SharedPreferences preferences = getSharedPreferences("SESION_USER", Context.MODE_PRIVATE);
        int userId = preferences.getInt("UserId",0);

        ViewGroup viewGroup = findViewById(android.R.id.content);
        final FloatingActionButton call ;


        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.cuatom_dialog, viewGroup, false);
        call = dialogView.findViewById(R.id.contact_call);
        TextView dialog_pet_own = dialogView.findViewById(R.id.dialog_pet_own);
        TextView dialog_email = dialogView.findViewById(R.id.contact_email);
        TextView dialog_address = dialogView.findViewById(R.id.contact_address);
        TextView dialog_about_me = dialogView.findViewById(R.id.contact_about_me);
        dialog_pet_own.setText(contact_fullname);
        dialog_email.setText(contact_email);
        dialog_address.setText(contact_address);
        dialog_about_me.setText(contact_description);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();

        if (userId == 0 ){
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
        }else{
            alertDialog.show();
        }


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PetdetailActivity.this);
                builder.setMessage("Estas apunto de iniciar una llamada")
                        .setTitle("Confirme su llamada");

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Call();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Vale te entendemos", Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    //Methods for start calls
    public void Call(){
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+PHONE_NUMBER));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
                startActivity(intent);
            }else{
                final String[] permissions = new String[]{CALL_PHONE};
                requestPermissions(permissions,CALL_PHONE_REQUEST_CODE);
            }
        }else{
            startActivity(intent);
        }
    }

    //Verifying call permissions
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == CALL_PHONE_REQUEST_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Call();
            }else if(shouldShowRequestPermissionRationale(CALL_PHONE)){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Hacer llamadas");
                builder.setMessage("Debes aceptar este permiso para hacer llamadas desde la app mascotas");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String[] permissions = new String[] {CALL_PHONE};
                        requestPermissions(permissions,CALL_PHONE_REQUEST_CODE);
                    }
                });
                builder.show();
            }
        }
    }

    //getting contact user
    private void get_user_contact(){
        String url = Utils.Constants.URL+"/";


        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("user");
                                JSONObject object=jsonArray.getJSONObject(0);
                                contact_fullname = object.optString("fullname");
                                contact_phone = object.getString("phone");
                                contact_email = object.getString("email");
                                contact_address = object.getString("addresses");
                                contact_description = object.getString("descriptions");
                                PHONE_NUMBER = contact_phone;
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en la conexion", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userId",String.valueOf(USER));
                return params;
            }
        };
        requestQueue.add(request);
    }

    public void add_favorites(int petpostId){
        FavdbHelper favdbHelper = new FavdbHelper(getApplicationContext());
        SQLiteDatabase database  = favdbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoritesContract.FavoritesColumns.FAV,petpostId);
        database.insert(FavoritesContract.FavoritesColumns.TABLE_NAME,null,values);
        database.close();
    }

    public void get_favorites(){
        FavdbHelper favdbHelper = new FavdbHelper(getApplicationContext());
        SQLiteDatabase database  = favdbHelper.getReadableDatabase();
        Cursor cursor = database.query(FavoritesContract.FavoritesColumns.TABLE_NAME,null,null,null,null,null,null,null);

        while (cursor.moveToNext()){
            favs.add(cursor.getInt(1));
        }
        cursor.close();
        cursor.close();
        Set<Integer> set = new LinkedHashSet<>(favs);
        arrayFavs = new ArrayList<>(set);
    }

    public void delete (int petpostId){
        FavdbHelper favdbHelper =  new FavdbHelper(getApplicationContext());
        favdbHelper.delete(petpostId);
    }

}
