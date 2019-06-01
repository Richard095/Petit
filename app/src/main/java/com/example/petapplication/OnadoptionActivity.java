package com.example.petapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.Map;

public class OnadoptionActivity extends AppCompatActivity {

    PostAdapter postAdapter;
    ArrayList<Post> mypetsList = new ArrayList<>();
    RequestQueue requestQueue;
    RecyclerView  mypets_recycler;
    String pet_status,image;
    int petpostId=0;
    String  pet_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onadoption);
        final Toolbar toolbar = findViewById(R.id.onadoption_activity_toolbar);
        setSupportActionBar(toolbar);
          mypets_recycler =findViewById(R.id.mypets_recycler);
        requestQueue = Volley.newRequestQueue(this);



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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        mypets_recycler.setLayoutManager(gridLayoutManager);
        postAdapter =new PostAdapter(OnadoptionActivity.this,mypetsList);


        get_my_posts();

        postAdapter.setOnPostClickListener(new PostAdapter.OnPostClickListener() {
            @Override
            public void onPostClik(Post post) {

                SharedPreferences preferences = getSharedPreferences("SESION_USER", Context.MODE_PRIVATE);
                int USERID= preferences.getInt("UserId",0);

                int weight;
                String petweight,vaccinated,dewormed,healthy,sterilized,descriptions;

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

                detailactivity.putExtra("USERID",USERID);

                startActivity(detailactivity);

            }

        });


    }
    private void get_my_posts(){
        String url = Utils.Constants.URL+"/create_post.php";

        SharedPreferences preferences = getSharedPreferences("SESION_USER", Context.MODE_PRIVATE);
        final int userId = preferences.getInt("UserId",0);


        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSE: ",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("mypets");

                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object=jsonArray.getJSONObject(i);

                                final String name,status,urlImage ,gender_pet,vaccinated,dewormed,healthy,sterilized,descriptions;
                                int pet_weight,petpostId;

                                petpostId = object.getInt("petpostId");
                                name = object.getString("petname");
                                status = object.getString("status");
                                urlImage = object.getString("urlThumb");

                                pet_weight = object.getInt("pet_weight");
                                vaccinated = object.getString("vaccinated");
                                dewormed = object.getString("dewormed");
                                healthy = object.getString("healthy");
                                sterilized = object.getString("sterilized");
                                descriptions = object.getString("descriptions");

                                if (status.equals("1")){ pet_status = "En adopción"; }else{pet_status="Adoptado";}

                                String nameImage =  urlImage.substring(urlImage.lastIndexOf('/') + 1);
                                image = Utils.Constants.URL+"/uploads/"+nameImage;

                                mypetsList.add(new Post(image,name,pet_status,0,petpostId,
                                        pet_weight,vaccinated,dewormed,healthy,sterilized,descriptions,0));
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        mypets_recycler.setAdapter(postAdapter);
                        postAdapter.notifyDataSetChanged();

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
                params.put("userId",String.valueOf(userId));
                return params;
            }
        };
        requestQueue.add(request);
    }

}
