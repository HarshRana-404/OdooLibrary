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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.adapters.IssuedAdapter;
import com.threebrains.odoolibrary.models.RequestedModel;

import java.util.ArrayList;
import java.util.List;

public class IssuedFragment extends Fragment {

    RecyclerView rvIssued;
    IssuedAdapter issuedAdapter;
    ArrayList<RequestedModel> alIssued = new ArrayList<>();
    FirebaseFirestore fbStore;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragIssued = inflater.inflate(R.layout.fragment_issued, container, false);
        try{

            fbStore = FirebaseFirestore.getInstance();
            rvIssued = fragIssued.findViewById(R.id.rv_issued);
            rvIssued.setLayoutManager(new LinearLayoutManager(requireContext()));
            issuedAdapter = new IssuedAdapter(requireContext(), alIssued);
            rvIssued.setAdapter(issuedAdapter);
            getIssued();

        } catch (Exception e) {

        }
        return fragIssued;
    }

    public void getIssued(){
        try{
            Task<QuerySnapshot> qs = fbStore.collection("requested").whereEqualTo("status", "approved").get();
            qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    try{
                        List<DocumentSnapshot> books = task.getResult().getDocuments();
                        int i=0;
                        for(DocumentSnapshot book : books){
                            alIssued.add(new RequestedModel(book.getString("isbn"), book.getString("title"), book.getString("uid"), book.getString("username"), book.getString("requestdate"), book.getString("issuedate"), book.getString("duedate"), book.getString("returndate"), book.getString("status")));
                            alIssued.get(i).setDocId(book.getId());
                            i++;
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