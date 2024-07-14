package com.threebrains.odoolibrary;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddLibrarianActivity extends AppCompatActivity {
    private TextInputEditText etUsername, etEmail, etPassword, etConfirmPassword;
    private Button btnAddLibrarian;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_librarian);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
       user = mAuth.getCurrentUser();
        etUsername = findViewById(R.id.et_username);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnAddLibrarian = findViewById(R.id.btn_sign_up);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        btnAddLibrarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();

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

        Map<String, Object> librarian = new HashMap<>();
        librarian.put("username", username);
        librarian.put("email", email);
        librarian.put("role", "Librarian");
        db.collection("users").document(user.getUid())
                .set(librarian).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()){
                            Toast.makeText(AddLibrarianActivity.this, "Librarian added", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddLibrarianActivity.this, "Error adding librarian: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.w("AddLibrarianActivity", "Error adding document", e);
                    }
                });

    }
}