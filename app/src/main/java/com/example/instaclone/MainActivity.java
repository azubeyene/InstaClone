package com.example.instaclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.ClipData;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.instaclone.fragments.ComposeFragment;
import com.example.instaclone.fragments.LikesFragment;
import com.example.instaclone.fragments.PostsFragment;
import com.example.instaclone.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        frameLayout = findViewById(R.id.flContainer);
        //Toolbar toolbar = findViewById(R.id.toolbar_main);
        //setActionBar(toolbar);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        //queryPosts();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_home:
                        // do something here
                        fragment = new PostsFragment();
                        break;

                    case R.id.action_compose:
                        // do something here
                        fragment = new ComposeFragment();
                        break;

                    case R.id.action_likes:
                        //navigate to likes page
                        fragment = new LikesFragment();
                        break;

                    case R.id.action_profile:
                        // do something here
                        fragment = new ProfileFragment(ParseUser.getCurrentUser());
                        break;
                    default:
                        //should never run;
                        fragment = new ComposeFragment();
                        Log.e(TAG, "Clicked on somewhere impossible");
                        //possibly bass
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;

            }
        });
        // Set default selection
        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}
