package com.example.petapplication;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.petapplication.adapters.PostAdapter;
import com.example.petapplication.model.FavdbHelper;
import com.example.petapplication.model.FavoritesContract;
import com.example.petapplication.model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SavedsActivity extends AppCompatActivity {

    RequestQueue requestQueue;
    RecyclerView saveds_recyRecycler;
    PostAdapter postAdapter;
    ArrayList<Post> post_list_favorites = new ArrayList<>();
    String pet_status,image;

    ArrayList<Integer> arrayFaforites = new ArrayList<>();
    Set<Integer> set;
    ArrayList<Integer> arrayList ;

    ImageView show_image;
    TextView message_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saveds);


        final Toolbar toolbar = findViewById(R.id.saveds_activity_toolbar);
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

        requestQueue = Volley.newRequestQueue(SavedsActivity.this);
        saveds_recyRecycler=findViewById(R.id.pets_saveds_recycler);
        show_image = findViewById(R.id.show_image);
        message_title = findViewById(R.id.message_title);



        //Sending GridLayoutManager
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        saveds_recyRecycler.setLayoutManager(gridLayoutManager);
        postAdapter =new PostAdapter(SavedsActivity.this,post_list_favorites);


        postAdapter.setOnPostClickListener(new PostAdapter.OnPostClickListener() {
            @Override
            public void onPostClik(Post post) {
                Toast.makeText(SavedsActivity.this,post.getPetName(),Toast.LENGTH_LONG).show();
            }
        });

        // Favorites array
        get_favorites();
        status_favorites();


        get_favorites_pets();

    }//Finish onCreate

    public void get_favorites_pets(){
        String url = Utils.Constants.URL+"/create_post.php";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("RESPONSEMAIN",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("petposts");
                            for (int i=0;i<jsonArray.length();i++){
                                JSONObject object=jsonArray.getJSONObject(i);
                                final String nameImage;
                                final String name,status,urlImage ,gender_pet,vaccinated,dewormed,healthy,sterilized,descriptions;
                                int pet_weight,fromUserId=0,petpostId=0;

                                petpostId = object.getInt("petpostId");
                                name = object.getString("petname");
                                status = object.getString("status");
                                urlImage = object.getString("urlThumb");
                                pet_weight = object.getInt("pet_weight");
                                gender_pet = object.getString("gender_pet");
                                vaccinated = object.getString("vaccinated");
                                dewormed = object.getString("dewormed");
                                healthy = object.getString("healthy");
                                sterilized = object.getString("sterilized");
                                descriptions = object.getString("descriptions");
                                fromUserId  = object.getInt("postedUserId");

                                if (status.equals("1")){
                                    pet_status = "En adopción";
                                }else{pet_status="Adoptado";}
                                nameImage =  urlImage.substring(urlImage.lastIndexOf('/') + 1);
                                image =  Utils.Constants.URL+"/uploads/"+nameImage;

                                for (int j = 0; j < arrayList.size(); j++) {
                                    Log.d("FAVORITES",arrayList.get(j).toString());
                                    if (arrayList.get(j) == petpostId){
                                        post_list_favorites.add(new Post(image,name,pet_status,0,petpostId,
                                                pet_weight,vaccinated,dewormed,healthy,sterilized,descriptions,fromUserId));
                                    }
                                }
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        saveds_recyRecycler.setAdapter(postAdapter);
                        postAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(getApplicationContext(), "Error al procesar los datos", Toast.LENGTH_LONG).show();
                    }
                }
        ) ;
        requestQueue.add(request);
    }


    public void get_favorites(){
        FavdbHelper favdbHelper = new FavdbHelper(getApplicationContext());
        SQLiteDatabase database  = favdbHelper.getReadableDatabase();
        Cursor cursor = database.query(FavoritesContract.FavoritesColumns.TABLE_NAME,null,null,null,null,null,null,null);

        while (cursor.moveToNext()){
            arrayFaforites.add(cursor.getInt(1));
        }
        cursor.close();
        cursor.close();
         set = new LinkedHashSet<>(arrayFaforites);

         arrayList = new ArrayList<>(set);
    }


    public void status_favorites(){
        get_favorites();
        if (!arrayList.isEmpty()){
            show_image.setVisibility(View.GONE);
            message_title.setVisibility(View.GONE);
        }
    }



}
