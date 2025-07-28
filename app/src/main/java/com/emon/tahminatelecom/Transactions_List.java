package com.emon.tahminatelecom;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emon.tahminatelecom.Adapter.TransactionAdapter;
import com.emon.tahminatelecom.Model.TransactionModel;
import com.emon.tahminatelecom.database.DatabaseHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Transactions_List extends AppCompatActivity {

    TextView nodatatv, totalexpensetv;
    MaterialToolbar appbar_title;

    public static String Title = "";
    public static boolean Expense = false;

    RecyclerView recyclerView;
    TransactionAdapter transactionAdapter;
    ArrayList<TransactionModel> transactionList = new ArrayList<>();

    FloatingActionButton fab_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transactions_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set status bar text color to white
        getWindow().getDecorView().setSystemUiVisibility(0);

        // Optional: Make status bar background color dark to see white text clearly
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue)); // or any dark color

        totalexpensetv = findViewById(R.id.totalexpensetv);
        nodatatv = findViewById(R.id.nodatatv);
        appbar_title = findViewById(R.id.appbar_title);
        appbar_title.setTitle(Title);


        appbar_title.setNavigationOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerView);
        fab_add = findViewById(R.id.fab_add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transactionAdapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(transactionAdapter);

        setupButtonClickListeners();



    }

    private void setupButtonClickListeners() {

        fab_add.setOnClickListener(v -> {
            Intent intent = new Intent(Transactions_List.this, Input_Data.class);
            startActivity(intent);
            Input_Data.Title= String.valueOf(Title);
        });
    }


    void DataFetch(){
        transactionList.clear(); // পুরনো ডাটা ক্লিয়ার করুন
        nodatatv.setVisibility(View.GONE); // ডিফল্ট ভাবে invisible করুন

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Cursor cursor;
        Double total;

        if (Title.equals("বাকি")) {
            cursor = databaseHelper.showIncome();
            total = databaseHelper.IncomeTotalBalance();

        } else if (Title.equals("পাওনা")) {
            cursor = databaseHelper.showExpense();
            total = databaseHelper.ExpenseTotalBalance();

        } else {
            cursor = databaseHelper.showTotal();
            total = databaseHelper.TotalBalance();
        }

        totalexpensetv.setText("Total: " + total);

        if (cursor != null && cursor.getCount() > 0) {
            nodatatv.setVisibility(View.GONE); // ✅ Data paile "No Data" hide
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String notes = cursor.getString(2);
                double amount = cursor.getDouble(3);
                String catagories = cursor.getString(4);
                String date = cursor.getString(5);

                TransactionModel model = new TransactionModel(id,name, notes, amount, date, catagories);
                transactionList.add(model);
            }
        } else {
            nodatatv.setVisibility(View.VISIBLE); // ❌ No data paile show korbe
            nodatatv.setText("No Data Found");
        }

        transactionAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // ✅ Fetch data and notify adapter
        DataFetch();
        transactionAdapter.notifyDataSetChanged(); //
    }
}