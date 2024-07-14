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

public class RequestedAdapter extends RecyclerView.Adapter<RequestedAdapter.IssuedViewHolder> {

    ArrayList<RequestedModel> alRequested;
    Context context;

    public RequestedAdapter(Context context, ArrayList<RequestedModel> alRequested){
        this.context = context;
        this.alRequested = alRequested;
    }

    @NonNull
    @Override
    public RequestedAdapter.IssuedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_issued_layout, parent, false);
        return new IssuedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedAdapter.IssuedViewHolder holder, int position) {
        try{
            holder.tvBookTitle.setText(alRequested.get(position).getTitle());
            holder.tvUsername.setText(alRequested.get(position).getUserName());
            holder.tvIssueDate.setText(alRequested.get(position).getIssueDate());
            holder.tvReturnDate.setText(alRequested.get(position).getReturnDate());
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
        public IssuedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tv_book_title);
            tvUsername = itemView.findViewById(R.id.tv_user_name);
            tvIssueDate = itemView.findViewById(R.id.tv_issued_date);
            tvReturnDate = itemView.findViewById(R.id.tv_return_date);
        }
    }
}
