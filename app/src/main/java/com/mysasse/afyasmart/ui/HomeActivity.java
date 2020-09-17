package com.mysasse.afyasmart.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mysasse.afyasmart.R;
import com.mysasse.afyasmart.data.models.Profile;
import com.mysasse.afyasmart.utils.UIHelpers;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDatabase;
    private CircleImageView userAvatarCiv;
    private TextView userNameTv;
    private Profile mProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Initialize fire-base instances
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseFirestore.getInstance();

        //Get toolbar and set it as the support action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Register the drawer layout
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        //Register the navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);

        //Setup the AppBarConfigurations
        mAppBarConfiguration = new AppBarConfiguration
                .Builder(R.id.homeFragment, R.id.doctorsFragment, R.id.diseasesFragment, R.id.messagesFragment, R.id.profileFragment)
                .setOpenableLayout(drawerLayout)
                .build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //Get and set the header layouts and the relevant views
        View headerLayout = navigationView.getHeaderView(0);
        userAvatarCiv = headerLayout.findViewById(R.id.user_avatar);
        userNameTv = headerLayout.findViewById(R.id.user_name);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options, menu);

        MenuItem menuItem = menu.findItem(R.id.search_option);

        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setQueryHint(getString(R.string.search_disease_hint));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.logout_option) {
            AlertDialog.Builder logoutDialog = new AlertDialog.Builder(this)
                    .setMessage("Are sure you want to logout ?")
                    .setPositiveButton("Sure", (dialog, which) -> {
                        mAuth.signOut();
                        sendToLogin();
                    })
                    .setNegativeButton("Cancel", ((dialog, which) -> {

                    }));

            logoutDialog.show();

            return true;
        }

        if (item.getItemId() == R.id.doctor_role_request_option) {
            navController.navigate(R.id.claimDoctorFragment);
            return true;
        }

        if (item.getItemId() == R.id.admin_dashboard_option) {
            startActivity(new Intent(this, AdminActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (mProfile != null) {

            if (!mProfile.getRole().equals("Admin")) {

                menu.removeItem(R.id.admin_dashboard_option);
            }

            if (!mProfile.getRole().equals("Patient")) {

                menu.removeItem(R.id.doctor_role_request_option);

            }
        }

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {

        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            sendToLogin();
            return;
        }

        updateUI(currentUser);
    }

    private void sendToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void updateUI(FirebaseUser currentUser) {
        Log.d(TAG, "updateUI: Authenticated User UID :" + currentUser.getUid());

        //Get and set the header details
        mDatabase.collection("profiles").document(currentUser.getUid())
                .addSnapshotListener(this, (documentSnapshot, e) -> {

                    if (e != null) {
                        Log.e(TAG, "updateUI: error getting user profile", e);
                        UIHelpers.toast("Error getting user profile: " + e.getLocalizedMessage());
                        return;
                    }

                    if (documentSnapshot != null) {
                        mProfile = documentSnapshot.toObject(Profile.class);

                        invalidateOptionsMenu();

                        //Set the display name
                        assert mProfile != null;

                        if (!TextUtils.isEmpty(mProfile.getName()))
                            userNameTv.setText(mProfile.getName());

                        if (!TextUtils.isEmpty(mProfile.getAvatar()))
                            Glide.with(this)
                                    .load(mProfile.getAvatar())
                                    .centerCrop()
                                    .placeholder(R.drawable.ic_account_circle)
                                    .into(userAvatarCiv);

                    }

                });
    }
}
