package com.ahmadinfotech.moneytracker.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ahmadinfotech.moneytracker.model.User;
import com.google.gson.Gson;

/**
 * Created by root on 15/1/18.
 */

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "AndroidHiveLogin";
    private static final String KEY_USER = "User";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_IS_PIN_SET = "isLoginPINSet";
    private static final String KEY_LOGIN_PIN = "LoginPIN";

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public void setUser(User user){
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString(KEY_USER, json);
        editor.commit();
    }

    public User getUser(){
        Gson gson = new Gson();
        String json = pref.getString(KEY_USER, "");
        User user = gson.fromJson(json, User.class);
        return user;
    }

    public void setPINEnabled(boolean value){
        editor.putBoolean(KEY_IS_PIN_SET, value);
        editor.commit();
        Log.d(TAG, "User login session modified!");
    }

    public boolean isPINEnabled(){
        return pref.getBoolean(KEY_IS_PIN_SET, false);
    }

    public void setLoginPIN(String pin){
        editor.putString(KEY_LOGIN_PIN, pin);
        editor.commit();
    }

    public String getLoginPIN(){
        return pref.getString(KEY_LOGIN_PIN, "");
    }
}