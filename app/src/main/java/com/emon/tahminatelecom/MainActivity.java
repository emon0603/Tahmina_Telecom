package com.emon.tahminatelecom;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    MaterialCardView due_bt, paona_bt,total_bal_bt;
    TextView nodatatv, tvTotalPaona,tv_due, tvTotal;
    RecyclerView recyclerView;
    TransactionAdapter transactionAdapter;
    ArrayList<TransactionModel> transactionList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Set status bar text color to white
        getWindow().getDecorView().setSystemUiVisibility(0);

        // Optional: Make status bar background color dark to see white text clearly
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue)); // or any dark color

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        tvTotal = findViewById(R.id.amountText);
        due_bt = findViewById(R.id.due_bt);
        paona_bt = findViewById(R.id.paona_bt);
        total_bal_bt = findViewById(R.id.total_bal_bt);
        tv_due = findViewById(R.id.tv_due);
        tvTotalPaona = findViewById(R.id.tvTotalPaona);
        nodatatv = findViewById(R.id.nodatatv);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transactionAdapter = new TransactionAdapter(transactionList);
        recyclerView.setAdapter(transactionAdapter);



        setupButtonClickListeners();


    }

    private void setupButtonClickListeners() {
        setTransactionButtonListener(due_bt, "বাকি", false);
        setTransactionButtonListener(paona_bt, "পাওনা",true);
        setTransactionButtonListener(total_bal_bt, "মোট ব্যালেন্স",true);

    }

    private void setTransactionButtonListener(MaterialCardView button, String title, boolean Expense) {
        button.setOnClickListener(v -> {
            Transactions_List.Title = title;
            Transactions_List.Expense = Expense;
            Intent intent = new Intent(MainActivity.this, Transactions_List.class);
            startActivity(intent);
        });
    }


    void TvDisplay() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Double total_balance = databaseHelper.TotalBalance();
        Double totalPaona = databaseHelper.ExpenseTotalBalance();
        Double totalDue = databaseHelper.IncomeTotalBalance();     // Custom method

        tvTotal.setText(formatDouble(total_balance));
        tvTotalPaona.setText(formatDouble(totalPaona));
        tv_due.setText(formatDouble(totalDue));
    }

    private String formatDouble(Double value) {
        return (value == null || value.isNaN() || value.isInfinite() || value < 0)
                ? "00" : String.valueOf(value);
    }

    void DataFetch(){
        transactionList.clear(); // পুরনো ডাটা ক্লিয়ার করুন
        nodatatv.setVisibility(View.GONE); // ডিফল্ট ভাবে invisible করুন

        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Cursor cursor;

        cursor = databaseHelper.showHistory();
        Log.d("test", String.valueOf(cursor.getCount()));

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
        TvDisplay();
        DataFetch();
    }
}
