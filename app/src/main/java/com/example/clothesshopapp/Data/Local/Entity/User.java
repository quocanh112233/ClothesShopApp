package com.example.clothesshopapp.Data.Local.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String email;
    private String username;
    private String password;
    private String profileImage;
    private String createdDate;
    private String role;

    // Constructor
    public User(String email, String username, String password, String profileImage, String createdDate, String role) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.profileImage = profileImage;
        this.createdDate = createdDate;
        this.role = role;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }
    public String getCreatedDate() { return createdDate; }
    public void setCreatedDate(String createdDate) { this.createdDate = createdDate; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
