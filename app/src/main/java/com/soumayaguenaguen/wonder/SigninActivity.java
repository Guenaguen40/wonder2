package com.soumayaguenaguen.wonder;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SigninActivity extends AppCompatActivity {
    private EditText userNameEdt, passwordEdt;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Referencing the TextView
        TextView textView = findViewById(R.id.textView);

        // The text that need to be styled using spans
        String text = "Don’t have an account? Sign Up";

        // This will convert the text-string to spannable string
        // and we will used this spannableString to put spans on
        // them and make the sub-string changes
        SpannableString spannableString = new SpannableString(text);
        SpannableString clickable = new SpannableString(text);

        // Creating the spans to style the string
        ForegroundColorSpan foregroundColorSpanBleu = new ForegroundColorSpan(Color.BLUE);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);

        // Setting the spans on spannable string
        spannableString.setSpan(foregroundColorSpanBleu, 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(boldSpan, 24, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Setting the spannable string on TextView
        textView.setText(spannableString);
        textView.setMovementMethod(new LinkMovementMethod());

        clickable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        }, clickable.length() - "Sign Up".length(), clickable.length(), 0);
        textView.setText(clickable);


//login


        mAuth = FirebaseAuth.getInstance();
        userNameEdt = findViewById(R.id.text_email);
        passwordEdt = findViewById(R.id.text_pass);
        Button loginBtn = findViewById(R.id.btnSubmit_login);
        loadingPB = findViewById(R.id.idPBLoading);

        // adding on click listener for our login button.
        loginBtn.setOnClickListener(v -> {
            // hiding our progress bar.
            loadingPB.setVisibility(View.VISIBLE);
            // getting data from our edit text on below line.
            String email = userNameEdt.getText().toString();
            String password = passwordEdt.getText().toString();
            // on below line validating the text input.
            if (TextUtils.isEmpty(email) && TextUtils.isEmpty(password)) {
                Toast.makeText(SigninActivity.this, "Please enter your credentials..", Toast.LENGTH_SHORT).show();
                return;
            }
            // on below line we are calling a sign in method and passing email and password to it.
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                // on below line we are checking if the task is success or not.
                if (task.isSuccessful()) {
                    // on below line we are hiding our progress bar.
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(SigninActivity.this, "Login Successful..", Toast.LENGTH_SHORT).show();
                    // on below line we are opening our mainactivity.
                    Intent i = new Intent(SigninActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    // hiding our progress bar and displaying a toast message.
                    loadingPB.setVisibility(View.GONE);
                    Toast.makeText(SigninActivity.this, "Please enter valid user credentials..", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // in on start method checking if
        // the user is already sign in.
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            // if the user is not null then we are
            // opening a main activity on below line.
            Intent i = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(i);
            this.finish();
        }

        TextView forgot_password = findViewById(R.id.forgot_password_button);
// Set an OnClickListener on the forgot_password
        forgot_password.setOnClickListener(view -> {
            // Create an Intent to open the target activity
            Intent forpass = new Intent(this, ForgotPasswordActivity.class);
            // Start the target activity
            startActivity(forpass);
        });



    }}