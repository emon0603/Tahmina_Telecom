package com.emon.tahminatelecom.Adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.emon.tahminatelecom.Model.TransactionModel;
import com.emon.tahminatelecom.R;
import com.emon.tahminatelecom.database.DatabaseHelper;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Random;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private ArrayList<TransactionModel> list;

    public TransactionAdapter(ArrayList<TransactionModel> list) {
        this.list = list;
    }

    DatabaseHelper dbHelper;



    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transa, parent, false);
        // dbHelper ইনিশিয়ালাইজ করা হচ্ছে এখানেই
        dbHelper = new DatabaseHelper(parent.getContext());
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionModel model = list.get(position);

        double amount = model.getAmount();
        String name = model.getName();
        String date = model.getDate();
        String notes = model.getNotes();
        String status = model.getStatus();



        holder.amounttv.setText(""+amount);
        holder.nametv.setText(name);
        holder.datetv.setText(date);
        holder.note_tv.setText(!TextUtils.isEmpty(notes) ? notes : "Null");



        if (status != null) {
            switch (status) {
                case "take":
                    holder.item_image.setImageResource(R.drawable.ic_baki_list);
                    holder.item_image.setVisibility(View.VISIBLE);
                    holder.itemText.setVisibility(View.GONE);
                    holder.itemContainer.getBackground().setTint(Color.RED);
                    break;
                case "give":
                    holder.item_image.setImageResource(R.drawable.ic_paona_list);
                    holder.item_image.setVisibility(View.VISIBLE);
                    holder.itemText.setVisibility(View.GONE);
                    holder.itemContainer.getBackground().setTint(Color.GREEN);
                    break;
                default:
                    //setRandomColor(holder);
                    showInitialAsFallback(holder, name);  // 🔄 fallback handle
                    break;
            }
        } else {
           // setRandomColor(holder);
            showInitialAsFallback(holder, name); // 🔄 fallback handle
        }






        holder.trancardview.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Amount: " + amount)
                    .setMessage("Name: " + name + "\n\nNote: " + notes)
                    .setPositiveButton("Delete", (dialog, which) -> {
                        int historyId = model.getId(); // history টেবিলের primary key
                        boolean isDeleted = dbHelper.deleteFromHistoryAndSource(historyId, model.getStatus());
                        boolean isDeletedTotalBalance = dbHelper.deleteTotalById(historyId);
                        if (isDeleted) {
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                        } else if (isDeletedTotalBalance){
                            list.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, list.size());
                        }else {
                            Toast.makeText(holder.itemView.getContext(), "অনুগ্রহ করে আপনার মূল লেনদেনটি মুছে দিন।", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView nametv, note_tv, datetv, amounttv, itemText;
        ImageView item_image;
        LinearLayout trancardview;
        RelativeLayout itemContainer;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            nametv = itemView.findViewById(R.id.name_tv);
            note_tv = itemView.findViewById(R.id.note_tv);
            datetv = itemView.findViewById(R.id.datetv);
            amounttv = itemView.findViewById(R.id.amounttv);
            trancardview = itemView.findViewById(R.id.trancardview);


            itemContainer = itemView.findViewById(R.id.item_container);
            item_image = itemView.findViewById(R.id.item_image);
            itemText = itemView.findViewById(R.id.item_text);

        }
    }


    private void setRandomColor(TransactionViewHolder holder) {
        int randomColor = Color.rgb(
                new Random().nextInt(256),
                new Random().nextInt(256),
                new Random().nextInt(256)
        );
        holder.itemContainer.setBackgroundColor(randomColor);
    }

    void DataFetch(TransactionViewHolder holder){

        Cursor cursor, cursor1;
        Double total;


        cursor = dbHelper.showIncome();
        cursor1 = dbHelper.showExpense();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                String status = cursor.getString(4);

                if (status != null) {
                    switch (status.trim()) {
                        case "take":
                            holder.item_image.setImageResource(R.drawable.ic_baki_list);
                            holder.itemContainer.getBackground().setTint(Color.RED);
                            break;
                        case "give":
                            holder.item_image.setImageResource(R.drawable.ic_paona_list);
                            holder.itemContainer.getBackground().setTint(Color.GREEN);
                            break;
                        default:
                           // setRandomStyle(holder);
                            break;
                    }
                } else {
                   // setRandomStyle(holder);
                }


            }
        } else {


        }
    }

    private void showInitialAsFallback(TransactionViewHolder holder, String name) {
        holder.item_image.setVisibility(View.GONE); // hide image
        holder.itemText.setVisibility(View.VISIBLE); // show text

        if (name != null && !name.isEmpty()) {
            String initial = name.substring(0, 1).toUpperCase();
            holder.itemText.setText(initial);
        } else {
            holder.itemText.setText("?");
        }

        holder.itemContainer.getBackground().setTint(getRandomColor()); // optional
    }

    private int getRandomColor() {
        Random random = new Random();
        return Color.rgb(
                random.nextInt(256),
                random.nextInt(256),
                random.nextInt(256)
        );
    }




}
