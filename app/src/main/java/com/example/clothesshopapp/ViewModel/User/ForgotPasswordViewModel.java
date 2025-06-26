package com.example.clothesshopapp.ViewModel.User;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.clothesshopapp.Activity.Utils.EmailUtils;
import com.example.clothesshopapp.Data.Repository.UserRepository;

import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class ForgotPasswordViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final MutableLiveData<String> errorEvent = new MutableLiveData<>();
    private final MutableLiveData<String> successEvent = new MutableLiveData<>();
    private final MutableLiveData<String> navigationEvent = new MutableLiveData<>();

    @Inject
    public ForgotPasswordViewModel(UserRepository userRepository) {
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

    public void resetPassword(String email, String password, String confirmPassword) {
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorEvent.postValue("Please fill in all information");
            return;
        }
        if (!EmailUtils.isValidEmail(email)) {
            errorEvent.postValue("Invalid email format");
            return;
        }

        userRepository.getUserByEmail(email, user -> {
            if (user == null) {
                errorEvent.postValue("Email does not exist");
                return;
            }
            if (!password.equals(confirmPassword)) {
                errorEvent.postValue("Passwords do not match");
                return;
            }
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
            userRepository.updateUser(user, () -> {
                successEvent.postValue("Password reset successful");
            });
        });
    }

    public void navigateToLogIn() {
        navigationEvent.postValue("LogInActivity");
    }
}
