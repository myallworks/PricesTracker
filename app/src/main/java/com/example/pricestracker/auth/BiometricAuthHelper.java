package com.example.pricestracker.auth;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.pricestracker.R;

import java.util.concurrent.Executor;

public class BiometricAuthHelper {
    private final Context context;
    private final BiometricAuthCallback callback;

    public BiometricAuthHelper(Context context, BiometricAuthCallback callback) {
        this.context = context;
        this.callback = callback;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    public void showBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(context);
        BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                callback.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                callback.onAuthenticationSuccess();
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(context, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(context.getString(R.string.biometric_auth_title))
                .setSubtitle(context.getString(R.string.biometric_auth_subtitle))
                .setNegativeButtonText(context.getString(R.string.cancel))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }

    public interface BiometricAuthCallback {
        void onAuthenticationSuccess();

        void onAuthenticationError(int errorCode, CharSequence errString);
    }
}