package com.example.clothesshopapp.Activity.User;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.clothesshopapp.Fragment.User.CartFragment;
import com.example.clothesshopapp.Fragment.User.HomeFragment;
import com.example.clothesshopapp.Fragment.User.ProductsFragment;
import com.example.clothesshopapp.Fragment.User.ProfileFragment;
import com.example.clothesshopapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_bottom);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(navListener);

        Fragment selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
    }
    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        int id = item.getItemId();
        Fragment selectedFragment = null;
        if (id == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (id == R.id.nav_cart) {
            selectedFragment = new CartFragment();
        } else if (id == R.id.nav_products) {
            selectedFragment = new ProductsFragment();
        } else if (id == R.id.nav_profile) {
            selectedFragment = new ProfileFragment();
        }
        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
        return true;
    };
}