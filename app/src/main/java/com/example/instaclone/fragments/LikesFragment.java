package com.example.instaclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.instaclone.Like;
import com.example.instaclone.LikesAdapter;
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
public class LikesFragment extends Fragment {
    public static final String TAG = "ProfileFragment";
    private RecyclerView rvLikes;
    private LikesAdapter adapter;
    private List<Like> allLikePosts;


    public LikesFragment() {
        // Required empty public constructor
    } //ParseUser.getCurrentUser accessible everywhere


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_likes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvLikes = view.findViewById(R.id.rvLikes);
        allLikePosts = new ArrayList<>();

        adapter = new LikesAdapter(getContext(), allLikePosts);
        rvLikes.setAdapter(adapter);
        rvLikes.setLayoutManager(new LinearLayoutManager(getContext()));
        queryLikes();
    }

    private void queryLikes() {
        //choose query class
        ParseQuery<Like> query = ParseQuery.getQuery(Like.class);
        query.whereEqualTo(Like.KEY_POST_OWNER, ParseUser.getCurrentUser());

        query.include(Like.KEY_LIKER); //this includes users as well as posts as they are linked
        query.include(Like.KEY_POST);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        //Now impose constraints on the query; here we will just get all posts
        query.findInBackground(new FindCallback<Like>() {
            @Override
            public void done(List<Like> likePosts, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "something went wrong", e);
                }
                allLikePosts.addAll(likePosts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
