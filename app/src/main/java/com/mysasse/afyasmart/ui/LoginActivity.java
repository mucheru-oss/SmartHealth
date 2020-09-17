package com.mysasse.afyasmart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.utils.UIHelpers;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;


    //Global accessible views
    private TextInputEditText emailTxt, passwordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize fire-base variables
        mAuth = FirebaseAuth.getInstance();

        //Register View
        emailTxt = findViewById(R.id.email_txt);
        passwordTxt = findViewById(R.id.password_txt);
        ProgressBar loginProgressBar = findViewById(R.id.login_progress_bar);
        MaterialButton loginButton = findViewById(R.id.login_button);
        TextView signUpTv = findViewById(R.id.sign_up_tv);

        //Login Button Click Listener
        loginButton.setOnClickListener(view -> {

            String email = String.valueOf(emailTxt.getText());
            String password = String.valueOf(passwordTxt.getText());

            if (hasInvalidInputs(email, password)) return;

            loginProgressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {

                        loginProgressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {
                            sendHome();
                            return;
                        }

                        assert task.getException() != null;
                        Log.e(TAG, "onCreate: signInWithEmailAndPassword-failed => ", task.getException());
                        UIHelpers.toast("Login failed: " + task.getException().getLocalizedMessage());
                    });
        });

        //Sign up text view listener
        signUpTv.setOnClickListener(view -> {
            //Start the register activity
            startActivity(new Intent(this, RegisterActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

    }


    /**
     * @param email    input by the user
     * @param password input by user
     * @return boolean if the inputs are valid
     */
    private boolean hasInvalidInputs(String email, String password) {

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

    @Override
    protected void onStart() {
        super.onStart();

        //Checking whether the user is already authenticated
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) sendHome();
    }

    public void sendHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }
}
