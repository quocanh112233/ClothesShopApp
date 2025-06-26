package com.example.clothesshopapp.ViewModel.Shared;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.clothesshopapp.Data.Repository.UserRepository;
import com.example.clothesshopapp.Data.SharedPreferenceManager;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class SplashViewModel extends ViewModel {
    private final UserRepository userRepository;
    private final SharedPreferenceManager prefsManager;
    private final MutableLiveData<String> navigationEvent = new MutableLiveData<>();

    @Inject
    public SplashViewModel(UserRepository userRepository, SharedPreferenceManager prefsManager) {
        this.userRepository = userRepository;
        this.prefsManager = prefsManager;
    }

    public LiveData<String> getNavigationEvent() {
        return navigationEvent;
    }

    public void checkLoginStatus() {
        String savedEmail = prefsManager.getEmail();
        if (savedEmail.isEmpty() || !prefsManager.getRememberMe()) {
            navigationEvent.postValue("LogInActivity");
            return;
        }

        userRepository.getUserByEmail(savedEmail, user -> {
            if (user != null) {
                navigationEvent.postValue("admin".equals(user.getRole()) ? "AdminActivity" : "MainActivity");
            } else {
                navigationEvent.postValue("LogInActivity");
            }
        });
    }
}
