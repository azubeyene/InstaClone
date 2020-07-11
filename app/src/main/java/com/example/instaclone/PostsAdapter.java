package com.example.instaclone;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.zip.Inflater;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
        //TODO: Below SET ON CLICK LISTENERS
        //attach on click listener on holder.tvUsername.setOnClickListener(...) and the rest of components
        //if heart get clicked, then immideatly update the data base and and check post.is_heart to true and update heart to appear/ oppo otherwise
        //if comment, then launch context.getFragmentManager... to commentFragment
        //if username, then launch context.getFragmentManager... to ProfileFragment
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUsername;
        private TextView tvDescription;
        private TextView tvRelativeTime;
        private ImageView ivImage;
        private ImageView ivPostProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvRelativeTime = itemView.findViewById(R.id.tvRelativeTime);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivPostProfilePic = itemView.findViewById(R.id.ivPostProfilePic);
        }

        public void bind(Post post) {
            //bind the post elements to view holder
            String username = post.getUser().getUsername();
            String sourceString = "<b>" + username + "</b> " + post.getDescription();
            tvUsername.setText(username);
            tvDescription.setText(Html.fromHtml(sourceString));
            //post.getCreatedAt.getTime
            String relativeDate = DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime(),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            tvRelativeTime.setText(relativeDate);
            if (post.getImage()!=null){
                Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
            }

            if (post.getUser().getParseFile("profilePhoto")!=null){
                //Set this pic as profile pic on main comment
                Glide.with(context).load(post.getUser().getParseFile("profilePhoto").getUrl()).into(ivPostProfilePic);
            } else {
                ivPostProfilePic.setImageResource(R.drawable.ic_person_black_24dp);
            }

        }
    }
}
