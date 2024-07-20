package com.threebrains.odoolibrary.utilities;

import com.google.firebase.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Constants {
    public static String UID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
    public static String  ROLE="";
}
