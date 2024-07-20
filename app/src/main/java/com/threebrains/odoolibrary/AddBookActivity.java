package com.threebrains.odoolibrary;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.threebrains.odoolibrary.databinding.ActivityAddBookBinding;
import com.threebrains.odoolibrary.utilities.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AddBookActivity extends AppCompatActivity {

    Uri uri;
    String isbn, title, desc, author, publisher, genre, year, qty;
    String currentDate = "";

    FirebaseFirestore fbStore;

    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    ActivityAddBookBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fbStore = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = sdf.format(Calendar.getInstance().getTime());
        Calendar calDue = Calendar.getInstance();
        calDue.add(Calendar.DATE, 15);
        String dueDate = sdf.format(calDue.getTime());

        binding.ivBookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });

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

                if(!isbn.isEmpty() && !title.isEmpty() && !desc.isEmpty() && !author.isEmpty() && !publisher.isEmpty() && !genre.isEmpty() && !year.isEmpty() && !qty.isEmpty() && uri != null){
                    loading(true);
                    Task<QuerySnapshot> qs = fbStore.collection("books").whereEqualTo("isbn".toLowerCase(), isbn.toLowerCase()).get();
                    qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.getResult().getDocuments().isEmpty()){
                                addNewBook();

                                loading(false);
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
        hmBook.put("bookcoverurl", "BookCovers/" + uri.getLastPathSegment());
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
                    uploadBookCover(uri);

                    Toast.makeText(AddBookActivity.this, "Book added successfully!", Toast.LENGTH_SHORT).show();

                    onBackPressed();
                }else {
                    Toast.makeText(AddBookActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (resultCode == Activity.RESULT_OK){
                if (requestCode == 1){
                    Uri selectedImgUri = data.getData();

                    if (selectedImgUri != null){
                        binding.ivBookImg.setImageURI(selectedImgUri);
                        binding.ivBookImg.setImageTintList(null);
                        binding.ivBookImg.setPadding(0,0,0,0);
                        binding.ivBookImg.setBackground(getResources().getDrawable(R.drawable.rounded_transparent_bg));
                        binding.ivBookImg.setBackgroundDrawable(getResources().getDrawable(R.drawable.rounded_transparent_bg));

                        uri = selectedImgUri;
                    }else {
                        Toast.makeText(getApplicationContext(), "Null Uri returned!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Edit: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadBookCover(Uri uri){
        storageReference = storageReference.child("BookCovers/" + uri.getLastPathSegment());

        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    // if any
                }else {
                    Toast.makeText(AddBookActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loading(boolean isLoading){
        if (isLoading){
            binding.btnAddBook.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
        }else {
            binding.btnAddBook.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    }
}