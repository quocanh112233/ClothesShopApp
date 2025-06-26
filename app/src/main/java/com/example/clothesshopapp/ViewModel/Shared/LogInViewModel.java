package com.example.clothesshopapp.ViewModel.Shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.clothesshopapp.Activity.Utils.EmailUtils;
import com.example.clothesshopapp.Data.Repository.UserRepository;
import com.example.clothesshopapp.Data.SharedPreferenceManager;

import org.mindrot.jbcrypt.BCrypt;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LogInViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final SharedPreferenceManager prefsManager;
    private final MutableLiveData<String> errorEvent = new MutableLiveData<>();
    private final MutableLiveData<String> navigationEvent = new MutableLiveData<>();

    @Inject
    public LogInViewModel(UserRepository userRepository, SharedPreferenceManager prefsManager) {
        this.userRepository = userRepository;
        this.prefsManager = prefsManager;
    }

    public LiveData<String> getErrorEvent() {
        return errorEvent;
    }

    public LiveData<String> getNavigationEvent() {
        return navigationEvent;
    }

    public void login(String email, String password, boolean isRemembered) {
        if (email.isEmpty() || password.isEmpty()) {
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
            if (!BCrypt.checkpw(password, user.getPassword())) {
                errorEvent.postValue("Incorrect password");
                return;
            }
            prefsManager.saveLoginInfo(email, isRemembered);
            navigationEvent.postValue("admin".equals(user.getRole()) ? "AdminActivity" : "MainActivity");
        });
    }

    public void navigateToSignUp() {
        navigationEvent.postValue("SignUpActivity");
    }

    public void navigateToForgotPassword() {
        navigationEvent.postValue("ForgotPasswordActivity");
    }
}
