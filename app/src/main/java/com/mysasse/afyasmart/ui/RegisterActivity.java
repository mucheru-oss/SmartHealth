package com.mysasse.afyasmart.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Profile;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;

    //Global accessible views
    private TextInputEditText nameTxt, emailTxt, passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize fire-base variables
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        //Register View
        nameTxt = findViewById(R.id.name_txt);
        emailTxt = findViewById(R.id.email_txt);
        passwordTxt = findViewById(R.id.password_txt);
        ProgressBar registerProgressBar = findViewById(R.id.register_progress_bar);
        MaterialButton loginButton = findViewById(R.id.register_button);
        TextView signInTv = findViewById(R.id.sign_in_tv);

        signInTv.setOnClickListener(view -> finish());

        loginButton.setOnClickListener(view -> {
            String name = String.valueOf(nameTxt.getText());
            String email = String.valueOf(emailTxt.getText());
            String password = String.valueOf(passwordTxt.getText());

            if (hasInvalidInputs(name, email, password)) return;

            registerProgressBar.setVisibility(View.VISIBLE);

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        registerProgressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Profile profile = new Profile();
                            profile.setName(name);
                            profile.setRole("Patient");
                            initProfile(profile);
                        } else {
                            Log.e(TAG, "onCreate: createUserWithEmailAndPassword: error: ", task.getException());
                            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    });


        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void initProfile(Profile profile) {
        assert mAuth.getCurrentUser() != null;

        String currentUid = mAuth.getCurrentUser().getUid();

        mDatabase.collection("profiles").document(currentUid).set(profile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "User profile successfully updated", Toast.LENGTH_SHORT).show();
                sendHome();
            } else {
                Log.e(TAG, "updateUserProfile: Error While uploading the profile details to fire-store: ", task.getException());
                Toast.makeText(this, "Error while uploading all the profile details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean hasInvalidInputs(String name, String email, String password) {

        if (TextUtils.isEmpty(name)) {
            nameTxt.setError("Name is Required");
            nameTxt.requestFocus();
            return true;
        }

        if (TextUtils.isEmpty(email)) {
            emailTxt.setError("Email Address is Required");
            emailTxt.requestFocus();
            return true;
        }

        if (TextUtils.isEmpty(password)) {
            passwordTxt.setError("Password is Required");
            passwordTxt.requestFocus();
            return true;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailTxt.setError("Invalid email format");
            emailTxt.requestFocus();
            return true;
        }

        if (password.length() < 6) {
            passwordTxt.setError("Minimum of 6 chars required");
            passwordTxt.requestFocus();
            return true;
        }

        return false;
    }

    public void sendHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
