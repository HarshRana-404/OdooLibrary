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
import com.threebrains.odoolibrary.adapters.RequestedAdapter;
import com.threebrains.odoolibrary.models.RequestedModel;

import java.util.ArrayList;
import java.util.List;

public class RequestsFragment extends Fragment {

    RecyclerView rvIssued;
    RequestedAdapter requestedAdapter;
    ArrayList<RequestedModel> alRequested = new ArrayList<>();
    FirebaseFirestore fbStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragRequests = inflater.inflate(R.layout.fragment_requests, container, false);

        try{
            fbStore = FirebaseFirestore.getInstance();
            rvIssued = fragRequests.findViewById(R.id.rv_requested);
            rvIssued.setLayoutManager(new LinearLayoutManager(requireContext()));
            requestedAdapter = new RequestedAdapter(requireContext(), alRequested);
            rvIssued.setAdapter(requestedAdapter);
            getRequested();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return fragRequests;
    }
    public void getRequested(){
        try{
            Task<QuerySnapshot> qs = fbStore.collection("requested").whereEqualTo("status", "pending").get();
            qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    try{
                        List<DocumentSnapshot> books = task.getResult().getDocuments();
                        for(DocumentSnapshot book : books){
                            alRequested.add(new RequestedModel(book.getString("isbn"), book.getString("title"), book.getString("uid"), book.getString("username"), book.getString("requestdate"), book.getString("issuedate"), book.getString("duedate"), book.getString("returndate"), book.getString("status")));
                        }
                        requestedAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }
}