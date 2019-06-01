package com.example.petapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.petapplication.adapters.PostAdapter;
import com.example.petapplication.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    RequestQueue requestQueue;
    PostAdapter postAdapter;
    ArrayList<Post> postList = new ArrayList<>();

    String pet_status="";
    String image;
    String nameImage;
    int petpostId=0;
    String pet_name;
    int pet_weight=0;
    Toolbar toolbar;
    NavigationView navigationView;
    ImageView logo;
    MenuItem  login_item;
    Button btn_show_map;
    boolean START_SESION = false;


    final Handler handler = new Handler();
    Runnable runnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo = findViewById(R.id.iv_logo);
        toolbar = findViewById(R.id.main_activity_toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.maDrawerLayout);
        btn_show_map = findViewById(R.id.btn_show_map);

        if (getSupportActionBar()  != null){ getSupportActionBar().setDisplayShowTitleEnabled(false);}
        requestQueue = Volley.newRequestQueue(this);
        postAdapter =new PostAdapter(MainActivity.this,postList);


        boolean firstRun = getSharedPreferences("preferences", MODE_PRIVATE).getBoolean("firstrun", true);
        if(firstRun){
            getSharedPreferences("preferences", MODE_PRIVATE).edit().putBoolean("firstrun", false).apply();
            //Creando share preference para verificar sesion
            SharedPreferences preferences = getSharedPreferences("SESION_USER", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("UserId",0);
            editor.apply();
        }else{
            //Toast.makeText(this,"Segunda vista",Toast.LENGTH_SHORT).show();
        }

       //Creating navigationDrawer
        send_view();
        navigationView = findViewById(R.id.maNavegationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                int id = item.getItemId();
                switch(id)
                {
                    case R.id.account:
                        Intent account = new Intent(getApplication(),AccountActivity.class);
                        startActivity(account);
                        break;
                    case R.id.in_adoption:
                        Intent OnadoptionActivity = new Intent(MainActivity.this,OnadoptionActivity.class);
                        startActivity(OnadoptionActivity);
                        break;
                    case R.id.give_in_adoption:
                        Intent give_On_Adoption = new Intent(MainActivity.this,GiveAdoptitionActivity.class);
                        startActivity(give_On_Adoption);
                        break;

                    case  R.id.recomended:
                        Intent recomended = new Intent(MainActivity.this,RecomendActivity.class);
                        startActivity(recomended);
                        break;
                    default:
                        return true;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });

        //verifying internet connection
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
                                get_pets();
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
                                get_pets();
                            }
                        });
                    }
                };
                thread.start();
            }
        } else {
            Toast.makeText(getApplication(),"Comprueba tu conexion a internet",Toast.LENGTH_SHORT).show();
        }

        //Onclick method
        postAdapter.setOnPostClickListener(new PostAdapter.OnPostClickListener() {
            @Override
            public void onPostClik(Post post) {


                String petweight,vaccinated,dewormed,healthy,sterilized,descriptions;
                int weight;

                petpostId = post.getPetpostId();
                pet_name = post.getPetName();

                weight = post.getWeight();
                vaccinated = post.getVaccinated();
                dewormed = post.getDewormed();
                healthy  = post.getHealthy();
                sterilized = post.getSterilized();
                descriptions = post.getDescriptions();

                petweight = String.valueOf(weight);

                Intent detailactivity = new Intent(getApplication(),PetdetailActivity.class);
                detailactivity.putExtra("petpostId",petpostId);
                detailactivity.putExtra("pet_name",pet_name);
                detailactivity.putExtra("petweight",petweight);
                detailactivity.putExtra("vaccinated",vaccinated);
                detailactivity.putExtra("dewormed",dewormed);
                detailactivity.putExtra("healthy",healthy);
                detailactivity.putExtra("sterilized",sterilized);
                detailactivity.putExtra("descriptions",descriptions);
                detailactivity.putExtra("fromUser",post.getFromUserId());
                startActivity(detailactivity);
            }
        });

        btn_show_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_maps_activity = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(open_maps_activity);
            }
        });

        //handler
        runnable = new Runnable() {
            @Override
            public void run() {
                get_pets();
                handler.postDelayed(this, 4000);
            }
        };

        //Start
        handler.postDelayed(runnable, 4000);


    }//finish OnCreate


    /**Menu Item Toolbar*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(toggle.onOptionsItemSelected(item))
            return true;

        if (item.getItemId() == R.id.favorites){
            Intent saveds = new Intent(MainActivity.this,SavedsActivity.class);
            startActivity(saveds);
            return true;
        }
        if (item.getItemId() == R.id.login){
            Intent intent = new Intent(getApplication(), LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    /**Action for recyclerDrawer*/
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            finish();
        }
        handler.removeCallbacks(runnable);
    }
    /**Menu over toolbar*/
    @Override
    public boolean onCreateOptionsMenu(Menu toolbarMenu) {
        getMenuInflater().inflate(R.menu.menu_over_toolbar, toolbarMenu);

        login_item = toolbarMenu.findItem(R.id.login);

        if (START_SESION){
                login_item.setVisible(false);
        }else{
            login_item.setVisible(true);
        }
        return true;
    }
    public void get_pets(){

        String url = Utils.Constants.URL+"/create_post.php";

        final RecyclerView postRecycler  =findViewById(R.id.petList_recycler);
        final StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        postList.clear();
                        postAdapter.notifyDataSetChanged();
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("petposts");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                final String name,status,urlImage,vaccinated,dewormed,healthy,sterilized,descriptions;
                                int fromUserId;
                                petpostId = object.getInt("petpostId");
                                name = object.getString("petname");
                                status = object.getString("status");
                                urlImage = object.getString("urlThumb");
                                pet_weight = object.getInt("pet_weight");
                                vaccinated = object.getString("vaccinated");
                                dewormed = object.optString("dewormed");
                                healthy = object.getString("healthy");
                                sterilized = object.getString("sterilized");
                                descriptions = object.getString("descriptions");
                                fromUserId  = object.getInt("postedUserId");
                                if (status.equals("1")){pet_status = "En adopción";}else{pet_status="Adoptado";}

                                nameImage =  urlImage.substring(urlImage.lastIndexOf('/') + 1);
                                image =  Utils.Constants.URL+"/uploads/"+nameImage;
                                postList.add(new Post(image,name,pet_status,0,petpostId, pet_weight,vaccinated,dewormed,healthy,sterilized,descriptions,fromUserId));

                            }
                        }catch (JSONException e){
                               e.printStackTrace();
                        }

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,2);
                        postRecycler.setLayoutManager(gridLayoutManager);
                        Collections.reverse(postList);
                        postRecycler.setAdapter(postAdapter);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en la conexion", Toast.LENGTH_LONG).show();
                    }
                }
        ) ;
        requestQueue.add(request);
    }
    @Override
    protected void onResume() {
        send_view();

        Log.d("COUNT",""+postAdapter.getItemCount()+" " );


        super.onResume();
    }


    public void send_view(){
        SharedPreferences preferences = getSharedPreferences("SESION_USER", Context.MODE_PRIVATE);
        int userId= preferences.getInt("UserId",0);

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout,R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (userId == 0 ){ // not logged in
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            if (getActionBar() != null){
                getActionBar().setDisplayUseLogoEnabled(true);
                getActionBar().setDisplayShowHomeEnabled(true);
            }
            toolbar.setNavigationIcon(null);
            logo.setVisibility(View.VISIBLE);

        }else{// logged in
            if (getSupportActionBar() != null) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_sort_black_24dp);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            }
            logo.setVisibility(View.GONE);

            START_SESION = true;


        }

    }

}
