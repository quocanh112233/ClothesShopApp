package com.example.clothesshopapp.ViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.clothesshopapp.Data.Repository.UserRepository;
import com.example.clothesshopapp.Data.SharedPreferenceManager;
import com.example.clothesshopapp.ViewModel.Shared.LogInViewModel;
import com.example.clothesshopapp.ViewModel.Shared.SplashViewModel;
import com.example.clothesshopapp.ViewModel.User.ForgotPasswordViewModel;
import com.example.clothesshopapp.ViewModel.User.SignUpViewModel;

import org.jspecify.annotations.NonNull;

public class ViewModelFactory implements ViewModelProvider.Factory{
    private final UserRepository userRepository;
    private final SharedPreferenceManager prefsManager;

    public ViewModelFactory(UserRepository userRepository, SharedPreferenceManager prefsManager) {
        this.userRepository = userRepository;
        this.prefsManager = prefsManager;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(SplashViewModel.class)) {
            return (T) new SplashViewModel(userRepository, prefsManager);
        } else if (modelClass.isAssignableFrom(LogInViewModel.class)) {
            return (T) new LogInViewModel(userRepository, prefsManager);
        } else if (modelClass.isAssignableFrom(SignUpViewModel.class)) {
            return (T) new SignUpViewModel(userRepository);
        } else if (modelClass.isAssignableFrom(ForgotPasswordViewModel.class)) {
            return (T) new ForgotPasswordViewModel(userRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
