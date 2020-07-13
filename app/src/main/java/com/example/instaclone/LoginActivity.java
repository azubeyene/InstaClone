package com.example.instaclone;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    public static final int SIGN_UP_REQUEST_CODE = 456;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*
        if (ParseUser.getCurrentUser() != null){
            ParseUser.logOut();
        }

         */

        if (ParseUser.getCurrentUser()!=null){
            if (ParseUser.getCurrentUser().getParseFile("profilePhoto")==null){
                //This code is from https://coffeeshots970976098.wordpress.com/
                Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.instagram_user_filled_24);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                byte[] byteArray = stream.toByteArray();
                ParseFile file = new ParseFile("default_profile.png", byteArray);
                ParseUser.getCurrentUser().put("profilePhoto", file);
                ParseUser.getCurrentUser().saveInBackground();
            }

            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToSignUp();
            }
        });
    }

    private void loginUser(String username, String password) {
        //Todo: navigate to main activity if user signed in properly
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e!=null){
                    Log.e(TAG, "something went wrong", e);
                    return;
                }
                //login successfull; navigate to main activity
                goMainActivity();
            }
        });
    }
    private void navigateToSignUp(){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);
    }


    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
