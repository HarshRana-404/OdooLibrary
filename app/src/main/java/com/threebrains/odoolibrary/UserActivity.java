package com.threebrains.odoolibrary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.threebrains.odoolibrary.fragments.BooksFragment;
import com.threebrains.odoolibrary.fragments.HistoryFragment;
import com.threebrains.odoolibrary.fragments.ProfileFragment;

public class UserActivity extends AppCompatActivity {

    BottomNavigationView bnvUser;
    FrameLayout flUser;
    MenuItem lastNavigatedMenuItem=null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        bnvUser = findViewById(R.id.bnv_user);
        flUser = findViewById(R.id.fl_user);

        setFragment(new BooksFragment());
        bnvUser.setSelectedItemId(R.id.mi_user_books);

        bnvUser.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.mi_user_books && lastNavigatedMenuItem!=item){
                    setFragment(new BooksFragment());
                    lastNavigatedMenuItem = item;
                }else if(item.getItemId()==R.id.mi_user_history && lastNavigatedMenuItem!=item){
                    setFragment(new HistoryFragment());
                    lastNavigatedMenuItem = item;
                }else if(item.getItemId()==R.id.mi_user_profile && lastNavigatedMenuItem!=item){
                    setFragment(new ProfileFragment());
                    lastNavigatedMenuItem = item;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bnvUser.setSelectedItemId(R.id.mi_user_books);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.librarian_bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void setFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_user, fragment);
        ft.commit();
    }
}