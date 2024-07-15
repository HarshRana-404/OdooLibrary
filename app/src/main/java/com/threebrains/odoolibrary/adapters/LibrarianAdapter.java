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
import com.threebrains.odoolibrary.models.UserModel;

import java.util.ArrayList;

public class LibrarianAdapter extends RecyclerView.Adapter<LibrarianAdapter.IssuedViewHolder> {

    ArrayList<UserModel> alLibrarian;
    Context context;

    public LibrarianAdapter(Context context, ArrayList<UserModel> alLibrarian){
        this.context = context;
        this.alLibrarian = alLibrarian;
    }

    @NonNull
    @Override
    public LibrarianAdapter.IssuedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_issued_layout, parent, false);
        return new IssuedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibrarianAdapter.IssuedViewHolder holder, int position) {
        try{
            holder.tvLibrarianName.setText(alLibrarian.get(position).getUsername());
            holder.tvLibrarianEmail.setText(alLibrarian.get(position).getEmail());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getItemCount() {
        return alLibrarian.size();
    }

    public class IssuedViewHolder extends RecyclerView.ViewHolder {
        TextView tvLibrarianName, tvLibrarianEmail;
        @SuppressLint("CutPasteId")
        public IssuedViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLibrarianName = itemView.findViewById(R.id.tv_librarian_name);
            tvLibrarianEmail = itemView.findViewById(R.id.tv_librarian_name);
        }
    }
}
