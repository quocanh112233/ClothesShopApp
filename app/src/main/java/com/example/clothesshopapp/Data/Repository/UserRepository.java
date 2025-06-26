package com.example.clothesshopapp.Data.Repository;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.example.clothesshopapp.Data.Local.AppDatabase;
import com.example.clothesshopapp.Data.Local.Dao.UserDao;
import com.example.clothesshopapp.Data.Local.Entity.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import javax.inject.Inject;

public class UserRepository {
    private final UserDao userDao;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Inject
    public UserRepository(AppDatabase database) {
        this.userDao = database.userDao();
    }

    public void insertUser(User user, Runnable onComplete) {
        executor.execute(() -> {
            userDao.insert(user);
            new Handler(Looper.getMainLooper()).post(onComplete);
        });
    }

    public void updateUser(User user, Runnable onComplete) {
        executor.execute(() -> {
            userDao.update(user);
            new Handler(Looper.getMainLooper()).post(onComplete);
        });
    }

    public void getUserByEmail(String email, Consumer<User> onResult) {
        executor.execute(() -> {
            User user = userDao.getUserByEmail(email);
            new Handler(Looper.getMainLooper()).post(() -> onResult.accept(user));
        });
    }
}
