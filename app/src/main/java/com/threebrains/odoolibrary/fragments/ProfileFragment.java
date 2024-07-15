package com.threebrains.odoolibrary.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.auth.Login;

public class ProfileFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    TextView Email, UserName;
    LinearLayout Logout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragProfile = inflater.inflate(R.layout.layout_profile, container, false);
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        UserName = fragProfile.findViewById(R.id.tv_user_name);
        Email = fragProfile.findViewById(R.id.tv_email);
        Logout = fragProfile.findViewById(R.id.ll_logout);
        fetchProfile();
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder adb = new AlertDialog.Builder(requireContext());
                AlertDialog ad = adb.create();
                adb.setTitle("Logout?");
                adb.setMessage("Are you sure you want to logout?");
                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        startActivity(new Intent(getActivity(), Login.class));
                    }
                });
                adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ad.dismiss();
                    }
                });
                adb.show();


            }
        });

        return fragProfile;
    }


    public void fetchProfile() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            firestore.collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        UserName.setText(document.getString("username"));
                        Email.setText(document.getString("email"));
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
