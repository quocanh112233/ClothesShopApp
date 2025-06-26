package com.example.clothesshopapp.Activity.Shared;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.clothesshopapp.Activity.Customer.AdminActivity;
import com.example.clothesshopapp.Activity.User.MainActivity;
import com.example.clothesshopapp.R;
import com.example.clothesshopapp.ViewModel.Shared.SplashViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private SplashViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        viewModel.getNavigationEvent().observe(this, destination -> {
            if (destination != null) {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    Intent intent;
                    switch (destination) {
                        case "AdminActivity":
                            intent = new Intent(SplashActivity.this, AdminActivity.class);
                            break;
                        case "MainActivity":
                            intent = new Intent(SplashActivity.this, MainActivity.class);
                            break;
                        default:
                            intent = new Intent(SplashActivity.this, LogInActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }, 3000);
            }
        });

        viewModel.checkLoginStatus();
    }
}