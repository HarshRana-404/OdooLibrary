package com.threebrains.odoolibrary.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.threebrains.odoolibrary.AddBookActivity;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.adapters.BookAdapter;
import com.threebrains.odoolibrary.models.BookModel;
import com.threebrains.odoolibrary.models.RequestedModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BooksFragment extends Fragment {

    RecyclerView rvBooks;
    BookAdapter bookAdapter;
    FloatingActionButton fabAddBook;

    FirebaseFirestore fbStore;
    HashMap<String, String> hmRequests = new HashMap<>();
    ArrayList<BookModel> alBook;
    ArrayList<RequestedModel> alRequests = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragBooks = inflater.inflate(R.layout.fragment_books, container, false);

        rvBooks = fragBooks.findViewById(R.id.rv_books);
        fabAddBook = fragBooks.findViewById(R.id.fab_add_book);

        try {
            fbStore = FirebaseFirestore.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = sdf.format(Calendar.getInstance().getTime());
            Calendar calDue = Calendar.getInstance();
            calDue.add(Calendar.DATE, 15);
            String dueDate = sdf.format(calDue.getTime());

            rvBooks.setLayoutManager(new LinearLayoutManager(requireContext()));
            alBook = new ArrayList<>();
            bookAdapter = new BookAdapter(requireContext(), alBook);
            rvBooks.setAdapter(bookAdapter);

            getAllBooks();

            fabAddBook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(requireContext(), AddBookActivity.class));
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return fragBooks;
    }

    public void requestBook(String isbn, String title, String uid, String userName, String requestDate, String issueDate, String dueDate, String returnDate, String status){
        hmRequests.put("isbn", isbn);
        hmRequests.put("title", title);
        hmRequests.put("uid", uid);
        hmRequests.put("username", userName);
        hmRequests.put("requestdate", requestDate);
        hmRequests.put("issuedate", issueDate);
        hmRequests.put("duedate", dueDate);
        hmRequests.put("returndate", returnDate);
        hmRequests.put("status", status);
        fbStore.collection("requested").document().set(hmRequests);
    }
    public void getAllRequests(){
        try{
            Task<QuerySnapshot> qs = fbStore.collection("requested").get();
            qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    try{
                        List<DocumentSnapshot> books = task.getResult().getDocuments();
                        for(DocumentSnapshot book : books){
                            alRequests.add(new RequestedModel(book.getString("isbn"), book.getString("title"), book.getString("uid"), book.getString("username"), book.getString("requestdate"), book.getString("issuedate"), book.getString("duedate"), book.getString("returndate"), book.getString("status")));
                        }
                        Toast.makeText(requireContext(), alRequests.size()+"", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }

    public void getAllBooks(){
        try{
            Task<QuerySnapshot> qs = fbStore.collection("books").get();
                qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    try{
                        List<DocumentSnapshot> books = task.getResult().getDocuments();
                        for(DocumentSnapshot book : books){
                            alBook.add(new BookModel(book.getString("isbn"), book.getString("title"), book.getString("description"), book.getString("author"), book.getString("publisher"), book.getString("year"), book.getString("genre"), Integer.parseInt(book.getString("quantity")), Integer.parseInt(book.getString("issuecount")), book.getString("dateadded")));
                        }
                        bookAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }

}