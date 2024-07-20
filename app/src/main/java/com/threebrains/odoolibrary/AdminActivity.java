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
import com.threebrains.odoolibrary.fragments.DashboardFragment;
import com.threebrains.odoolibrary.fragments.IssuedFragment;
import com.threebrains.odoolibrary.fragments.LibrariansFragment;
import com.threebrains.odoolibrary.fragments.ProfileFragment;
import com.threebrains.odoolibrary.fragments.RequestsFragment;

public class AdminActivity extends AppCompatActivity {

    BottomNavigationView bnvAdmin;
    FrameLayout flAdmin;
    MenuItem lastNavigatedMenuItem=null;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        bnvAdmin = findViewById(R.id.bnv_admin);
        flAdmin = findViewById(R.id.fl_admin);

        setFragment(new DashboardFragment());
        bnvAdmin.setSelectedItemId(R.id.mi_admin_dashboard);

        bnvAdmin.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.mi_admin_dashboard && lastNavigatedMenuItem!=item){
                    setFragment(new DashboardFragment());
                    lastNavigatedMenuItem = item;
                }else if(item.getItemId()==R.id.mi_admin_librarians && lastNavigatedMenuItem!=item){
                    setFragment(new LibrariansFragment());
                    lastNavigatedMenuItem = item;
                }else if(item.getItemId()==R.id.mi_admin_books && lastNavigatedMenuItem!=item){
                    setFragment(new BooksFragment());
                    lastNavigatedMenuItem = item;
                }else if(item.getItemId()==R.id.mi_admin_issued && lastNavigatedMenuItem!=item){
                    setFragment(new IssuedFragment());
                    lastNavigatedMenuItem = item;
                }else if(item.getItemId()==R.id.mi_admin_profile && lastNavigatedMenuItem!=item){
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
        bnvAdmin.setSelectedItemId(R.id.mi_admin_dashboard);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_bottom_nav_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void setFragment(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fl_admin, fragment);
        ft.commit();
    }
    public void setFromFragment(int i){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (i == 0){
            ft.replace(R.id.fl_admin, new BooksFragment());
            bnvAdmin.setSelectedItemId(R.id.mi_admin_books);
        }else if(i == 1){
            ft.replace(R.id.fl_admin, new BooksFragment());
            bnvAdmin.setSelectedItemId(R.id.mi_admin_books);
        }else if (i == 2){
            ft.replace(R.id.fl_admin, new IssuedFragment());
            bnvAdmin.setSelectedItemId(R.id.mi_admin_issued);
        }

        ft.commit();
    }
}