package com.threebrains.odoolibrary;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.threebrains.odoolibrary.auth.Login;

public class Splash_Activity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               AuthUsers();
                finish();
            }
        },3000);
    }

    private void AuthUsers() {

        if (fAuth.getCurrentUser() == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        } else {
            String uid = (fAuth.getCurrentUser()).getUid();
            DocumentReference df = db.collection("users").document(uid);

            df.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String role = documentSnapshot.getString("role");

                    Log.d("FirestoreData", "role: " + role);

                    assert role != null;
                    if (role.equals("User")) {
                      //  startActivity(new Intent(getApplicationContext(), admin_activity.class));
                        finish();
                    } else if (role.equals("Librarian")) {
                       // startActivity(new Intent(getApplicationContext(), home.class));

                        finish();
                    }else if (role.equals("Admin")){

                    }
                    else {
                        Toast.makeText(this, "User type not recognized", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                } else {
                    Log.d("FirestoreData", "No such document");
                    Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            }).addOnFailureListener(e -> {
                Log.e("FirestoreError", "Error fetching document", e);
                FirebaseAuth.getInstance().signOut();
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                finish();
            });
        }
    }
}