package com.threebrains.odoolibrary.fragments;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.adapters.HistoryAdapter;
import com.threebrains.odoolibrary.models.RequestedModel;
import com.threebrains.odoolibrary.utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    RecyclerView rvHistory;
    HistoryAdapter historyAdapter;
    ArrayList<RequestedModel> alHistory = new ArrayList<>();
    FirebaseFirestore fbStore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragHistory = inflater.inflate(R.layout.fragment_history, container, false);
        try{

            fbStore = FirebaseFirestore.getInstance();
            rvHistory = fragHistory.findViewById(R.id.rv_history);
            rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
            historyAdapter = new HistoryAdapter(requireContext(), alHistory);
            rvHistory.setAdapter(historyAdapter);
            getHistory();

        } catch (Exception e) {

        }
        return fragHistory;
    }
    public void getHistory(){
        try{
            Task<QuerySnapshot> qs = fbStore.collection("requested").whereEqualTo("uid", Constants.UID).get();
            qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @SuppressLint("NotifyDataSetChanged")
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    try{
                        List<DocumentSnapshot> books = task.getResult().getDocuments();
                        for(DocumentSnapshot book : books){
                            alHistory.add(new RequestedModel(book.getString("isbn"), book.getString("title"), book.getString("uid"), book.getString("username"), book.getString("requestdate"), book.getString("issuedate"), book.getString("duedate"), book.getString("returndate"), book.getString("status")));
                        }
                        historyAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }
}