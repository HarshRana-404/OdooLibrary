package com.threebrains.odoolibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.threebrains.odoolibrary.auth.Login;
import com.threebrains.odoolibrary.auth.Signup;
import com.threebrains.odoolibrary.utilities.Constants;
import com.threebrains.odoolibrary.utilities.NewRequestBackgroundService;

public class SplashActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore db;
    TextView tvTitle1, tvTitle2;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        fAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        tvTitle1 = findViewById(R.id.tv_splash_title_1);
        tvTitle2 = findViewById(R.id.tv_splash_title_2);

        Animation animForTitle1 = new TranslateAnimation(-400, 0, 0, 0);
        animForTitle1.setDuration(300);
        Animation animForTitle2 = new TranslateAnimation(400, 0, 0, 0);
        animForTitle2.setDuration(300);

        tvTitle1.startAnimation(animForTitle1);
        tvTitle2.startAnimation(animForTitle2);

        getWindow().setNavigationBarColor(getColor(R.color.lighter));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fAuth.getCurrentUser()!=null){
                    try{
                        String uid = (fAuth.getCurrentUser()).getUid();
                        Constants.UID = uid;
                        DocumentReference dRef = db.collection("users").document(uid);
                        dRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                try{
                                    if(task.isComplete()){
                                        String role = task.getResult().getString("role");
                                        Constants.ROLE = role;
                                        if(role.equals("User")) {
                                            startActivity(new Intent(getApplicationContext(), UserActivity.class));
                                            finish();
                                        }else if (role.equals("Librarian")) {
//                                            startService(new Intent(getApplicationContext(), NewRequestBackgroundService.class));
                                            startActivity(new Intent(getApplicationContext(), LibrarianActivity.class));
                                            finish();
                                        }else if (role.equals("Admin")){
                                            startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                                            finish();
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            }
                        });
                    } catch (Exception e) {
                    }
                }else{
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            }
        },400);
    }

}