package com.threebrains.odoolibrary.fragments;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.threebrains.odoolibrary.AdminActivity;
import com.threebrains.odoolibrary.LibrarianActivity;
import com.threebrains.odoolibrary.R;
import com.threebrains.odoolibrary.models.LastSixMonthIssueModel;
import com.threebrains.odoolibrary.utilities.Constants;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    CardView cvTotalBooks, cvAvailableBooks, cvIssuedBooks, cvPendingRequests;

    TextView tvTotalBooks, tvAvailableBooks, tvIssuedBooks, tvPendingRequests;

    BarChart barChartIssuedBooks;
    ArrayList<BarEntry> entries;
    BarDataSet barDataSet;
    BarData barData;
    int[] issuedBooksCounts = new int[12];
    ArrayList<LastSixMonthIssueModel> alLastSixMonths = new ArrayList<>();
    int currentMonth = 0, currentYear = 0;

    FirebaseFirestore db;
    int totalBooks, availableBooks, issuedBooks, pendingRequests;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragDashboard = inflater.inflate(R.layout.fragment_dashboard, container, false);

        cvTotalBooks = fragDashboard.findViewById(R.id.cv_total_books);
        cvAvailableBooks = fragDashboard.findViewById(R.id.cv_available_books);
        cvIssuedBooks = fragDashboard.findViewById(R.id.cv_issued_books);
        cvPendingRequests = fragDashboard.findViewById(R.id.cv_pending_requests);
        tvTotalBooks = fragDashboard.findViewById(R.id.tv_total_books);
        tvAvailableBooks = fragDashboard.findViewById(R.id.tv_available_books);
        tvIssuedBooks = fragDashboard.findViewById(R.id.tv_issued_books);
        tvPendingRequests = fragDashboard.findViewById(R.id.tv_pending_requests);
        barChartIssuedBooks = fragDashboard.findViewById(R.id.bar_chart_issued_books);

        db = FirebaseFirestore.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;

        entries = new ArrayList<>();
        barDataSet = new BarDataSet(entries, "Monthly Issued Books");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(getResources().getColor(R.color.darker));
        barData = new BarData(barDataSet);
        barChartIssuedBooks.setData(barData);

        getLastSixMonthsIssueCount();

        barChartIssuedBooks.setNoDataTextColor(getResources().getColor(R.color.red));
        barChartIssuedBooks.getXAxis().setTextColor(getResources().getColor(R.color.darker));
        barChartIssuedBooks.getAxis(YAxis.AxisDependency.LEFT).setTextColor(getResources().getColor(R.color.darker));
        barChartIssuedBooks.getAxis(YAxis.AxisDependency.RIGHT).setTextColor(getResources().getColor(R.color.darker));
        barChartIssuedBooks.getDescription().setTextColor(getResources().getColor(R.color.darker));
        barChartIssuedBooks.getDescription().setEnabled(false);
        barChartIssuedBooks.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChartIssuedBooks.getLegend().setTextColor(getResources().getColor(R.color.darker));

        barChartIssuedBooks.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChartIssuedBooks.getXAxis().setGranularity(1f);
        barChartIssuedBooks.getXAxis().setGranularityEnabled(true);

        getAvailableBooks();

        cvTotalBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(0);
            }
        });

        cvAvailableBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(1);
            }
        });

        cvIssuedBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(2);
            }
        });

        cvPendingRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(3);
            }
        });

        return fragDashboard;
    }

    private void getAvailableBooks(){
        db.collection("books").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document: task.getResult()){
                        availableBooks += Integer.parseInt(document.getString("quantity"));
                    }

                    tvAvailableBooks.setText(String.valueOf(availableBooks));
                    getIssuedBooksPendingRequests();
                }else {
                    Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getIssuedBooksPendingRequests(){
        db.collection("requested").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot document: task.getResult()){
                        pendingRequests += document.getString("status").equals("pending")? 1 : 0;

                        if (document.getString("status").equals("approved")){
                            issuedBooks += document.getString("returndate").equals("-")? 1 : 0;
                            String[] issueDate = document.getString("issuedate").split("-");

                            int year = Integer.parseInt(issueDate[0]);
                            int month = Integer.parseInt(issueDate[1]);
                            int day = Integer.parseInt(issueDate[2]);

                            if ((year == currentYear && month <= currentMonth) || (year == (currentYear - 1) && month > currentMonth)){
                                issuedBooksCounts[month-1] = issuedBooksCounts[month-1]++;
                            }
                        }
                    }

                    tvIssuedBooks.setText(String.valueOf(issuedBooks));
                    tvPendingRequests.setText(String.valueOf(pendingRequests));

                    calcTotalBooks();
                }else {
                    Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void calcTotalBooks(){
        totalBooks = issuedBooks + availableBooks;

        tvTotalBooks.setText(String.valueOf(totalBooks));
    }

    private void changeFragment(int i){
        if(Constants.ROLE.equals("Admin")){
            AdminActivity activity = (AdminActivity) getActivity();
            activity.setFromFragment(i);
        }else if(Constants.ROLE.equals("Librarian")){
            LibrarianActivity activity = (LibrarianActivity) getActivity();
            activity.setFromFragment(i);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void getLastSixMonthsIssueCount(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(Calendar.getInstance().getTime());
        String temp[] = currentDate.split("-");
        int currentYear = Integer.parseInt(temp[0]);


        String localDate = currentDate;
        LocalDate ld = LocalDate.parse(localDate);
        ld = ld.minusMonths(6);

        int startYear = ld.getYear();
        int startMonth = ld.getMonthValue()+1;
        String yrMn[] = new String[6];

        int issueCount[] = new int[6];
        issueCount[0] = 0;
        issueCount[1] = 0;
        issueCount[2] = 0;
        issueCount[3] = 0;
        issueCount[4] = 0;
        issueCount[5] = 0;


        if(startYear == currentYear){
            for (int i=0;i<6;i++){
                if(startMonth<=9){
                    yrMn[i] = startYear+"-0"+startMonth;
                }else{
                    yrMn[i] = startYear+"-"+startMonth;
                }
                startMonth++;
            }
        }else{
            for (int i=0;i<6;i++){
                if(startMonth<=12){
                    if(startMonth<=9){
                        yrMn[i] = startYear+"-0"+startMonth;
                    }else{
                        yrMn[i] = startYear+"-"+startMonth;
                    }
                    startMonth++;
                }else{
                    startMonth = 1;
                    startYear = currentYear;
                    yrMn[i] = startYear + "-0" + startMonth;
                    startMonth++;
                }
            }
        }
        ArrayList<String> alYrMn = new ArrayList<>();
        Task<QuerySnapshot> qs = db.collection("requested").get();
        qs.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                try{
                    List<DocumentSnapshot> requests = task.getResult().getDocuments();
                    for(DocumentSnapshot request : requests){
                        alYrMn.add(request.getString("issuedate"));
                    }
                    Collections.sort(alYrMn);
                    for(String yearMonth: alYrMn){
                        for(int i=0;i<6;i++){
                            if(yearMonth.contains(yrMn[i])){
                                issueCount[i]++;
                            }
                        }
                    }
                    for(int i=0;i<6;i++){
                        alLastSixMonths.add(new LastSixMonthIssueModel(yrMn[i], issueCount[i]));
                    }

                    entries.clear();
                    for (int i=0; i<6; i++){
                        BarEntry barEntry = new BarEntry(i, alLastSixMonths.get(i).getIssuecount(), alLastSixMonths.get(i).getMonth());
//                        BarEntry barEntry = new BarEntry(i, (i+1)*10);
                        entries.add(barEntry);
                    }
                    barDataSet.notifyDataSetChanged();
                    barData.notifyDataChanged();
                    barChartIssuedBooks.notifyDataSetChanged();
                    barChartIssuedBooks.invalidate();
                    barChartIssuedBooks.refreshDrawableState();
                    barChartIssuedBooks.animateY(400);

                    barChartIssuedBooks.getXAxis().setValueFormatter(new IndexAxisValueFormatter(yrMn));
                } catch (Exception e) {
                    Log.d("dalle", e.toString());
                    Toast.makeText(requireContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}