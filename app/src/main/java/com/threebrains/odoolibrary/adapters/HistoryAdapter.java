package com.threebrains.odoolibrary.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.models.RequestedModel;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.IssuedViewHolder> {

    ArrayList<RequestedModel> alHistory;
    Context context;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    public HistoryAdapter(Context context, ArrayList<RequestedModel> alHistory){
        this.context = context;
        this.alHistory = alHistory;

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
    }

    @NonNull
    @Override
    public HistoryAdapter.IssuedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_layout, parent, false);
        return new IssuedViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor", "UseCompatLoadingForDrawables"})
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.IssuedViewHolder holder, int position) {
        try{
            holder.progressBar.setVisibility(View.VISIBLE);
            RequestedModel rm = alHistory.get(position);
            String title = rm.getTitle();
            String requestDate = rm.getRequestDate();
            String issueDate = rm.getIssueDate();
            String dueDate = rm.getDueDate();
            String returnDate = rm.getReturnDate();
            String status = rm.getStatus();
            String temp[] = requestDate.split("-");
            requestDate = temp[2]+"-"+temp[1]+"-"+temp[0];
            holder.tvBookTitle.setText(title);
            holder.tvRequestDate.setText(requestDate);

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

            if(status.equals("pending")){
                holder.tvIssueDate.setText("Pending");
                Drawable drawablePending = context.getDrawable(R.drawable.ic_history);
                drawablePending.setTint(context.getColor(R.color.primary));
                holder.tvIssueDate.setCompoundDrawablesWithIntrinsicBounds(drawablePending, null, null, null);
                holder.tvDueReturn.setVisibility(View.GONE);
                holder.tvDueReturnDate.setVisibility(View.GONE);
            }else if(status.equals("approved")){
                temp = issueDate.split("-");
                issueDate = temp[2]+"-"+temp[1]+"-"+temp[0];
                holder.tvIssueDate.setText(issueDate);

                if(returnDate.equals("-")){
                    temp = dueDate.split("-");
                    dueDate = temp[2]+"-"+temp[1]+"-"+temp[0];
                    holder.tvDueReturn.setText("Due Date:");
                    holder.tvDueReturnDate.setText(dueDate);
                    holder.tvDueReturnDate.setTextColor(context.getColor(R.color.red));
                }else{
                    temp = returnDate.split("-");
                    returnDate = temp[2]+"-"+temp[1]+"-"+temp[0];
                    holder.tvDueReturn.setText("Return Date:");
                    holder.tvDueReturnDate.setText(returnDate);
                    holder.tvDueReturnDate.setTextColor(context.getColor(R.color.green));
                }
                holder.tvDueReturn.setVisibility(View.VISIBLE);
                holder.tvDueReturnDate.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return alHistory.size();
    }

    public class IssuedViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;
        ShapeableImageView sivBookImg;
        TextView tvBookTitle, tvRequestDate, tvIssueDate, tvDueReturn, tvDueReturnDate;

        public IssuedViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progress_bar);
            sivBookImg = itemView.findViewById(R.id.siv_book_img);
            tvBookTitle = itemView.findViewById(R.id.tv_book_title);
            tvRequestDate = itemView.findViewById(R.id.tv_request_date);
            tvIssueDate = itemView.findViewById(R.id.tv_issued_date);
            tvDueReturn = itemView.findViewById(R.id.tv_due_return);
            tvDueReturnDate = itemView.findViewById(R.id.tv_due_return_date);
        }
    }
}
