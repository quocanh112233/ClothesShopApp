package com.example.clothesshopapp.Activity.Utils;

import android.util.Patterns;

import kotlin.jvm.JvmStatic;

public class EmailUtils {
    @JvmStatic
    public static boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
