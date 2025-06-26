package com.example.clothesshopapp.Activity.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;

import com.example.clothesshopapp.Activity.Shared.LogInActivity;
import com.example.clothesshopapp.Fragment.Customer.BannerManagementFragment;
import com.example.clothesshopapp.Fragment.Customer.CategoryManagementFragment;
import com.example.clothesshopapp.Fragment.Customer.CouponManagementFragment;
import com.example.clothesshopapp.Fragment.Customer.DashboardFragment;
import com.example.clothesshopapp.Fragment.Customer.FeedbackManagementFragment;
import com.example.clothesshopapp.Fragment.Customer.OrderManagementFragment;
import com.example.clothesshopapp.Fragment.Customer.ProductManagementFragment;
import com.example.clothesshopapp.Fragment.Customer.ReviewManagementFragment;
import com.example.clothesshopapp.Fragment.Customer.RoleAndPermissionsFragment;
import com.example.clothesshopapp.Fragment.Customer.UserManagementFragment;
import com.example.clothesshopapp.R;
import com.google.android.material.navigation.NavigationView;

public class AdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.Open,
                R.string.Close
        );
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(ContextCompat.getColor(this, R.color.dark_lavender));
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new DashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.dashboard);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.dashboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new DashboardFragment()).commit();
        }
        else if (item.getItemId() == R.id.productManagement) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new ProductManagementFragment()).commit();
        }
        else if (item.getItemId() == R.id.orderManagement) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new OrderManagementFragment()).commit();
        }
        else if (item.getItemId() == R.id.categoryManagement) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new CategoryManagementFragment()).commit();
        }
        else if (item.getItemId() == R.id.userManagement) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new UserManagementFragment()).commit();
        }
        else if (item.getItemId() == R.id.rolesAndPermissions) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new RoleAndPermissionsFragment()).commit();
        }
        else if (item.getItemId() == R.id.couponManagement) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new CouponManagementFragment()).commit();
        }
        else if (item.getItemId() == R.id.bannerManagement) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new BannerManagementFragment()).commit();
        }
        else if (item.getItemId() == R.id.reviewManagement) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new ReviewManagementFragment()).commit();
        }
        else if (item.getItemId() == R.id.feedbackManagement) {
            getSupportFragmentManager().beginTransaction().replace(R.id.flameLayout_container, new FeedbackManagementFragment()).commit();
        }
        else if (item.getItemId() == R.id.logout) {
            showLogoutDialog();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (isDrawerOpen()) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isDrawerOpen() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    private void closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }
    private void showLogoutDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Log out")
                .setMessage("Do you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(AdminActivity.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}