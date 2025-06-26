package com.example.clothesshopapp.Data.Local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.clothesshopapp.Data.Local.Dao.UserDao;
import com.example.clothesshopapp.Data.Local.Entity.User;

import org.jspecify.annotations.NonNull;
import org.mindrot.jbcrypt.BCrypt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Database(entities = {User.class}, version = 1, exportSchema = false)
@Singleton
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    private static volatile AppDatabase INSTANCE;

    @Module
    @InstallIn(SingletonComponent.class)
    public static class AppDatabaseModule {
        @Provides
        @Singleton
        public static AppDatabase provideAppDatabase(@ApplicationContext Context context) {
            if (INSTANCE == null) {
                synchronized (AppDatabase.class) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                        AppDatabase.class, "clothesShop_db")
                                .addCallback(new RoomDatabase.Callback() {
                                    @Override
                                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                        super.onCreate(db);
                                    }
                                })
                                .build();
                        initializeAdminUser(context);
                    }
                }
            }
            return INSTANCE;
        }

        private static void initializeAdminUser(Context context) {
            AppDatabase db = INSTANCE;
            if (db != null) {
                new Thread(() -> {
                    UserDao userDao = db.userDao();
                    if (userDao.getUserByEmail("admin123@admin.com") == null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy-HH:mm:ss", Locale.getDefault());
                        String hashedPassword = BCrypt.hashpw("admin!123", BCrypt.gensalt());
                        User admin = new User(
                                "admin123@admin.com",
                                "Admin",
                                hashedPassword,
                                "",
                                sdf.format(new Date()),
                                "admin"
                        );
                        userDao.insert(admin);
                    }
                }).start();
            }
        }
    }
}