package com.example.clothesshopapp.Activity.User;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.clothesshopapp.Activity.Shared.LogInActivity;
import com.example.clothesshopapp.R;
import com.example.clothesshopapp.ViewModel.User.SignUpViewModel;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private SignUpViewModel viewModel;
    private EditText edtEmail, edtUsername, edtPassword, edtConfirmPassword;
    private TextView tvAddImage, tvHaveAccount;
    private RoundedImageView imageProfile;
    private Button btnSignUp;
    private String selectedImagePath = "";
    private static final int STORAGE_PERMISSION_CODE = 100;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImagePath = saveImageToInternalStorage(uri);
                    if (!selectedImagePath.isEmpty()) {
                        Glide.with(this).load(selectedImagePath).into(imageProfile);
                        viewModel.onImageSelected();
                    } else {
                        runOnUiThread(() -> showErrorDialog("Failed to save the selected image"));
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        tvAddImage = findViewById(R.id.tvAddImage);
        imageProfile = findViewById(R.id.imageProfile);
        btnSignUp = findViewById(R.id.btnSignUp);
        tvHaveAccount = findViewById(R.id.tvHaveAccount);

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        tvAddImage.setOnClickListener(v -> requestStoragePermission());

        btnSignUp.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            String username = edtUsername.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();
            String confirmPassword = edtConfirmPassword.getText().toString().trim();
            viewModel.signUp(email, username, password, confirmPassword, selectedImagePath);
        });

        tvHaveAccount.setOnClickListener(v -> viewModel.navigateToLogIn());

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
                startActivity(new Intent(SignUpActivity.this, LogInActivity.class));
                finish();
            }
        });

        viewModel.getHideAddImageEvent().observe(this, hide -> {
            if (hide) {
                tvAddImage.setVisibility(View.GONE);
            }
        });
    }

    private void requestStoragePermission() {
        String permission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            permission = android.Manifest.permission.READ_MEDIA_IMAGES;
        } else {
            permission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
        }

        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    STORAGE_PERMISSION_CODE);
        } else {
            pickImageLauncher.launch("image/*");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageLauncher.launch("image/*");
            } else {
                showErrorDialog("Storage permission denied");
            }
        }
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                throw new FileNotFoundException("Cannot open input stream for URI: " + uri);
            }
            File directory = new File(getFilesDir(), "images");
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    throw new IOException("Failed to create images directory");
                }
            }
            File file = new File(directory, "profile_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();
            return file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found when saving image: " + e.getMessage(), e);
            runOnUiThread(() -> showErrorDialog("Cannot access the selected image"));
            return "";
        } catch (IOException e) {
            String errorMessage = e.getMessage() != null ? e.getMessage() : "Unknown error";
            if (errorMessage.contains("ENOSPC")) {
                Log.e(TAG, "Insufficient storage space: " + errorMessage, e);
                runOnUiThread(() -> showErrorDialog("Not enough storage space to save image"));
            } else {
                Log.e(TAG, "Failed to save image: " + errorMessage, e);
                runOnUiThread(() -> showErrorDialog("Failed to save image due to an unexpected error"));
            }
            return "";
        }
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .show();
    }
}