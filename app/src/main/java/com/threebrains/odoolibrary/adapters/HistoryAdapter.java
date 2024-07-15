package com.threebrains.odoolibrary.adapters;

import android.annotation.SuppressLint;
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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.IssuedViewHolder> {

    ArrayList<RequestedModel> alHistory;
    Context context;

    public HistoryAdapter(Context context, ArrayList<RequestedModel> alHistory){
        this.context = context;
        this.alHistory = alHistory;
    }

    @NonNull
    @Override
    public HistoryAdapter.IssuedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_layout, parent, false);
        return new IssuedViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.IssuedViewHolder holder, int position) {
        try{
            holder.tvBookTitle.setText(alHistory.get(position).getTitle());
            holder.tvRequestDate.setText(alHistory.get(position).getRequestDate());
            holder.tvIssueDate.setText(alHistory.get(position).getIssueDate());
            if(alHistory.get(position).getReturnDate().equals("-")){
                holder.tvDueReturn.setText("Due Date:");
                holder.tvDueReturnDate.setText(alHistory.get(position).getDueDate());
                holder.tvDueReturnDate.setTextColor(context.getResources().getColor(R.color.red));
            }else{
                holder.tvDueReturn.setText("Return Date:");
                holder.tvDueReturnDate.setText(alHistory.get(position).getReturnDate());
                holder.tvDueReturnDate.setTextColor(context.getResources().getColor(R.color.green));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return alHistory.size();
    }

    public class IssuedViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvRequestDate, tvIssueDate, tvDueReturn, tvDueReturnDate;
        public IssuedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tv_book_title);
            tvRequestDate = itemView.findViewById(R.id.tv_request_date);
            tvIssueDate = itemView.findViewById(R.id.tv_issued_date);
            tvDueReturn = itemView.findViewById(R.id.tv_due_return);
            tvDueReturnDate = itemView.findViewById(R.id.tv_due_return_date);
        }
    }
}
