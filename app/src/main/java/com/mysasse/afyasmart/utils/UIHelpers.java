package com.mysasse.afyasmart.utils;

import android.widget.Toast;

import com.mysasse.afyasmart.AfyaSmartApplication;

public class UIHelpers {

    public static void toast(String message) {
        Toast.makeText(AfyaSmartApplication.getInstance(), message, Toast.LENGTH_SHORT).show();
    }
}
