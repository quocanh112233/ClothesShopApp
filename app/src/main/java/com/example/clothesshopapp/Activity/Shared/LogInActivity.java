package com.example.clothesshopapp.Activity.Shared;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.clothesshopapp.Activity.Customer.AdminActivity;
import com.example.clothesshopapp.Activity.User.ForgotPasswordActivity;
import com.example.clothesshopapp.Activity.User.MainActivity;
import com.example.clothesshopapp.Activity.User.SignUpActivity;

import com.example.clothesshopapp.R;
import com.example.clothesshopapp.ViewModel.Shared.LogInViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LogInActivity extends AppCompatActivity {
    private LogInViewModel viewModel;
    private EditText edtEmail, edtPassword;
    private CheckBox checkboxRemember;
    private Button btnLogIn;
    private TextView tvForgotPassword, tvCreateAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        checkboxRemember = findViewById(R.id.checkboxRemember);
        btnLogIn = findViewById(R.id.btnLogIn);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
        tvCreateAccount = findViewById(R.id.tvCreateAccount);

        viewModel = new ViewModelProvider(this).get(LogInViewModel.class);

        btnLogIn.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            boolean rememberMe = checkboxRemember.isChecked();
            viewModel.login(email, password, rememberMe);
        });

        tvForgotPassword.setOnClickListener(v -> viewModel.navigateToForgotPassword());

        tvCreateAccount.setOnClickListener(v -> viewModel.navigateToSignUp());

        viewModel.getErrorEvent().observe(this, error -> showErrorDialog(error));

        viewModel.getNavigationEvent().observe(this, destination -> {
            Intent intent;
            switch (destination) {
                case "AdminActivity":
                    intent = new Intent(LogInActivity.this, AdminActivity.class);
                    break;
                case "MainActivity":
                    intent = new Intent(LogInActivity.this, MainActivity.class);
                    break;
                case "SignUpActivity":
                    intent = new Intent(LogInActivity.this, SignUpActivity.class);
                    break;
                case "ForgotPasswordActivity":
                    intent = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
                    break;
                default:
                    return;
            }
            startActivity(intent);
            if (destination.equals("AdminActivity") || destination.equals("MainActivity")) {
                finish();
            }
        });
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .show();
    }
}