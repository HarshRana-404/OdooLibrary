package com.threebrains.odoolibrary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.models.RequestedModel;

import java.util.ArrayList;

public class IssuedAdapter extends RecyclerView.Adapter<IssuedAdapter.IssuedViewHolder> {

    ArrayList<RequestedModel> alIssued;
    Context context;

    public IssuedAdapter(Context context, ArrayList<RequestedModel> alIssued){
        this.context = context;
        this.alIssued = alIssued;
    }

    @NonNull
    @Override
    public IssuedAdapter.IssuedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_issued_layout, parent, false);
        return new IssuedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssuedAdapter.IssuedViewHolder holder, int position) {
        try{
            holder.tvBookTitle.setText(alIssued.get(position).getTitle());
            holder.tvUsername.setText(alIssued.get(position).getUserName());
            holder.tvIssueDate.setText(alIssued.get(position).getIssueDate());
            holder.tvReturnDate.setText(alIssued.get(position).getReturnDate());
            holder.tvDueDate.setText(alIssued.get(position).getDueDate());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return alIssued.size();
    }

    public class IssuedViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvUsername, tvIssueDate, tvReturnDate, tvDueDate;
        public IssuedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tv_book_title);
            tvUsername = itemView.findViewById(R.id.tv_user_name);
            tvIssueDate = itemView.findViewById(R.id.tv_issued_date);
            tvReturnDate = itemView.findViewById(R.id.tv_return_date);
            tvDueDate = itemView.findViewById(R.id.tv_due_date);
        }
    }
}
