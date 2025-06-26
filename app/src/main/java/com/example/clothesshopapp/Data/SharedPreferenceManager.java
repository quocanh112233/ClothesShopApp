package com.example.clothesshopapp.Data;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ApplicationContext;

public class SharedPreferenceManager {
    private static final String PREF_NAME = "ClothesShopPrefs";
    private final SharedPreferences prefs;

    @Inject
    public SharedPreferenceManager(@ApplicationContext Context context) {
        this.prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveLoginInfo(String email, boolean rememberMe) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.putBoolean("rememberMe", rememberMe);
        editor.apply();
    }

    public String getEmail() {
        return prefs.getString("email", "");
    }

    public boolean getRememberMe() {
        return prefs.getBoolean("rememberMe", false);
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}
