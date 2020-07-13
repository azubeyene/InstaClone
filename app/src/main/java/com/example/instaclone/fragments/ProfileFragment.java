package com.example.instaclone.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instaclone.LoginActivity;
import com.example.instaclone.MainActivity;
import com.example.instaclone.Post;
import com.example.instaclone.ProfileAdapter;
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
public class ProfileFragment extends Fragment {
    public static final String TAG = "ProfileFragment";
    private RecyclerView rvProfilePosts;
    private ProfileAdapter adapter;
    private List<Post> allUserPosts;
    private ParseUser user;
    private ImageView ivProfilePhoto;
    private TextView tvRealName;
    private Button btnLogOut;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public ProfileFragment(ParseUser given_user){
        user = given_user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        rvProfilePosts = view.findViewById(R.id.rvProfilePosts);
        ivProfilePhoto = view.findViewById(R.id.ivProfilePhoto);
        tvRealName = view.findViewById(R.id.tvRealName);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setVisibility(View.INVISIBLE);

        if (user.equals(ParseUser.getCurrentUser())){
            btnLogOut.setVisibility(View.VISIBLE);
            btnLogOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //navigate to Settings Fragment, set getCurrentUser Equal to null, then intent to LoginScreen
                    ParseUser.logOut();
                    Intent i = new Intent((MainActivity) getContext(), LoginActivity.class);
                    startActivity(i);
                    ((MainActivity) getContext()).finish();
                }
            });
        }

        //TODO load profile photo;
        //Glide.with(getContext()).load(user.getProfilePhoto)
        tvRealName.setText(user.getString("realName"));

        //Log.i(TAG, user.getParseFile("profilePhoto").getUrl());
        if (user.getParseFile("profilePhoto")!=null){
            Glide.with(getContext()).load(user.getParseFile("profilePhoto").getUrl()).into(ivProfilePhoto);
        }

        //ivProfilePhoto.setImageResource(R.drawable.ic_heart_active);
        allUserPosts = new ArrayList<>();
        //set adapter
        adapter = new ProfileAdapter(getContext(), allUserPosts);
        rvProfilePosts.setAdapter(adapter);
        rvProfilePosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        queryPosts();
    }

    private void queryPosts() {
        //choose query class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(Post.KEY_USER, user);
        query.include(Post.KEY_USER); //this includes users as well as posts as they are linked
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        //Now impose constraints on the query; here we will just get all posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "something went wrong", e);
                }
                allUserPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
