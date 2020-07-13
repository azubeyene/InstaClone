package com.example.instaclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instaclone.Comment;
import com.example.instaclone.Like;
import com.example.instaclone.MainActivity;
import com.example.instaclone.Post;
import com.example.instaclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfilePostFragment extends Fragment {
    private Post post;
    private TextView tvUsername;
    private TextView tvDescription;
    private TextView tvRelativeTime;
    private TextView tvPostViewComments;
    private TextView tvPostLikes;

    private ImageView ivPostProfilePic;
    private ImageView ivImage;
    private ImageView ivPostComment;
    private int numOfLikes;

    public ProfilePostFragment() {
        // Required empty public constructor
    }

    public ProfilePostFragment(Post post) {
        // Required empty public constructor
        this.post = post;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.item_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvDescription = view.findViewById(R.id.tvDescription);
        tvRelativeTime = view.findViewById(R.id.tvRelativeTime);
        tvPostViewComments = view.findViewById(R.id.tvPostViewComments);
        tvPostLikes = view.findViewById(R.id.tvPostLikes);
        ivPostProfilePic = view.findViewById(R.id.ivPostProfilePic);
        ivImage = view.findViewById(R.id.ivImage);
        ivPostComment = view.findViewById(R.id.ivPostComment);

        queryComments(post);
        try {
            numOfLikes = queryLikes(post);
        } catch (ParseException e) {
            e.printStackTrace();
        }
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
        ivPostComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("PostsAdapter", "comments icon is being tapped");
                Log.i("PostsAdapter", post.getUser().getUsername());
                //TODO query likes and ass bellow
                Fragment commentsFragment = new CommentsFragment(post, numOfLikes);
                ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction().replace(R.id.flContainer, commentsFragment).addToBackStack(null).commit();
            }
        });


        if (post.getImage()!=null){
            Glide.with(getContext()).load(post.getImage().getUrl()).into(ivImage);
        }

        if (post.getUser().getParseFile("profilePhoto")!=null){
            //Set this pic as profile pic on main comment
            Glide.with(getContext()).load(post.getUser().getParseFile("profilePhoto").getUrl()).into(ivPostProfilePic);
        } else {
            ivPostProfilePic.setImageResource(R.drawable.ic_person_black_24dp);
        }


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
                if (posts.size()>1||posts.size()==0){
                    tvPostViewComments.setText("View " + Integer.toString(posts.size())+ " comments");
                } else {
                    tvPostViewComments.setText("View 1 comment");
                }

            }
        });
    }

    private int queryLikes(Post post) throws ParseException {
        //choose query class
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.whereEqualTo(Like.KEY_POST, post);
        return query.count();
    }
}
