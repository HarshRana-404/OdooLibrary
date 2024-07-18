package com.threebrains.odoolibrary.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.auth.Login;
import com.threebrains.odoolibrary.models.RequestedModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RequestedAdapter extends RecyclerView.Adapter<RequestedAdapter.IssuedViewHolder> {

    ArrayList<RequestedModel> alRequested;
    Context context;
    FirebaseFirestore fbStore;
    int quantity=0, issueCount=0;
    String booksDocId;

    public RequestedAdapter(Context context, ArrayList<RequestedModel> alRequested){
        this.context = context;
        this.alRequested = alRequested;
        fbStore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public RequestedAdapter.IssuedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request_layout, parent, false);
        return new IssuedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedAdapter.IssuedViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            RequestedModel requestedModel = alRequested.get(position);
            holder.tvBookTitle.setText(requestedModel.getTitle());
            holder.tvUsername.setText(requestedModel.getUserName());
            holder.tvIssueDate.setText(requestedModel.getIssueDate());
            holder.tvReturnDate.setText(requestedModel.getReturnDate());
            String requestedDocId = requestedModel.getDocId();
            holder.btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(context);
                    AlertDialog ad = adb.create();
                    adb.setTitle("Approve?");
                    adb.setMessage("Do you want to approve request for "+requestedModel.getTitle()+" ?");
                    adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String currentDate = sdf.format(Calendar.getInstance().getTime());
                            Calendar calDue = Calendar.getInstance();
                            calDue.add(Calendar.DATE, 15);
                            String dueDate = sdf.format(calDue.getTime());
                            Task<QuerySnapshot> qs = fbStore.collection("books").whereEqualTo("isbn", requestedModel.getIsbn()).get();
                            qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    List<DocumentSnapshot> books = task.getResult().getDocuments();
                                    for(DocumentSnapshot book : books){
                                        booksDocId = book.getId();
                                        if(book.getString("title").equals(requestedModel.getTitle())){
                                            quantity = Integer.parseInt(book.getString("quantity"));
                                            issueCount = Integer.parseInt(book.getString("issuecount"));
                                            fbStore.collection("requested").document(requestedDocId).update("status","approved").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    fbStore.collection("requested").document(requestedDocId).update("issuedate",currentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            fbStore.collection("requested").document(requestedDocId).update("duedate", dueDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    fbStore.collection("books").document(booksDocId).update("quantity", String.valueOf((quantity-1))).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            fbStore.collection("books").document(booksDocId).update("issuecount", String.valueOf((issueCount+1))).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                                @SuppressLint("NotifyDataSetChanged")
                                                                                @Override
                                                                                public void onSuccess(Void unused) {
                                                                                    Toast.makeText(context, "Book request approved!", Toast.LENGTH_SHORT).show();
                                                                                    alRequested.remove(position);
                                                                                    notifyDataSetChanged();
                                                                                }
                                                                            });
                                                                        }
                                                                    });
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                    }
                                }
                            });
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return alRequested.size();
    }

    public class IssuedViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvUsername, tvIssueDate, tvReturnDate;
        Button btnApprove;
        public IssuedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tv_book_title);
            tvUsername = itemView.findViewById(R.id.tv_user_name);
            tvIssueDate = itemView.findViewById(R.id.tv_issued_date);
            tvReturnDate = itemView.findViewById(R.id.tv_return_date);
            btnApprove = itemView.findViewById(R.id.btn_approve);
        }
    }
}
