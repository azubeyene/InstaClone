package com.example.instaclone;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instaclone.fragments.CommentsFragment;
import com.example.instaclone.fragments.ProfileFragment;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
        try {
            holder.bind(post);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void clear(){
        posts.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Post> new_posts){
        posts.addAll(new_posts);
        notifyDataSetChanged();
    }

    public Post lastPost(){
        return posts.get(posts.size()-1);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvUsername;
        private TextView tvDescription;
        private TextView tvRelativeTime;
        private TextView tvPostViewComments;
        private TextView tvPostLikes;
        private ImageView ivPostLike;
        private ImageView ivPostComment;
        private ImageView ivImage;
        private ImageView ivPostProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvRelativeTime = itemView.findViewById(R.id.tvRelativeTime);
            tvPostViewComments = itemView.findViewById(R.id.tvPostViewComments);
            tvPostLikes = itemView.findViewById(R.id.tvPostLikes);

            ivPostLike = itemView.findViewById(R.id.ivPostLike);
            ivPostComment = itemView.findViewById(R.id.ivPostComment);
            ivImage = itemView.findViewById(R.id.ivImage);
            ivPostProfilePic = itemView.findViewById(R.id.ivPostProfilePic);
        }

        public void bind(final Post post) throws ParseException {
            //bind the post elements to view holder
            queryComments(post);
            int numOfComments;
            final int numOfLikes = queryLikes(post);
            //TODO: make queryLikes(post) --> update tvPostLikes (define this if not already)
            //TODO: Also remember to do this on ProfilePostFragment/Adapter

            String username = post.getUser().getUsername();
            String sourceString = "<b>" + username + "</b> " + post.getDescription();
            tvUsername.setText(username);
            tvDescription.setText(Html.fromHtml(sourceString));
            //post.getCreatedAt.getTime
            String relativeDate = DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime(),
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            tvRelativeTime.setText(relativeDate);
            if (numOfLikes>1 || numOfLikes ==0){
                tvPostLikes.setText(Integer.toString(numOfLikes) + " likes");
            } else {
                tvPostLikes.setText(Integer.toString(1) + " like");
            }
            /*
            if (numOfComments>1 || numOfComments ==0){
                tvPostViewComments.setText("View " + Integer.toString(posts.size())+ " comments");
            } else {
                tvPostViewComments.setText("View 1 comment");
            }

             */


            Log.i("PostsAdapter", "we are binding stuff");

            ivPostComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("PostsAdapter", "comments icon is being tapped");
                    Log.i("PostsAdapter", post.getUser().getUsername());
                    //TODO actually et below num
                    Fragment commentsFragment = new CommentsFragment(post, numOfLikes);
                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, commentsFragment).addToBackStack(null).commit();
                }
            });

            ivPostLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Some how get if the image is liked
                    //just assume i didnt like it but everyone did
                    Like like = new Like();
                    like.setPost(post);
                    like.setLiker(ParseUser.getCurrentUser());
                    like.saveInBackground();
                    ivPostLike.setImageResource(R.drawable.ic_heart_active);
                    //also change like counter
                    tvPostLikes.setText(Integer.toString(numOfLikes+1) + " likes");
                }
            });

            tvPostViewComments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment commentsFragment = new CommentsFragment(post, numOfLikes);
                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, commentsFragment).addToBackStack(null).commit();
                }
            });

            tvUsername.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment userFragment = new ProfileFragment(post.getUser());
                    ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, userFragment).addToBackStack(null).commit();
                }
            });

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

        private int queryLikes(Post post) throws ParseException {
            //choose query class
            ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
            query.whereEqualTo(Like.KEY_POST, post);
            return query.count();
        }

        public void queryComments(final Post post){
            //choose query class
            ParseQuery<Comment> query = ParseQuery.getQuery(Comment.class);
            query.whereEqualTo(Comment.KEY_POST, post);
            query.include(Comment.KEY_COMMENTER); //this includes users as well as posts as they are linked
            query.setLimit(20);
            query.addDescendingOrder(Post.KEY_CREATED_AT);

            //Now impose constraints on the query; here we will just get all posts
            query.findInBackground(new FindCallback<Comment>() {
                @Override
                public void done(List<Comment> posts, ParseException e) {
                    if (e!=null){
                        Log.e("postsadapter", "something went wrong", e);
                    }
                    //TODO
                    if (posts.size()>1||posts.size()==0){
                        tvPostViewComments.setText("View " + Integer.toString(posts.size())+ " comments");
                    } else {
                        tvPostViewComments.setText("View 1 comment");
                    }

                }
            });
        }

    }
}
