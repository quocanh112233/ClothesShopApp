package com.example.clothesshopapp.ViewModel.User;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.clothesshopapp.Activity.Utils.EmailUtils;
import com.example.clothesshopapp.Data.Local.Entity.User;
import com.example.clothesshopapp.Data.Repository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SignUpViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<String> errorEvent = new MutableLiveData<>();
    private final MutableLiveData<String> successEvent = new MutableLiveData<>();
    private final MutableLiveData<String> navigationEvent = new MutableLiveData<>();
    private final MutableLiveData<Boolean> hideAddImageEvent = new MutableLiveData<>();

    @Inject
    public SignUpViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<String> getErrorEvent() {
        return errorEvent;
    }

    public LiveData<String> getSuccessEvent() {
        return successEvent;
    }

    public LiveData<String> getNavigationEvent() {
        return navigationEvent;
    }

    public LiveData<Boolean> getHideAddImageEvent() {
        return hideAddImageEvent;
    }

    public void signUp(String email, String username, String password, String confirmPassword, String profileImage) {
        if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorEvent.postValue("Please fill in all information");
            return;
        }
        if (!EmailUtils.isValidEmail(email)) {
            errorEvent.postValue("Invalid email format");
            return;
        }
        if (profileImage.isEmpty()) {
            errorEvent.postValue("Please select a profile image");
            return;
        }

        userRepository.getUserByEmail(email, existingUser -> {
            if (existingUser != null) {
                errorEvent.postValue("Email already exists");
                return;
            }
            if (!password.equals(confirmPassword)) {
                errorEvent.postValue("Passwords do not match");
                return;
            }

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss", Locale.getDefault());
            User user = new User(
                    email,
                    username,
                    hashedPassword,
                    profileImage,
                    sdf.format(new Date()),
                    "customer"
            );
            userRepository.insertUser(user, () -> {
                successEvent.postValue("Sign Up successful");
            });
        });
    }

    public void onImageSelected() {
        hideAddImageEvent.postValue(true);
    }

    public void navigateToLogIn() {
        navigationEvent.postValue("LogInActivity");
    }
}
