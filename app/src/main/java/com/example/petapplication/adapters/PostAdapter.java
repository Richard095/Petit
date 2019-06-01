package com.example.petapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.petapplication.R;
import com.example.petapplication.model.Post;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> petList;
    private OnPostClickListener onPostClickListener;

    public interface OnPostClickListener{
        void onPostClik(Post post);
    }
    public void setOnPostClickListener(OnPostClickListener onPostClickListener){
        this.onPostClickListener = onPostClickListener;
    }



    public PostAdapter(Context context, ArrayList<Post> petList) {
        this.context = context;
        this.petList = petList;
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(context).inflate(R.layout.item_list_post,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostAdapter.ViewHolder viewHolder, int position) {

        final Post post = petList.get(position);
        Picasso.get()
                .load(post.getImageURL())
                .resize(400,400)
                .centerCrop()
                .into(viewHolder.petImage);

        viewHolder.pet_list_item_name.setText(post.getPetName());
        viewHolder.status_post_item.setText(post.getStatus());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               onPostClickListener.onPostClik(post);
            }
        });

    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView  petImage;
        TextView pet_list_item_name;
        TextView status_post_item;
        public ViewHolder(View itemView) {
            super(itemView);

            petImage = itemView.findViewById(R.id.petImage_list_item);
            pet_list_item_name = itemView.findViewById(R.id.pet_list_item_name);
            status_post_item = itemView.findViewById(R.id.status_post_item);

        }
    }

}
