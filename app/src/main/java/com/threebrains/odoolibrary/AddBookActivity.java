package com.threebrains.odoolibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.threebrains.odoolibrary.databinding.ActivityAddBookBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddBookActivity extends AppCompatActivity {

    String isbn, title, desc, author, publisher, genre, year, qty;
    String currentDate = "";

    FirebaseFirestore fbStore;
    ActivityAddBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fbStore = FirebaseFirestore.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = sdf.format(Calendar.getInstance().getTime());
        Calendar calDue = Calendar.getInstance();
        calDue.add(Calendar.DATE, 15);
        String dueDate = sdf.format(calDue.getTime());

        binding.btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isbn = binding.etBookIsbn.getText().toString().trim();
                title = binding.etBookTitle.getText().toString().trim();
                desc = binding.etBookDesc.getText().toString().trim();
                author = binding.etBookAuthor.getText().toString().trim();
                publisher = binding.etBookPublisher.getText().toString().trim();
                genre = binding.etBookGenre.getText().toString().trim();
                year = binding.etBookYear.getText().toString().trim();
                qty = binding.etBookQuantity.getText().toString().trim();

                if(!isbn.isEmpty() && !title.isEmpty() && !desc.isEmpty() && !author.isEmpty() && !publisher.isEmpty() && !genre.isEmpty() && !year.isEmpty() && !qty.isEmpty()){
                    Task<QuerySnapshot> qs = fbStore.collection("books").whereEqualTo("isbn".toLowerCase(), isbn.toLowerCase()).get();
                    qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.getResult().getDocuments().isEmpty()){
                                addNewBook();
                            }else{
                                Toast.makeText(AddBookActivity.this, "Book with given ISBN already exists!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(AddBookActivity.this, "Provide all book details!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addNewBook(){
        HashMap<String, String> hmBook = new HashMap<>();
        hmBook.put("isbn", isbn);
        hmBook.put("title", title);
        hmBook.put("description", desc);
        hmBook.put("author", author);
        hmBook.put("publisher", publisher);
        hmBook.put("year", year);
        hmBook.put("genre", genre);
        hmBook.put("quantity", qty);
        hmBook.put("issuecount", "0");
        hmBook.put("dateadded", currentDate);

        fbStore.collection("books").document().set(hmBook).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(AddBookActivity.this, "Book added successfully!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else {
                    Toast.makeText(AddBookActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}