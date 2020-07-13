package com.example.instaclone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instaclone.fragments.ProfilePostFragment;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    public ProfileAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);

        holder.bind(post, position);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivProfileGridImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfileGridImage = itemView.findViewById(R.id.ivProfileGridImage);
        }

        public void bind(final Post post, final int position) {
            ivProfileGridImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //int position = getAdapterPosition();
                    Log.i("profileadapter", Integer.toString(position));
                    Fragment profilePostFragment = new ProfilePostFragment(post);
                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, profilePostFragment).addToBackStack(null).commit();
                }
            });
            Glide.with(context).load(post.getImage().getUrl()).into(ivProfileGridImage);
        }

    }

}
