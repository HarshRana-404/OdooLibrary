package com.threebrains.odoolibrary.auth;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.threebrains.odoolibrary.AdminActivity;
import com.threebrains.odoolibrary.LibrarianActivity;
import com.threebrains.odoolibrary.Splash_Activity;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.UserActivity;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    private FirebaseAuth mAuth;
    ProgressDialog MprogressDialog;
    TextInputLayout PasswordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignup = findViewById(R.id.tv_CreateAc);
        PasswordLayout = findViewById(R.id.tl_password);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //AuthUsers();
        MprogressDialog = new ProgressDialog(this);
        MprogressDialog.setCancelable(false);
        MprogressDialog.setTitle("Login");
        MprogressDialog.setMessage("Please wait...");
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                PasswordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

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
                    etPassword.requestFocus();
                    hidePasswordTint();
                    return;
                }

                if (password.length() < 8) {
                    etPassword.setError("Password must be at least 8 characters long.");
                    etPassword.requestFocus();
                    hidePasswordTint();
                    return;
                }

                loginUser(email, password);
                MprogressDialog.show();


            }
        });


        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Signup.class));
            }
        });
    }

    private void loginUser(String email, String password) {
        try {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    String uid = (Objects.requireNonNull(fAuth.getCurrentUser())).getUid();
                    DocumentReference df = db.collection("users").document(uid);
                    df.get().addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String role = documentSnapshot.getString("role");
                            Log.d("FirestoreData", "role: " + role);
                            if (role != null) {
                                switch (role) {
                                    case "User":
                                        startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                        finish();
                                        break;
                                    case "Librarian":
                                        startActivity(new Intent(getApplicationContext(), LibrarianActivity.class));
                                        finish();
                                        break;
                                    case "Admin":
                                        startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                                        finish();
                                        break;
                                    default:
                                        Toast.makeText(this, "User type not recognized", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut();
                                        startActivity(new Intent(getApplicationContext(), Login.class));
                                        break;
                                }
                            }
                        } else {
                            Log.d("FirestoreData", "No such document");
                            Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), Signup.class));
                            finish();
                        }
                    }).addOnFailureListener(e -> {
                        Log.e("FirestoreError", "Error fetching document", e);
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    });
                    //finish();
                    //finishing Activity from stack so that it will free up some memory.
                }
            }).addOnFailureListener(e -> {
                MprogressDialog.dismiss();
                //Telling user that  login Failed..
                Toast.makeText(Login.this, "Login Failed:" + e, Toast.LENGTH_SHORT).show();
            });
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    public void hidePasswordTint() {
        PasswordLayout.setEndIconMode(TextInputLayout.END_ICON_NONE);
    }

}