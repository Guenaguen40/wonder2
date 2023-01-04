package com.soumayaguenaguen.wonder;

import static com.soumayaguenaguen.wonder.R.id.ques;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class SignupActivity extends AppCompatActivity {
    CircleImageView ImgUserPhoto;
    static int PReqCode = 1 ;
    static int REQUESCODE = 1 ;
    Uri pickedImgUri ;

    private EditText userEmail,userPassword,userName;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Referencing the TextView
        TextView textView = findViewById(ques);

        // The text that need to be styled using spans
        String text = "Already have an account? Sign In";

        // This will convert the text-string to spannable string
        // and we will used this spannableString to put spans on
        // them and make the sub-string changes
        SpannableString spannableString = new SpannableString(text);
        SpannableString clickable = new SpannableString(text);

        // Creating the spans to style the string
        ForegroundColorSpan foregroundColorSpanBleu = new ForegroundColorSpan(Color.BLUE);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);

        // Setting the spans on spannable string
        spannableString.setSpan(foregroundColorSpanBleu, 27, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(boldSpan, 27, 32, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Setting the spannable string on TextView
        textView.setText(spannableString);
        textView.setMovementMethod(new LinkMovementMethod());

        clickable.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        }, clickable.length() - "Sign In".length(), clickable.length(), 0);
        textView.setText(clickable);

        //ini views
        userEmail = findViewById(R.id.textEmail);
        userPassword = findViewById(R.id.textPass);
        userName = findViewById(R.id.textName);
        Button regBtn = findViewById(R.id.btnSignUpAcc);


        mAuth = FirebaseAuth.getInstance();


        regBtn.setOnClickListener(view -> {

            final String email = userEmail.getText().toString();
            final String password = userPassword.getText().toString();
            final String name = userName.getText().toString();

            if( email.isEmpty() || name.isEmpty() || password.isEmpty()) {


                // something goes wrong : all fields must be filled
                // we need to display an error message
                showMessage("Please Verify all fields") ;

            }
            else {
                // everything is ok and all fields are filled now we can start creating user account
                // CreateUserAccount method will try to create the user if the email is valid
                CreateUserAccount(email,name,password);
            }
        });

        ImgUserPhoto = findViewById(R.id.regUserPhoto) ;

        ImgUserPhoto.setOnClickListener(view -> checkAndRequestForPermission());
    }



    private void CreateUserAccount(String email, final String name, String password) {

        // this method create user account with specific email and password

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // user account created successfully
                        showMessage("Account created");
                        // after we created user account we need to update his profile picture and name
                        updateUserInfo( name ,pickedImgUri,mAuth.getCurrentUser());
                    }
                    else
                    {
                        // account creation failed
                        showMessage("account creation failed" + Objects.requireNonNull(task.getException()).getMessage());

                    }
                });
    }


    // update user photo and name
    private void updateUserInfo(final String name, @NonNull Uri pickedImgUri, final FirebaseUser currentUser) {

        // first we need to upload user photo to firebase storage and get url

        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        Log.d("TAG", "Putting file at URI: " + pickedImgUri);

            imageFilePath.putFile(pickedImgUri).addOnSuccessListener(taskSnapshot -> {

            // image uploaded succesfully
            // now we can get our image url

            imageFilePath.getDownloadUrl().addOnSuccessListener(uri -> {

                // uri contain user image url


                UserProfileChangeRequest profleUpdate = new UserProfileChangeRequest.Builder()
                        .setDisplayName(name)
                        .setPhotoUri(uri)
                        .build();


                currentUser.updateProfile(profleUpdate)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // user info updated successfully
                                showMessage("Register Complete");
                                updateUI();
                            }
                        }).addOnFailureListener(e -> {
                            // failure code here
                            Log.e("TAG", "Error updating user profile: " + e.getMessage());
                        });


            });
        });
    }

    private void updateUI() {

        Intent homeActivity = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(homeActivity);
    }

    // simple method to show toast message
    private void showMessage(String message) {

        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }

    private void openGallery() {
        //TODO: open gallery intent and wait for user to pick an image !

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);
    }

    private void checkAndRequestForPermission() {


        if (ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(SignupActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();

            }

            else
            {
                ActivityCompat.requestPermissions(SignupActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }
        else
            openGallery();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null ) {
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData() ;
            ImgUserPhoto.setImageURI(pickedImgUri);
        }




    }

}
