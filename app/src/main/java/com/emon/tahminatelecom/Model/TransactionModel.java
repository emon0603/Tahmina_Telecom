package com.emon.tahminatelecom.Model;

public class TransactionModel {
    private int id;
    private String name;
    private double amount;
    private String date;
    private String notes;
    private String status;

    public TransactionModel(int id, String name, String notes, double amount, String date,String status) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
        this.status = status;
    }

    // Getter Methods
    public int getId() { return id; }
    public String getName() { return name; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getNotes() { return notes; }
    public String getStatus() { return status != null ? status : "unknown"; }
}
