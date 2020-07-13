package com.example.instaclone;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.ViewHoler> {
    public static final String TAG = "LikesAdapter";
    private Context context;
    private List<Like> posts;

    public LikesAdapter(Context context, List<Like> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_likes, parent, false);
        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        Like like = posts.get(position);
        holder.bind(like);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHoler extends RecyclerView.ViewHolder{
        ImageView ivLikesProfilePic;
        ImageView ivLikedPost;
        TextView tvLikedNotify;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            ivLikesProfilePic = itemView.findViewById(R.id.ivLikesProfilePic);
            ivLikedPost= itemView.findViewById(R.id.ivLikedPost);
            tvLikedNotify = itemView.findViewById(R.id.tvLikedNotify);
        }

        public void bind(Like like) {
            tvLikedNotify.setText(like.getLiker().getUsername() +" liked your post!");
            if (like.getLiker().getParseFile("profilePhoto")!=null && like.getPost().getParseFile(Post.KEY_Image)!=null){
                Log.i(TAG, "we found a like and object");
                Glide.with(context).load(like.getLiker().getParseFile("profilePhoto").getUrl()).into(ivLikesProfilePic);
                Glide.with(context).load(like.getPost().getParseFile(Post.KEY_Image).getUrl()).into(ivLikedPost);
            } else {
                Log.i(TAG, "Images not loading properly into adapter, check why they are null");
            }

        }
    }
}
