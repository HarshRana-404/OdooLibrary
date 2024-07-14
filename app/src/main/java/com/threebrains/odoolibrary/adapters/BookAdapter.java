package com.threebrains.odoolibrary.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.threebrains.odoolibrary.BookDetailsActivity;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.models.BookModel;
import com.threebrains.odoolibrary.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    Context context;
    ArrayList<BookModel> alBooks;

    FirebaseFirestore fbStore;
    String currentDate = "", dueDate = "";

    public BookAdapter(Context context, ArrayList<BookModel> alBooks) {
        this.context = context;
        this.alBooks = alBooks;

        fbStore = FirebaseFirestore.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = sdf.format(Calendar.getInstance().getTime());
        Calendar calDue = Calendar.getInstance();
        calDue.add(Calendar.DATE, 15);
        dueDate = sdf.format(calDue.getTime());
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
        holder.tvBookQuantity.setText(String.valueOf(book.getQuantity() + "Available"));
        holder.tvBookDescription.setText(book.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                AlertDialog alertDialog = builder.create();
                alertDialog.setTitle("Request book");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Request", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestBook(book);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return alBooks.size();
    }

    public void requestBook(BookModel book){
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();

        HashMap<String, String> hmRequests = new HashMap<>();
        hmRequests.put("isbn", book.getIsbn());
        hmRequests.put("title", book.getTitle());
        hmRequests.put("uid", Constants.UID);
        hmRequests.put("username", email);
        hmRequests.put("requestdate", currentDate);
        hmRequests.put("issuedate", "-");
        hmRequests.put("duedate", dueDate);
        hmRequests.put("returndate", "-");
        hmRequests.put("status", "pending");
        fbStore.collection("requested").document().set(hmRequests);
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
