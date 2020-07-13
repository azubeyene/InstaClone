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
import android.widget.Button;
import android.widget.EditText;
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
import com.parse.ParseUser;

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
    private ImageView ivCommentsPostImage;

    private TextView tvCommentsDescription;
    private TextView tvCommentsRelativeTime;
    private TextView tvCommentLikes;

    private EditText etAddComment;
    private Button btnSubmitComment;
    private Post post;
    private int numOfLikes;


    public CommentsFragment() {
        // Required empty public constructor
    }

    public CommentsFragment(Post post, int numOfLikes){
        this.post = post;
        this.numOfLikes = numOfLikes;
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
        ivCommentsPostImage = view.findViewById(R.id.ivCommentsPostImage);
        tvCommentsDescription = view.findViewById(R.id.tvCommentsDescription);
        tvCommentsRelativeTime = view.findViewById(R.id.tvCommentsRelativeTime);
        tvCommentLikes = view.findViewById(R.id.tvCommentLikes);

        etAddComment = view.findViewById(R.id.etAddComment);
        btnSubmitComment = view.findViewById(R.id.btnSubmitComment);

        String username = post.getUser().getUsername();
        String sourceString = "<b>" + username + "</b> " + post.getDescription();
        tvCommentsDescription.setText(Html.fromHtml(sourceString));
        //post.getCreatedAt.getTime
        String relativeDate = DateUtils.getRelativeTimeSpanString(post.getCreatedAt().getTime(),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        tvCommentsRelativeTime.setText(relativeDate);
        tvCommentLikes.setText(Integer.toString(numOfLikes) + " likes");
        Log.i(TAG, "comments init is being run");

        if (post.getUser().getParseFile("profilePhoto")!=null){
            //Set this pic as profile pic on main comment
            Glide.with(getContext()).load(post.getUser().getParseFile("profilePhoto").getUrl()).into(ivMyProfilePic);
        } else {
            ivMyProfilePic.setImageResource(R.drawable.ic_person_black_24dp);
        }

        if (post.getImage()!=null){
            //Set this pic as profile pic on main comment
            Glide.with(getContext()).load(post.getImage().getUrl()).into(ivCommentsPostImage);
        } else {
            ivCommentsPostImage.setImageResource(R.drawable.ic_person_black_24dp);
        }

        rvComments = view.findViewById(R.id.rvComments);
        allCommentsPosts = new ArrayList<>();

        adapter = new CommentsAdapter(getContext(), allCommentsPosts, post);
        rvComments.setAdapter(adapter);
        rvComments.setLayoutManager(new LinearLayoutManager(getContext()));
        queryComments();
        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comment = etAddComment.getText().toString();
                if (comment.isEmpty()){
                    Toast.makeText(getContext(), "Comment must be non-empty", Toast.LENGTH_SHORT);
                    return;
                }
                Log.i(TAG, "submit btn being pressed");
                //Create a comment and send to Parse data base
                Comment newComment = new Comment();
                newComment.setMessage(comment);
                newComment.setCommenter(ParseUser.getCurrentUser());
                newComment.setPost(post);
                newComment.saveInBackground();
                etAddComment.setText("");
                //TODO: Auto add comment to comments list ie comments.add(0, newComment)
            }
        });
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
