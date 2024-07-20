package com.threebrains.odoolibrary.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.models.RequestedModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class IssuedAdapter extends RecyclerView.Adapter<IssuedAdapter.IssuedViewHolder> {

    ArrayList<RequestedModel> alIssued;
    Context context;
    FirebaseFirestore fbStore;
    int quantity=0;
    String booksDocId = "";

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    public IssuedAdapter(Context context, ArrayList<RequestedModel> alIssued){
        this.context = context;
        this.alIssued = alIssued;

        fbStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @NonNull
    @Override
    public IssuedAdapter.IssuedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_issued_layout, parent, false);
        return new IssuedViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull IssuedAdapter.IssuedViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try{
            RequestedModel rm = alIssued.get(position);
            String issueDate = rm.getIssueDate();
            String dueDate = rm.getDueDate();
            String returnDate = rm.getReturnDate();
            String returnDocId = rm.getDocId();

            holder.progressBar.setVisibility(View.VISIBLE);

            storageReference.child(rm.getBookCoverUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide
                        .with(context)
                        .load(uri)
                        .centerCrop()
                        .into(holder.sivBookImg);

                    holder.progressBar.setVisibility(View.GONE);
                }
            });

            String temp[] = rm.getIssueDate().split("-");
            issueDate = temp[2]+"-"+temp[1]+"-"+temp[0];
            holder.tvIssueDate.setText(issueDate);
            temp = rm.getDueDate().split("-");
            dueDate = temp[2]+"-"+temp[1]+"-"+temp[0];
            holder.tvDueDate.setText(dueDate);
            holder.btnReturned.setVisibility(View.VISIBLE);

            if(returnDate.equals("-")){
                holder.tvReturnDate.setText("Not returned");
                holder.tvReturnDate.setTextColor(context.getResources().getColor(R.color.red));
            }else{
                temp = rm.getReturnDate().split("-");
                returnDate = temp[2]+"-"+temp[1]+"-"+temp[0];
                holder.tvReturnDate.setText(returnDate);
                holder.tvReturnDate.setTextColor(context.getResources().getColor(R.color.green));
                holder.btnReturned.setVisibility(View.GONE);
            }
            holder.tvBookTitle.setText(rm.getTitle());
            holder.tvUsername.setText(rm.getUserName());

            holder.btnReturned.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder adb = new AlertDialog.Builder(context);
                    AlertDialog ad = adb.create();
                    adb.setTitle("Return?");
                    adb.setMessage("Do you want to return "+rm.getTitle()+" ?");
                    adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                            String currentDate = sdf.format(Calendar.getInstance().getTime());
                            Calendar calDue = Calendar.getInstance();
                            calDue.add(Calendar.DATE, 15);
                            String temp[] = currentDate.split("-");
                            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            String currentDateToShow = sdf.format(Calendar.getInstance().getTime());
                            Task<QuerySnapshot> qs = fbStore.collection("books").whereEqualTo("isbn", rm.getIsbn()).get();
                            qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    List<DocumentSnapshot> books = task.getResult().getDocuments();
                                    for(DocumentSnapshot book : books){
                                        booksDocId = book.getId();
                                        if(book.getString("title").equals(rm.getTitle())){
                                            quantity = Integer.parseInt(book.getString("quantity"));
                                            fbStore.collection("requested").document(returnDocId).update("returndate", currentDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    fbStore.collection("books").document(booksDocId).update("quantity", String.valueOf((quantity+1))).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @SuppressLint("NotifyDataSetChanged")
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(context, "Book returned!", Toast.LENGTH_SHORT).show();
                                                            alIssued.set(position, new RequestedModel(rm.getIsbn(), rm.getBookCoverUrl(),rm.getTitle(),rm.getUid(), rm.getUserName(), rm.getRequestDate(), rm.getIssueDate(), rm.getIssueDate(), currentDateToShow, rm.getStatus()));
                                                            holder.tvReturnDate.setTextColor(context.getColor(R.color.green));
                                                            notifyDataSetChanged();
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
        }
    }

    @Override
    public int getItemCount() {
        return alIssued.size();
    }

    public class IssuedViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView sivBookImg;
        ProgressBar progressBar;
        TextView tvBookTitle, tvUsername, tvIssueDate, tvReturnDate, tvDueDate;
        Button btnReturned;

        public IssuedViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progress_bar);
            sivBookImg = itemView.findViewById(R.id.siv_book_img);

            tvBookTitle = itemView.findViewById(R.id.tv_book_title);
            tvUsername = itemView.findViewById(R.id.tv_user_name);
            tvIssueDate = itemView.findViewById(R.id.tv_issued_date);
            tvReturnDate = itemView.findViewById(R.id.tv_return_date);
            tvDueDate = itemView.findViewById(R.id.tv_due_date);
            btnReturned = itemView.findViewById(R.id.btn_returned);
        }
    }
}
