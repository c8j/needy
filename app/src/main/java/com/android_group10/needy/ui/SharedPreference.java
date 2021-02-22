package com.android_group10.needy.ui;

import android.content.Context;

import android.content.SharedPreferences;

import java.util.HashMap;


public class SharedPreference {
    SharedPreferences usersSession;
    //To edit the shared preferences
    SharedPreferences.Editor editor;
    // context pass the reference to another class
    Context context;

    //This is user session variables
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String IS_LOGIN = "isLoggedIn";
    public static final String KEY_USERSESSION = "userLoginSession";
    public static final String SESSION_REMEMBERME = "rememberMe";

    //Remember me variables
    private static final String IS_REMEMBERME = "isRememberMe";
    public static final String KEY_SESSION_EMAIL = "email";
    public static final String KEY_SESSION_PASSWORD = "password";


    public SharedPreference(Context context, String sessionName) {
        this.context = context;
        usersSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        editor = usersSession.edit();
    }

    public void createRememberMeSession(String email, String password) {
        editor.putBoolean(IS_REMEMBERME, true);
        editor.putString(KEY_SESSION_EMAIL, email);
        editor.putString(KEY_SESSION_PASSWORD, password);

        editor.commit();
    }

    public HashMap<String, String> getRememberMeDetailsFromSession() {

        HashMap<String, String> userData = new HashMap<>();

        userData.put(KEY_SESSION_EMAIL, usersSession.getString(KEY_SESSION_EMAIL, null));
        userData.put(KEY_PASSWORD, usersSession.getString(KEY_SESSION_PASSWORD, null));

        return userData;
    }

    public boolean checkRememberMe() {
        if (usersSession.getBoolean(IS_REMEMBERME, false)) {
            return true;
        } else {
            return false;
        }
    }
}
