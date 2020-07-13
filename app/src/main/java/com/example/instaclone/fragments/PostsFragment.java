package com.example.instaclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instaclone.EndlessRecyclerViewScrollListener;
import com.example.instaclone.Post;
import com.example.instaclone.PostsAdapter;
import com.example.instaclone.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFragment extends Fragment {
    public static final String TAG = "PostsFragment";
    protected RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    protected SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;


    public PostsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO Fill in app state like

        rvPosts = view.findViewById(R.id.rvPosts);
        swipeContainer = view.findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        allPosts = new ArrayList<>();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryPosts();
            }
        });

        adapter = new PostsAdapter(getContext(), allPosts);

        rvPosts.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(layoutManager);

        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //load more data
                queryMorePosts(adapter.lastPost());
            }
        };
        rvPosts.addOnScrollListener(scrollListener);
        queryPosts();
    }

    protected void queryPosts() {
        //choose query class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
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
                //ada
                adapter.clear();
                //allPosts.addAll(posts);
                //adapter.notifyDataSetChanged();
                adapter.addAll(posts);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    protected void queryMorePosts(Post lastPost) {
        //choose query class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER); //this includes users as well as posts as they are linked
        query.whereLessThan("createdAt", lastPost);
        query.setLimit(20);
        query.addDescendingOrder(Post.KEY_CREATED_AT);

        //Now impose constraints on the query; here we will just get all posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "something went wrong", e);
                }
                //ada
                //adapter.clear();
                //allPosts.addAll(posts);
                //adapter.notifyDataSetChanged();
                adapter.addAll(posts);
                //swipeContainer.setRefreshing(false);
            }
        });
    }
}
