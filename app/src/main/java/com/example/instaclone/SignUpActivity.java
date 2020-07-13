package com.example.instaclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;

public class SignUpActivity extends AppCompatActivity {
    public static final String TAG = "SignUpActivity";
    private Context context = this;
    private EditText etUsernameSignUp;
    private EditText etRealName;
    private EditText etEmailSignUp;
    private EditText etPasswordSignUp;
    private EditText etPasswordSignUpConfirm;
    private Button btnSignUp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etUsernameSignUp = findViewById(R.id.etUsernameSignUp);
        etRealName = findViewById(R.id.etRealName);
        etEmailSignUp = findViewById(R.id.etEmailSignUp);
        etPasswordSignUp = findViewById(R.id.etPasswordSignUp);
        etPasswordSignUpConfirm = findViewById(R.id.etPasswordSignUpConfirm);
        btnSignUp2 = findViewById(R.id.btnSignUp2);
        btnSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etUsernameSignUp.getText().toString().isEmpty()){
                    Toast.makeText(context, "Username is required", Toast.LENGTH_SHORT);
                    return;
                }
                if (etRealName.getText().toString().isEmpty()){
                    Toast.makeText(context, "Full Name is required", Toast.LENGTH_SHORT);
                    return;
                }
                if (etEmailSignUp.getText().toString().isEmpty()){
                    Toast.makeText(context, "Email required", Toast.LENGTH_SHORT);
                    return;
                }
                if (etPasswordSignUp.getText().toString().isEmpty()){
                    Toast.makeText(context, "Password required", Toast.LENGTH_SHORT);
                    return;
                }
                if (etPasswordSignUpConfirm.getText().toString().isEmpty()){
                    Toast.makeText(context, "Password Confirmation required", Toast.LENGTH_SHORT);
                    return;
                }
                //we have to check real validity
                Log.i(TAG, "we passed all requirements");
                if (etPasswordSignUp.getText().toString().equals(etPasswordSignUpConfirm.getText().toString())){
                    ParseUser user = new ParseUser();
                    user.setUsername(etUsernameSignUp.getText().toString());
                    user.setPassword(etPasswordSignUp.getText().toString());
                    user.setEmail(etEmailSignUp.getText().toString());
                    user.put("realName", etRealName.getText().toString());
                    /*
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.instagram_user_filled_24);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
                    byte[] byteArray = stream.toByteArray();
                    ParseFile file = new ParseFile("default_profile.png", byteArray);
                    user.put("profilePhoto", file);
                     */

                    Log.i(TAG, "passwords matched");
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            Log.i(TAG, "we made it");
                            if (e!=null){
                                Log.e(TAG, "something went wrong", e);
                                Toast.makeText(context, " Something went wrong with sign up", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent i = new Intent(context, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });
                } else {
                    Toast.makeText(context, "Passwords do not match ", Toast.LENGTH_SHORT);
                }
            }
        });



    }
}
