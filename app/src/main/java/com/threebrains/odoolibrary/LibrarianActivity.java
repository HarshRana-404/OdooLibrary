package com.threebrains.odoolibrary;

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
import com.threebrains.odoolibrary.fragments.DashboardFragment;
import com.threebrains.odoolibrary.fragments.IssuedFragment;
import com.threebrains.odoolibrary.fragments.LibrariansFragment;
import com.threebrains.odoolibrary.fragments.ProfileFragment;
import com.threebrains.odoolibrary.fragments.RequestsFragment;

public class LibrarianActivity extends AppCompatActivity {

    BottomNavigationView bnvLibrarian;
    FrameLayout flLibrarian;
    MenuItem lastNavigatedMenuItem=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian);

        bnvLibrarian = findViewById(R.id.bnv_librarian);
        flLibrarian = findViewById(R.id.fl_librarian);


        bnvLibrarian.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.mi_librarian_dashboard && lastNavigatedMenuItem!=item){
                    setFragment(new DashboardFragment());
                    lastNavigatedMenuItem = item;
                }else if(item.getItemId()==R.id.mi_librarian_requests && lastNavigatedMenuItem!=item){
                    setFragment(new RequestsFragment());
                    lastNavigatedMenuItem = item;
                }else if(item.getItemId()==R.id.mi_librarian_books && lastNavigatedMenuItem!=item){
                    setFragment(new BooksFragment());
                    lastNavigatedMenuItem = item;
                }else if(item.getItemId()==R.id.mi_librarian_issued && lastNavigatedMenuItem!=item){
                    setFragment(new IssuedFragment());
                    lastNavigatedMenuItem = item;
                }else if(item.getItemId()==R.id.mi_librarian_profile && lastNavigatedMenuItem!=item){
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
        setFragment(new DashboardFragment());
        bnvLibrarian.setSelectedItemId(R.id.mi_librarian_dashboard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.librarian_bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void setFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_librarian, fragment);
        ft.commit();
    }
}