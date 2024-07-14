package com.threebrains.odoolibrary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.models.BookModel;
import com.threebrains.odoolibrary.models.RequestedModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BooksFragment extends Fragment {

    FirebaseFirestore fbStore;
    HashMap<String, String> hmBook = new HashMap<>();
    HashMap<String, String> hmRequests = new HashMap<>();
    ArrayList<BookModel> alBook = new ArrayList<>();
    ArrayList<RequestedModel> alRequests = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragBooks = inflater.inflate(R.layout.fragment_books, container, false);

        try {
            fbStore = FirebaseFirestore.getInstance();
//            addNewBook("ISBN_1","Programming in Java","Java's most preferred book","Author-1","Publisher-1","2018","Learning",5,0,"2024-07-14");
//            getAllBooks();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = sdf.format(Calendar.getInstance().getTime());
            Calendar calDue = Calendar.getInstance();
            calDue.add(Calendar.DATE, 15);
            String dueDate = sdf.format(calDue.getTime());

//            requestBook("ISBN_1", "Title", "uid1", "uname", "2024-07-14", "2024-07-15", "2024-07-30", "2024-07-30","pending");
//            getAllRequests();
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

    public void addNewBook(String isbn, String title, String description, String author, String publisher, String year, String genre, int quantity, int issueCount, String dateAdded){
        hmBook.put("isbn", isbn);
        hmBook.put("title", title);
        hmBook.put("description", description);
        hmBook.put("author", author);
        hmBook.put("publisher", publisher);
        hmBook.put("year", year);
        hmBook.put("genre", genre);
        hmBook.put("quantity", String.valueOf(quantity));
        hmBook.put("issuecount", String.valueOf(issueCount));
        hmBook.put("dateadded", dateAdded);
        fbStore.collection("books").document().set(hmBook);
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
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {
        }
    }

}