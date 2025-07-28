package com.emon.tahminatelecom.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, "tahmina_table", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE expense (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, notes TEXT, amount DOUBLE, status TEXT, time TEXT)");
        db.execSQL("CREATE TABLE income (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, notes TEXT, amount DOUBLE, status TEXT, time TEXT)");
        db.execSQL("CREATE TABLE total_balance (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, notes TEXT, amount DOUBLE,status TEXT, time TEXT)");
        db.execSQL("CREATE TABLE history (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, notes TEXT, amount DOUBLE, status TEXT, time TEXT,original_id INTEGER, source TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists expense");
        db.execSQL("drop table if exists income");
        db.execSQL("drop table if exists total_balance");
        db.execSQL("drop table if exists history");

    }


    public void AddExpense(String name, String notes, Double amount, String status) {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm", Locale.getDefault());
        String formattedTime = sdf.format(new Date(currentTimeMillis));

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues expenseValues = new ContentValues();

        expenseValues.put("name", name);
        expenseValues.put("notes", notes);
        expenseValues.put("amount", amount);
        expenseValues.put("status", status);
        expenseValues.put("time", formattedTime);

        long insertedId = db.insert("expense", null, expenseValues);

        // Insert into history with original_id and source
        ContentValues historyValues = new ContentValues();
        historyValues.put("original_id", insertedId);
        historyValues.put("source", "expense");
        historyValues.put("name", name);
        historyValues.put("notes", notes);
        historyValues.put("amount", amount);
        historyValues.put("status", status);
        historyValues.put("time", formattedTime);

        db.insert("history", null, historyValues);
    }

    public void AddInComes(String name, String notes, Double amount, String status) {
        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm", Locale.getDefault());
        String formattedTime = sdf.format(new Date(currentTimeMillis));

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues incomeValues = new ContentValues();

        incomeValues.put("name", name);
        incomeValues.put("notes", notes);
        incomeValues.put("amount", amount);
        incomeValues.put("status", status);
        incomeValues.put("time", formattedTime);

        long insertedId = db.insert("income", null, incomeValues);

        // Insert into history with original_id and source
        ContentValues historyValues = new ContentValues();
        historyValues.put("original_id", insertedId);
        historyValues.put("source", "income");
        historyValues.put("name", name);
        historyValues.put("notes", notes);
        historyValues.put("amount", amount);
        historyValues.put("status", status);
        historyValues.put("time", formattedTime);

        db.insert("history", null, historyValues);
    }

    public void Add_Total_balance(String name,String notes,Double amount){

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm", Locale.getDefault());
        String formattedTime = sdf.format(new Date(currentTimeMillis));


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("notes", notes);
        contentValues.put("amount", amount);
        contentValues.put("time", formattedTime);

        db.insert("total_balance", null, contentValues);

    }
    public void Add_History(String name,String notes, Double amount, String status){

        long currentTimeMillis = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd\nHH:mm", Locale.getDefault());
        String formattedTime = sdf.format(new Date(currentTimeMillis));


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("notes", notes);
        contentValues.put("amount", amount);
        contentValues.put("status", status);
        contentValues.put("time", formattedTime);

        db.insert("history", null, contentValues);

    }

    /////===========


    public Double ExpenseTotalBalance(){

        double expensetotalexpense = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor= db.rawQuery("select * from expense", null);
        if (cursor != null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                double expense = cursor.getDouble(3);
                expensetotalexpense = expensetotalexpense+expense;
            }

        }

        return expensetotalexpense;

    }

    public Double IncomeTotalBalance(){

        double incomeTotalBalance = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select * from income", null);
        if (cursor != null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                double expense = cursor.getDouble(3);
                incomeTotalBalance = incomeTotalBalance+expense;
            }

        }

        return incomeTotalBalance;

    }

    public Double TotalBalance(){

        double incomeTotalBalance = 0;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.rawQuery("select * from total_balance", null);
        if (cursor != null && cursor.getCount()>0){
            while (cursor.moveToNext()){
                double expense = cursor.getDouble(3);
                incomeTotalBalance = incomeTotalBalance+expense;
            }

        }

        return incomeTotalBalance;

    }
//-----------------------------------------------------------------

    public Cursor showExpense(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from expense ORDER BY id DESC", null);

        return cursor;
    }

    public Cursor showIncome(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from income ORDER BY id DESC", null);

        return cursor;
    }

    public Cursor showTotal(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from total_balance ORDER BY id DESC", null);

        return cursor;
    }

    public Cursor showHistory(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from history ORDER BY id DESC", null);

        return cursor;
    }

    //--------------------------------------


    // ডিলিট মেথড, যা history ও source উভয় থেকে মুছে ফেলবে
    public boolean deleteFromHistoryAndSource(int id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedHistory = 0;
        int deletedSource = 0;

        try {
            db.beginTransaction();

            String source = "";
            if ("take".equals(status)) {
                source = "income";
            } else if ("give".equals(status)) {
                source = "expense";
            }

            Log.d("DB_DELETE", "Trying to delete history with original_id=" + id + " and source=" + source);

            // Delete from history
            deletedHistory = db.delete("history", "original_id = ? AND source = ?", new String[]{String.valueOf(id), source});
            Log.d("DB_DELETE", "Deleted rows from history: " + deletedHistory);

            // Delete from source
            if ("income".equals(source)) {
                deletedSource = db.delete("income", "id = ?", new String[]{String.valueOf(id)});
                Log.d("DB_DELETE", "Deleted rows from income: " + deletedSource);
            } else if ("expense".equals(source)) {
                deletedSource = db.delete("expense", "id = ?", new String[]{String.valueOf(id)});
                Log.d("DB_DELETE", "Deleted rows from expense: " + deletedSource);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DB_DELETE", "Error while deleting data", e);
        } finally {
            db.endTransaction();
            db.close();
        }

        return (deletedHistory > 0 || deletedSource > 0);
    }




    public void deleteFromExpenseAndHistory(int expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete expense
        db.delete("expense", "id=?", new String[]{String.valueOf(expenseId)});

        // Delete corresponding history entry
        db.delete("history", "original_id=? AND source='expense'", new String[]{String.valueOf(expenseId)});

        db.close();
    }

    public void deleteFromIncomeAndHistory(int incomeId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete income
        db.delete("income", "id=?", new String[]{String.valueOf(incomeId)});

        // Delete corresponding history entry
        db.delete("history", "original_id=? AND source='income'", new String[]{String.valueOf(incomeId)});

        db.close();
    }

    // Delete from total_balance table by ID
    public boolean deleteTotalById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete("total_balance", "id=?", new String[]{String.valueOf(id)});
        db.close();
        return deletedRows > 0;
    }






}
