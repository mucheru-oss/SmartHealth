package com.mysasse.afyasmart.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.mysasse.afyasmart.R;

public class RequestPermissionActivity extends AppCompatActivity {

    private static final String TAG = "RequestPermission";

    private final int PERMISSION_RC = 22;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_permission);

        //Request the permission when the button is pressed
        Button grantPermissionsButton = findViewById(R.id.grant_permissions_button);
        grantPermissionsButton.setOnClickListener(view -> ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_RC));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (requestCode == PERMISSION_RC) {
                Log.d(TAG, "onRequestPermissionsResult: PERMISSION GRANTED");
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onRequestPermissionsResult: PERMISSION DENIED");
        }
    }
}
