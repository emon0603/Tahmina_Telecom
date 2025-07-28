package com.emon.tahminatelecom;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.emon.tahminatelecom.database.DatabaseHelper;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

public class Input_Data extends AppCompatActivity {

    MaterialToolbar appbar_title;

    public static String Title = "";

    private DatabaseHelper dbHelper;
    public static boolean Expense = true;
    MaterialButton insert_bt;

    EditText edname,ednotes,edamount, edcate ;
    AutoCompleteTextView Catagories_Spinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set status bar text color to white
        getWindow().getDecorView().setSystemUiVisibility(0);

        // Optional: Make status bar background color dark to see white text clearly
        getWindow().setStatusBarColor(getResources().getColor(R.color.blue)); // or any dark color



        appbar_title = findViewById(R.id.appbar_title);
        insert_bt = findViewById(R.id.insert_bt);
        edname = findViewById(R.id.edname);
        ednotes = findViewById(R.id.ednotes);
        edamount = findViewById(R.id.edamount);





        appbar_title.setTitle(Title);


        dbHelper = new DatabaseHelper(getApplicationContext());

        appbar_title.setNavigationOnClickListener(v -> finish());

        DataInsertMethod();


    }


    void DataInsertMethod() {
        insert_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String samount = edamount.getText().toString().trim();
                String note = ednotes.getText().toString().trim();
                String name = edname.getText().toString().trim();

                // ✅ ইনপুট চেক করুন
                if (name.isEmpty() || samount.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "সবগুলো তথ্য পূরণ করুন।", Toast.LENGTH_SHORT).show();
                    return; // এক্সিকিউশন এখানেই থেমে যাবে
                }

                // ✅ সংখ্যাটি সঠিকভাবে আছে কিনা যাচাই (যদি পার্স করতে না পারে)
                double amount;
                try {
                    amount = Double.parseDouble(samount);
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "পরিমাণ সঠিক নয়।", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ ডেটা ইনসার্ট
                if (Title.equals("বাকি")) {
                    dbHelper.AddInComes(name, note, amount, "take");
                    //dbHelper.Add_History(name, note, amount,"take");
                    Toast.makeText(getApplicationContext(), "বাকি হিসাব সংরক্ষণ সফল হয়েছে।", Toast.LENGTH_SHORT).show();

                } else if (Title.equals("পাওনা")) {
                    dbHelper.AddExpense(name, note, amount,"give");
                   // dbHelper.Add_History(name, note, amount,"give");
                    Toast.makeText(getApplicationContext(), "পাওনা হিসাব সংরক্ষণ সফল হয়েছে।", Toast.LENGTH_SHORT).show();

                } else {
                    dbHelper.Add_Total_balance(name, note, amount);
                    Toast.makeText(getApplicationContext(), "মোট ব্যালেন্স হিসাব সংরক্ষণ সফল হয়েছে।", Toast.LENGTH_SHORT).show();
                }

                // ✅ ইনপুট ফিল্ড ক্লিয়ার করুন
                edname.getText().clear();
                edamount.getText().clear();
                ednotes.getText().clear();
            }
        });
    }




}