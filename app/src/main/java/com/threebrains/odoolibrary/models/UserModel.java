package com.threebrains.odoolibrary.models;

public class UserModel {
    String email;
    String role;
    String username;

    public UserModel(String email, String role, String username){
        this.email = email;
        this.role = role;
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public String getUsername() {
        return username;
    }
}
