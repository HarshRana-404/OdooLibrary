package com.threebrains.odoolibrary.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.threebrains.odoolibrary.BookDetailsActivity;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.models.BookModel;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    Context context;
    ArrayList<BookModel> alBooks;

    public BookAdapter(Context context, ArrayList<BookModel> alBooks) {
        this.context = context;
        this.alBooks = alBooks;
    }

    @NonNull
    @Override
    public BookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ViewHolder holder, int position) {
        BookModel book = alBooks.get(position);

        holder.tvBookTitle.setText(book.getTitle());
        holder.tvBookAuthor.setText(book.getAuthor());
        holder.tvBookYear.setText(book.getYear());
        holder.tvBookPublisher.setText(book.getPublisher());
        holder.tvBookGenre.setText(book.getGenre());

        if (book.getQuantity() == 0){
            holder.tvBookQuantity.setText(String.valueOf("Not Available"));
            holder.tvBookQuantity.setTextColor(context.getResources().getColor(R.color.red));
        }else {
            holder.tvBookQuantity.setText(String.valueOf(book.getQuantity() + " Available"));
            holder.tvBookQuantity.setTextColor(context.getResources().getColor(R.color.green));
        }
        holder.tvBookDescription.setText(book.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, BookDetailsActivity.class);
                intent.putExtra("bookIsbn", book.getIsbn());
                intent.putExtra("bookTitle", book.getTitle());
                intent.putExtra("bookDescription", book.getDescription());
                intent.putExtra("bookAuthor", book.getAuthor());
                intent.putExtra("bookYear", book.getYear());
                intent.putExtra("bookPublisher", book.getPublisher());
                intent.putExtra("bookGenre", book.getGenre());
                intent.putExtra("bookQuantity", book.getQuantity());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return alBooks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView sivBookImg;
        TextView tvBookTitle, tvBookAuthor, tvBookYear, tvBookPublisher, tvBookGenre, tvBookQuantity, tvBookDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sivBookImg = itemView.findViewById(R.id.siv_book_img);
            tvBookTitle = itemView.findViewById(R.id.tv_book_title);
            tvBookAuthor = itemView.findViewById(R.id.tv_book_author);
            tvBookYear = itemView.findViewById(R.id.tv_book_year);
            tvBookPublisher = itemView.findViewById(R.id.tv_book_publisher);
            tvBookGenre = itemView.findViewById(R.id.tv_book_genre);
            tvBookQuantity = itemView.findViewById(R.id.tv_book_quantity);
            tvBookDescription = itemView.findViewById(R.id.tv_book_description);
        }
    }
}
