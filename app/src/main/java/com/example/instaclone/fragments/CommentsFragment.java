package com.example.instaclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instaclone.Comment;
import com.example.instaclone.CommentsAdapter;
import com.example.instaclone.Post;
import com.example.instaclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsFragment extends Fragment {
    public static final String TAG = "CommentsFragment";
    private RecyclerView rvComments;
    private List<Comment> allCommentsPosts;
    private CommentsAdapter adapter;
    private ImageView ivMyProfilePic;
    private TextView tvCommentsDescription;
    private TextView tvCommentsRelativeTime;
    private Post post;


    public CommentsFragment() {
        // Required empty public constructor
    }

    public CommentsFragment(Post post){
        this.post = post;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivMyProfilePic = view.findViewById(R.id.ivMyProfilePic);
        tvCommentsDescription = view.findViewById(R.id.tvCommentsDescription);
        tvCommentsRelativeTime = view.findViewById(R.id.tvCommentsRelativeTime);

        String username = post.getUser().getUsername();
        String sourceString = "<b>" + username + "</b> " + post.getDescription();
        tvCommentsDescription.setText(Html.fromHtml(sourceString));
        //post.getCreatedAt.getTime
        String relativeDate = DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        tvCommentsRelativeTime.setText(relativeDate);

        if (post.getUser().getParseFile("profilePhoto")!=null){
            //Set this pic as profile pic on main comment
            Glide.with(getContext()).load(post.getUser().getParseFile("profilePhoto").getUrl()).into(ivMyProfilePic);
        } else {
            ivMyProfilePic.setImageResource(R.drawable.ic_person_black_24dp);
        }

        rvComments = view.findViewById(R.id.rvComments);
        allCommentsPosts = new ArrayList<>();

        adapter = new CommentsAdapter(getContext(), allCommentsPosts);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        queryComments();
    }

    private void queryComments() {
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
                    Log.e(TAG, "something went wrong", e);
                }
                allCommentsPosts.addAll(posts);
                Toast.makeText(getContext(), "number of items in posts "+ Integer.toString(posts.size()), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });
    }
}
