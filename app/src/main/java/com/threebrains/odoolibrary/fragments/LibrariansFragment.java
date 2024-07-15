package com.threebrains.odoolibrary.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.adapters.IssuedAdapter;
import com.threebrains.odoolibrary.models.RequestedModel;
import com.threebrains.odoolibrary.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class LibrariansFragment extends Fragment {

    RecyclerView rvIssued;
    IssuedAdapter issuedAdapter;
    ArrayList<UserModel> alIssued = new ArrayList<>();
    FirebaseFirestore fbStore;

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
//            rvIssued = fragIssued.findViewById(R.id.rv_issued);
            rvIssued.setLayoutManager(new LinearLayoutManager(requireContext()));
//            issuedAdapter = new IssuedAdapter(requireContext(), );
            rvIssued.setAdapter(issuedAdapter);
//            getIssued();
        } catch (Exception e) {
        }

        return fragLibrarians;
    }
    public void getLibrarians(){
        try{
            Task<QuerySnapshot> qs = fbStore.collection("requested").whereEqualTo("role", "Librarian").get();
            qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    try{
                        List<DocumentSnapshot> books = task.getResult().getDocuments();
                        for(DocumentSnapshot book : books){
//                            alIssued.add(new RequestedModel(book.getString("isbn"), book.getString("title"), book.getString("uid"), book.getString("username"), book.getString("requestdate"), book.getString("issuedate"), book.getString("duedate"), book.getString("returndate"), book.getString("status")));
                        }
                        issuedAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }
}