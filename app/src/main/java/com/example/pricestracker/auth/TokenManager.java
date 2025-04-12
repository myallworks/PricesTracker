package com.example.pricestracker.auth;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import com.example.pricestracker.utils.Constants;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class TokenManager {
    private final SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            sharedPreferences = EncryptedSharedPreferences.create(
                    context,
                    Constants.SHARED_PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Failed to create encrypted shared preferences", e);
        }
    }

    public void saveToken(String token) {
        sharedPreferences.edit().putString(Constants.AUTH_TOKEN_KEY, token).apply();
    }

    public String getToken() {
        return sharedPreferences.getString(Constants.AUTH_TOKEN_KEY, null);
    }

    public void clearToken() {
        sharedPreferences.edit().remove(Constants.AUTH_TOKEN_KEY).remove(Constants.BIOMETRIC_ENABLED_KEY).apply();
    }

    public boolean isBiometricEnabled() {
        return sharedPreferences.getBoolean(Constants.BIOMETRIC_ENABLED_KEY, false);
    }

    public void setBiometricEnabled(boolean enabled) {
        sharedPreferences.edit().putBoolean(Constants.BIOMETRIC_ENABLED_KEY, enabled).apply();
    }
}