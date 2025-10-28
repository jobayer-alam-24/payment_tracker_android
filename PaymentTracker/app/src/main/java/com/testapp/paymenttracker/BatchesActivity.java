package com.testapp.paymenttracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BatchesActivity extends AppCompatActivity {

    RecyclerView rvBatches;
    BatchAdapter adapter;
    DatabaseHelper dbHelper;
    List<Batch> batchList;
    TextView tvEmpty, tvText;
    Button btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batches);

        // --- Initialize views ---
        rvBatches = findViewById(R.id.rvBatches);
        rvBatches.setLayoutManager(new LinearLayoutManager(this));

        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BatchesActivity.this, MainActivity.class));
            }
        });
        tvEmpty = findViewById(R.id.tvEmpty);
        tvText = findViewById(R.id.tvText);

        // --- Initialize database ---
        dbHelper = new DatabaseHelper(this);

        batchList = new ArrayList<>();
        loadBatchesFromDB();

        // --- Show empty state if no batches ---
        if (batchList.isEmpty()) {
            rvBatches.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            tvText.setVisibility(View.GONE);
        } else {
            rvBatches.setVisibility(View.VISIBLE);
            tvEmpty.setVisibility(View.GONE);
            tvText.setVisibility(View.VISIBLE);
        }

        // --- Set adapter ---
        adapter = new BatchAdapter(batchList);
        rvBatches.setAdapter(adapter);
    }

    private void loadBatchesFromDB() {
        Cursor cursor = dbHelper.getAllBatches();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String startDate = cursor.getString(cursor.getColumnIndex("start_date"));
                @SuppressLint("Range") double fee = cursor.getDouble(cursor.getColumnIndex("fee_per_month"));

                batchList.add(new Batch(id, name, startDate, fee));
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}
