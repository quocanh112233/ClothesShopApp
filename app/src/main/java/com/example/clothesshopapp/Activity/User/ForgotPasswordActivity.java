package com.example.clothesshopapp.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.clothesshopapp.Activity.Shared.LogInActivity;
import com.example.clothesshopapp.R;
import com.example.clothesshopapp.ViewModel.User.ForgotPasswordViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ForgotPasswordActivity extends AppCompatActivity {
    private ForgotPasswordViewModel viewModel;
    private EditText edtEmail, edtPassword, edtConfirmPassword;
    private Button btnResetPass;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        btnResetPass = findViewById(R.id.btnResetPass);
        back = findViewById(R.id.back);

        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        back.setOnClickListener(v -> finish());
        btnResetPass.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();
            viewModel.resetPassword(email, password, confirmPassword);
        });
        
        viewModel.getErrorEvent().observe(this, error -> showErrorDialog(error));

        viewModel.getSuccessEvent().observe(this, message -> {
            new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setPositiveButton("OK", (dialog, which) -> viewModel.navigateToLogIn())
                    .setCancelable(false)
                    .show();
        });

        viewModel.getNavigationEvent().observe(this, destination -> {
            if ("LogInActivity".equals(destination)) {
                startActivity(new Intent(ForgotPasswordActivity.this, LogInActivity.class));
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