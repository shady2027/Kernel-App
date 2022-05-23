package com.news.kernel;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.news.kernel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//this is the activity for user registration

public class MainActivity extends AppCompatActivity {

    private TextView text_log_in;
    private Button btn_create_account;
    private EditText et_name;
    private EditText et_password;
    private EditText et_emailId;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private boolean registrationStatus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //to check if the user has registered in
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            //set the registrationStatus as true
            registrationStatus = true;

            // User is signed in
            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        } else {
            //set the registrationStatus as false
            registrationStatus = false;


            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }

        //creating clickable link to send the user from this activity to the login activity
        text_log_in = findViewById(R.id.text_log_in);
        String text = "Log in";
        SpannableString ss = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        text_log_in.setText(ss);
        text_log_in.setMovementMethod(LinkMovementMethod.getInstance());
        text_log_in.setHighlightColor(Color.TRANSPARENT);

        mAuth = FirebaseAuth.getInstance();
        btn_create_account = findViewById(R.id.btn_create_account);
        et_name = findViewById(R.id.et_name);
        et_emailId = findViewById(R.id.et_emailId);
        et_password = findViewById(R.id.et_password);
        progressBar = findViewById(R.id.progressbar);

        // Set on Click Listener on Registration button
        btn_create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });

    }

    //creating the firebase authentication methods
    private void registerNewUser() {

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // Take the value of the edit texts in Strings
        String email, password;
        email = et_emailId.getText().toString().trim();
        password = et_password.getText().toString().trim();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email!!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!!", Toast.LENGTH_LONG).show();
            return;
        }


        // create new user or register new user
        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_SHORT).show();

                            //set the registrationStatus as true
                            registrationStatus = true;


                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);

                            // if the user created intent to home activity
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Registration failed
                            Toast.makeText(
                                    getApplicationContext(), "Registration failed!!" + " Please try again later",
                                    Toast.LENGTH_LONG)
                                    .show();



                            // hide the progress bar
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

}