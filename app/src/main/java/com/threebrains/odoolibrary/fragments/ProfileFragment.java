package com.threebrains.odoolibrary.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.auth.Login;
import com.threebrains.odoolibrary.utilities.Constants;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    CircleImageView civProfile;
    ImageView ivEditProfile;

    TextView tvEmail, tvUserName;
    LinearLayout llLogout;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragProfile = inflater.inflate(R.layout.fragment_profile, container, false);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        civProfile = fragProfile.findViewById(R.id.civ_profile);
        ivEditProfile = fragProfile.findViewById(R.id.iv_edit_profile);

        tvUserName = fragProfile.findViewById(R.id.tv_user_name);
        tvEmail = fragProfile.findViewById(R.id.tv_email);
        llLogout = fragProfile.findViewById(R.id.ll_logout);

        fetchProfile();

        ivEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        llLogout.setOnClickListener(new View.OnClickListener() {
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
                        tvUserName.setText(document.getString("username"));
                        tvEmail.setText(document.getString("email"));

                        String profilePictureUrl = document.getString("pictureurl");

                        if (!profilePictureUrl.isEmpty()){
                            storageReference.child(profilePictureUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide
                                        .with(requireContext())
                                        .load(uri)
                                        .centerCrop()
                                        .into(civProfile);
                                }
                            });
                        }
                    } else {
                        Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Error getting user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK){
                if (requestCode == 1){
                    Uri selectedImgUri = data.getData();

                    if (selectedImgUri != null){
                        civProfile.setImageURI(selectedImgUri);

                        uploadProfile(selectedImgUri);
                    }else {
                        Toast.makeText(requireContext(), "Null Uri returned!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Edit: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfile(Uri uri){
        storageReference = storageReference.child("ProfilePictures/" + uri.getLastPathSegment());

        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    firestore.collection("users").document(Constants.UID).update("pictureurl", "ProfilePictures/" + uri.getLastPathSegment()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                // code
                            }else {
                                Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}