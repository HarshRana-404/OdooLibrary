package com.threebrains.odoolibrary.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.threebrains.odoolibrary.R;

public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    String Email,UserName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragProfile = inflater.inflate(R.layout.fragment_profile, container, false);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        return fragProfile;
    }
    public void fetchProfile(){
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            firestore.collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        tvUsername.setText(document.getString("username"));
//                        tvEmail.setText(document.getString("email"));
                    } else {
                        Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error getting user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    }
