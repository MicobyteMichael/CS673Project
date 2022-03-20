package com.example.healthapp.backend.auth;

import android.util.Patterns;

import java.util.Locale;

public interface AuthDataValidator {

    public static String validate(String user, String email, String phone, String pass, String pass2) {
        if(user  != null && !checkUsername(user))         return "Username too short!";
        if(email != null && !checkEmail(email))           return "Invalid email address!";
        if(phone != null && !checkPhone(phone))           return "Invalid phone number!";
        if(pass  != null && !checkPassword(pass))         return "Password too short!";
        if(pass2 != null && !checkPasswords(pass, pass2)) return "Passwords don't match!";

        return null;
    }

    public static boolean checkPasswords(String password, String password2) {
        return password.equals(password2);
    }

    public static boolean checkPassword(String password) {
        return password.length() >= 10;
    }

    public static boolean checkUsername(String username) {
        return username.length() >= 10;
    }

    public static boolean checkEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean checkPhone(String phone) {
        return Patterns.PHONE.matcher(phone).matches();
    }

    public static String stripUsername(String username) {
        return username.toLowerCase(Locale.ROOT);
    }

    public static String stripEmail(String email) {
        return email.toLowerCase(Locale.ROOT);
    }

    public static String stripPhone(String phone) {
        String phoneFiltered = "";
        for(char ch : phone.toCharArray()) if(Character.isDigit(ch)) phoneFiltered += ch;

        return phoneFiltered;
    }
}
