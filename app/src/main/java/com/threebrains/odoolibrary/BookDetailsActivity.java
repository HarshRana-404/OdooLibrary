package com.threebrains.odoolibrary;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.threebrains.odoolibrary.auth.Login;
import com.threebrains.odoolibrary.models.BookModel;
import com.threebrains.odoolibrary.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class BookDetailsActivity extends AppCompatActivity {

    ShapeableImageView sivBookImage;
    TextView tvBookTitle, tvBookDescription, tvBookAuthor, tvBookYear, tvBookPublisher, tvBookGenre, tvBookQuantity;
    Button btnRequestBook;

    String username;
    String isbn, bookCoverUrl, title, desc, author, year, publisher, genre;
    int qty;

    FirebaseFirestore fbStore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    String currentDate = "", dueDate = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        sivBookImage = findViewById(R.id.siv_book_img);
        tvBookTitle = findViewById(R.id.tv_book_title);
        tvBookDescription = findViewById(R.id.tv_book_description);
        tvBookAuthor = findViewById(R.id.tv_book_author);
        tvBookYear = findViewById(R.id.tv_book_year);
        tvBookPublisher = findViewById(R.id.tv_book_publisher);
        tvBookGenre = findViewById(R.id.tv_book_genre);
        tvBookQuantity = findViewById(R.id.tv_book_quantity);
        btnRequestBook = findViewById(R.id.btn_request_book);

        fbStore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        if(Constants.ROLE.equals("Librarian")){
            btnRequestBook.setVisibility(View.GONE);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = sdf.format(Calendar.getInstance().getTime());
        Calendar calDue = Calendar.getInstance();
        calDue.add(Calendar.DATE, 15);
        dueDate = sdf.format(calDue.getTime());

        getBookDetails();
        getUserName();

        btnRequestBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder adb = new AlertDialog.Builder(BookDetailsActivity.this);
                AlertDialog ad = adb.create();
                adb.setTitle(title);
                adb.setMessage("Are you sure you want to issue this book?");
                adb.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Task<QuerySnapshot> qs = fbStore.collection("requested").whereEqualTo("uid", Constants.UID).get();
                        qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                List<DocumentSnapshot> books = task.getResult().getDocuments();
                                Boolean isBookAlreadyRequested = false;
                                for(DocumentSnapshot book : books){
                                    if(book.getString("isbn").equals(isbn)){
                                        if(book.getString("status").equals("pending")){
                                            Toast.makeText(BookDetailsActivity.this, "Book already requested!", Toast.LENGTH_SHORT).show();
                                            isBookAlreadyRequested = true;
                                        }else if(book.getString("status").equals("approved")){
                                            if(book.getString("returndate").equals("-")){
                                                String temp[] = book.getString("duedate").split("-");
                                                String dueToShow = temp[2]+"-"+temp[1]+"-"+temp[0];    Toast.makeText(BookDetailsActivity.this, "Book request is already approved, due date is "+dueToShow, Toast.LENGTH_SHORT).show();
                                                isBookAlreadyRequested = true;
                                            }
                                        }
                                    }
                                }
                                if(!isBookAlreadyRequested){
                                    requestBook(isbn, title);
                                }
                            }
                        });
                    }
                });
                adb.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ad.dismiss();
                    }
                });
                adb.show();

            }
        });
    }

    public void requestBook(String isbn, String title){
        HashMap<String, String> hmRequests = new HashMap<>();
        hmRequests.put("isbn", isbn);
        hmRequests.put("bookcoverurl", bookCoverUrl);
        hmRequests.put("title", title);
        hmRequests.put("uid", Constants.UID);
        hmRequests.put("username", username);
        hmRequests.put("requestdate", currentDate);
        hmRequests.put("issuedate", "-");
        hmRequests.put("duedate", dueDate);
        hmRequests.put("returndate", "-");
        hmRequests.put("status", "pending");
        fbStore.collection("requested").document().set(hmRequests).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(BookDetailsActivity.this, "Book requested successfully!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    public void getUserName(){
        fbStore.collection("users").document(Constants.UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    username = document.getString("username");
                }else {
                    Toast.makeText(BookDetailsActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getBookDetails(){
        isbn = getIntent().getStringExtra("bookIsbn");
        bookCoverUrl = getIntent().getStringExtra("bookCoverUrl");
        title = getIntent().getStringExtra("bookTitle");
        desc = getIntent().getStringExtra("bookDescription");
        author = getIntent().getStringExtra("bookAuthor");
        year = getIntent().getStringExtra("bookYear");
        publisher = getIntent().getStringExtra("bookPublisher");
        genre = getIntent().getStringExtra("bookGenre");
        qty = (getIntent().getIntExtra("bookQuantity", 0));

        if (qty == 0){
            tvBookQuantity.setText("Not Available");
            tvBookQuantity.setTextColor(getResources().getColor(R.color.red));
            btnRequestBook.setEnabled(false);
        }else {
            tvBookQuantity.setText(qty + " Available");
            tvBookQuantity.setTextColor(getResources().getColor(R.color.green));
            btnRequestBook.setEnabled(true);
        }

        storageReference.child(bookCoverUrl).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide
                    .with(getApplicationContext())
                    .load(uri)
                    .centerCrop()
                    .into(sivBookImage);
            }
        });

        tvBookTitle.setText(title);
        tvBookDescription.setText(desc);
        tvBookAuthor.setText(author);
        tvBookYear.setText(year);
        tvBookPublisher.setText(publisher);
        tvBookGenre.setText(genre);
    }
}