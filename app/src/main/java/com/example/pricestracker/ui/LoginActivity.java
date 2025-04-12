package com.example.pricestracker.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import com.example.pricestracker.R;
import com.example.pricestracker.api.ApiClient;
import com.example.pricestracker.api.ApiInterface;
import com.example.pricestracker.auth.BiometricAuthHelper;
import com.example.pricestracker.auth.TokenManager;
import com.example.pricestracker.models.LoginReq;
import com.example.pricestracker.models.LoginResponse;
import com.example.pricestracker.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements BiometricAuthHelper.BiometricAuthCallback {
    private EditText etUsername, etPassword;
    private CheckBox cbRememberMe;
    private TokenManager tokenManager;
    private BiometricAuthHelper biometricAuthHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tokenManager = new TokenManager(this);
        biometricAuthHelper = new BiometricAuthHelper(this, this);

        initializeViews();
        setupLoginButton();
        attemptBiometricLogin();
    }

    private void initializeViews() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);

        etUsername.setText("admin");
        etPassword.setText("A7ge#hu&dt(wer");
    }

    private void setupLoginButton() {
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (validateInputs(username, password)) {
                if (!NetworkUtils.isNetworkAvailable(this)) {
                    Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginUser(username, password);
            }
        });
    }

    private boolean validateInputs(String username, String password) {
        if (username.isEmpty()) {
            etUsername.setError("Username cannot be empty");
            return false;
        }
        if (password.isEmpty()) {
            etPassword.setError("Password cannot be empty");
            return false;
        }
        return true;
    }

    private void loginUser(String username, String password) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoginResponse> call = apiInterface.login(new LoginReq(username, password));

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleSuccessfulLogin(response.body().getToken());
                } else {
                    Toast.makeText(LoginActivity.this,
                            "Login failed: Invalid credentials",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Network error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleSuccessfulLogin(String token) {
        tokenManager.saveToken(token);
        tokenManager.setBiometricEnabled(cbRememberMe.isChecked());

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (tokenManager.isBiometricEnabled()) {
            authenticateWithBiometrics();
        }
    }

    private void authenticateWithBiometrics() {
        BiometricAuthHelper biometricAuthHelper = new BiometricAuthHelper(this, this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            biometricAuthHelper.showBiometricPrompt();
        }
    }

    private void attemptBiometricLogin() {
        if (tokenManager.getToken() != null && tokenManager.isBiometricEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                biometricAuthHelper.showBiometricPrompt();
            }
        }
    }

    @Override
    public void onAuthenticationSuccess() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        if (errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
            Toast.makeText(this, "Biometric error: " + errString, Toast.LENGTH_SHORT).show();
        }
    }
}