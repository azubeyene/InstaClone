package com.example.instaclone;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private EditText etDescription;
    private ImageView ivPostImage;
    private Button btnTakePic;
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etDescription = findViewById(R.id.etDescription);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnTakePic = findViewById(R.id.btnTakePic);

        queryPosts();
    }

    private void queryPosts() {
        //choose query class
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER); 
        //Now impose constraints on the query; here we will just get all posts
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "something went wrong", e);
                }
                for (Post post: posts){
                    Log.i(TAG, "Post"+post.getDescription());
                }
            }
        });
    }
}
