package com.mysasse.afyasmart.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mysasse.afyasmart.R;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        //Get the toolbar and set the action bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //AppBarConfiguration setup
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.allUsersFragment, R.id.allDiseasesFragment, R.id.notificationsFragment)
                .build();

        //Get the bottom navigation view
        BottomNavigationView navigationView = findViewById(R.id.nav_menu);

        NavController navController = Navigation.findNavController(this, R.id.admin_nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.admin_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.front_end_room) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
