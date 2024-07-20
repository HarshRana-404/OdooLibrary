package com.threebrains.odoolibrary.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.threebrains.odoolibrary.AddLibrarianActivity;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.adapters.LibrarianAdapter;
import com.threebrains.odoolibrary.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class LibrariansFragment extends Fragment {

    RecyclerView rvLibrarian;
    LibrarianAdapter librarianAdapter;
    ArrayList<UserModel> alLibrarian = new ArrayList<>();
    FirebaseFirestore fbStore;
    FloatingActionButton fabAddLibrarian;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragLibrarians = inflater.inflate(R.layout.fragment_librarians, container, false);

        try{
            fbStore = FirebaseFirestore.getInstance();
            rvLibrarian = fragLibrarians.findViewById(R.id.rv_librarians);
            fabAddLibrarian = fragLibrarians.findViewById(R.id.fab_add_librarian);
            rvLibrarian.setLayoutManager(new LinearLayoutManager(requireContext()));
            librarianAdapter = new LibrarianAdapter(requireContext(), alLibrarian);
            rvLibrarian.setAdapter(librarianAdapter);
            getLibrarians();
            fabAddLibrarian.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(requireContext(), AddLibrarianActivity.class));
                }
            });
        } catch (Exception e) {
        }
        return fragLibrarians;
    }
    public void getLibrarians(){
        try{
            Task<QuerySnapshot> qs = fbStore.collection("users").whereEqualTo("role", "Librarian").get();
            qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    try{
                        List<DocumentSnapshot> librarians = task.getResult().getDocuments();
                        for(DocumentSnapshot librarian : librarians){
                            alLibrarian.add(new UserModel(librarian.getString("email"), librarian.getString("role"), librarian.getString("username")));
                        }
                        librarianAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }
}