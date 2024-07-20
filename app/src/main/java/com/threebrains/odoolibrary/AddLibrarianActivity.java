package com.threebrains.odoolibrary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddLibrarianActivity extends AppCompatActivity {
    TextInputEditText etUsername, etEmail, etPassword, etConfirmPassword;
    Button btnAddLibrarian;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser adminUser;
    String username, email, password, confirmPassword;
    Map<String, Object> librarian = new HashMap<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_librarian);

        try{
            etUsername = findViewById(R.id.et_librarian_username);
            etEmail = findViewById(R.id.et_librarian_email);
            etPassword = findViewById(R.id.et_librarian_password);
            etConfirmPassword = findViewById(R.id.et_librarian_confirm_password);
            btnAddLibrarian = findViewById(R.id.btn_add_librarian);
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();
            adminUser = mAuth.getCurrentUser();
        } catch (Exception e) {
            Toast.makeText(this, e+"", Toast.LENGTH_SHORT).show();
        }

        btnAddLibrarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUsername.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                confirmPassword = etConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    etUsername.setError("Name is required.");
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
                    etPassword.requestFocus();
                    return;
                }

                if (password.length() < 8) {
                    etPassword.setError("Password must be at least 8 characters long.");
                    etPassword.requestFocus();
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    etConfirmPassword.setError("Passwords do not match.");
                    etConfirmPassword.requestFocus();
                    return;
                }

                addLibrarianToFirestore(username, email);
            }
        });

    }

    private void addLibrarianToFirestore(String username, String email) {
        librarian.put("username", username);
        librarian.put("email", email);
        librarian.put("role", "Librarian");
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    db.collection("users").document(mAuth.getCurrentUser().getUid())
                            .set(librarian).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {
                                        mAuth.updateCurrentUser(adminUser);
                                        Toast.makeText(AddLibrarianActivity.this, "Librarian added!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddLibrarianActivity.this, "Cannot add librarian!", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Log.w("AddLibrarianActivity", "Error adding document", e);
                                }
                            });
                }
            }
        });

    }
}