package com.ahmadinfotech.moneytracker.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by root on 17/1/18.
 */

public class AppUtil {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Generates a hashed password of the given string.
     *
     * @param password
     *            String which is to be hashed
     * @return Hashed password
     */
    public static String generateHashedPassword(String password) {
        StringBuffer hashedPassword = new StringBuffer();

        // Get a byte array of the password concatenated with the password salt
        // value
        byte[] defaultBytes = (password + "a secret salt value").getBytes();
        try {
            // Perform the hashing with SHA
            MessageDigest algorithm = MessageDigest.getInstance("SHA");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                hashedPassword.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
        } catch (NoSuchAlgorithmException nsae) {

        }

        return hashedPassword.toString();
    }

}
