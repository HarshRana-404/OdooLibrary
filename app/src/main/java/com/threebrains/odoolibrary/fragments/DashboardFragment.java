package com.threebrains.odoolibrary.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.threebrains.odoolibrary.R;

public class DashboardFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragDashboard = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return fragDashboard;
    }
}