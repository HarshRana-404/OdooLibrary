package com.threebrains.odoolibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.threebrains.odoolibrary.databinding.ActivityAddBookBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddBookActivity extends AppCompatActivity {

//    EditText etBookISBN, etBookTitle, etBookDescription, etBookAuthor, etBookPublisher, etBookGenre, etBookYear, etBookQuantity;
//    Button btnAddBook;

    String isbn, title, desc, author, publisher, genre, year, qty;
    String currentDate = "";

    FirebaseFirestore fbStore;
    ActivityAddBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
                title = binding.etBookIsbn.getText().toString().trim();
                desc = binding.etBookIsbn.getText().toString().trim();
                author = binding.etBookIsbn.getText().toString().trim();
                publisher = binding.etBookIsbn.getText().toString().trim();
                genre = binding.etBookIsbn.getText().toString().trim();
                year = binding.etBookIsbn.getText().toString().trim();
                qty = binding.etBookIsbn.getText().toString().trim();

                addNewBook();
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
                    onBackPressed();
                }else {
                    Toast.makeText(AddBookActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}