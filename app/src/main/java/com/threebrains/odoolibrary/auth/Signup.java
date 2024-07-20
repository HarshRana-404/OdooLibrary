package com.threebrains.odoolibrary.auth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.threebrains.odoolibrary.R;

import java.util.HashMap;
import java.util.Map;

public class Signup extends AppCompatActivity {

    private TextInputEditText etUsername, etEmail, etPassword, etConfirmPassword;
    private AutoCompleteTextView etState;
    private Button btnSignUp;
    private TextView tvLogin;
    ProgressDialog SignuprogressDialog;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    TextInputLayout confirmPasswordLayout, PasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getWindow().setNavigationBarColor(getColor(R.color.lighter));

        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnSignUp = findViewById(R.id.btn_sign_up);
        tvLogin = findViewById(R.id.tv_login);
        PasswordLayout = findViewById(R.id.et_password_layout);
        confirmPasswordLayout = findViewById(R.id.et_confirm_password_layout);
        SignuprogressDialog = new ProgressDialog(this);
        SignuprogressDialog.setCancelable(false);
        SignuprogressDialog.setTitle("Sign up");
        SignuprogressDialog.setMessage("please wait...");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    etUsername.setError("Username is required");
                    etUsername.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Email is required.");
                    etEmail.requestFocus();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Please enter a valid email address.");
                    etEmail.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    etPassword.setError("Password is required.");
                    hidePasswordTint();
                    return;
                }

                if (password.length() < 8) {
                    etPassword.setError("Password must be at least 8 characters long.");
                    hidePasswordTint();
                    return;
                }

                String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
                if (!password.matches(passwordPattern)) {
                    etPassword.setError("Password must contain at least one uppercase, one lowercase, one number, and one special character.");
                    hidePasswordTint();
                    return;
                }

                if (TextUtils.isEmpty(confirmPassword)) {
                    etConfirmPassword.setError("Confirm Password is required.");
                    etConfirmPassword.requestFocus();
                    hideTint();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    etConfirmPassword.setError("Passwords do not match.");
                    etConfirmPassword.requestFocus();
                    hideTint();
                    return;
                }


                registerUser(username, email, password, confirmPassword);
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to login activity
                finish();
            }
        });
    }

    private void registerUser(String username, String email, String password, String ConfirmPassword) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Save additional user information to Firestore
                            Map<String, Object> userDetails = new HashMap<>();
                            userDetails.put("username", username);
                            userDetails.put("email", email);
                            userDetails.put("role", "User");
                            db.collection("users").document(user.getUid())
                                    .set(userDetails)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(Signup.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                                        updateUI(user);
                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(Signup.this, "Failed to save user details.", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(Signup.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                            SignuprogressDialog.dismiss();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Navigate to main activity or dashboard
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
    }

    public void hideTint() {
        confirmPasswordLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
    }

    public void hidePasswordTint() {
        PasswordLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
    }
}
