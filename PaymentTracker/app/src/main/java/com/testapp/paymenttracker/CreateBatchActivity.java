package com.testapp.paymenttracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateBatchActivity extends AppCompatActivity {
    private AppCompatButton btnSaveBatch, home;
    private EditText etBatchName, etStartDate, etFee;
    DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_batch);
        etBatchName = findViewById(R.id.etBatchName);
        etStartDate = findViewById(R.id.etStartDate);
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateBatchActivity.this, MainActivity.class));
            }
        });
        etFee = findViewById(R.id.etFee);
        btnSaveBatch = findViewById(R.id.btnSaveBatch);
        dbHelper = new DatabaseHelper(this);
        etStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        btnSaveBatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBatchToDatabase();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CreateBatchActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                        etStartDate.setText(date);
                    }
                }, year, month, day);
        datePickerDialog.show();
    }
    private void saveBatchToDatabase() {
        String name = etBatchName.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String feeStr = etFee.getText().toString().trim();

        // --- Validate inputs ---
        if (name.isEmpty() || startDate.isEmpty() || feeStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double fee;
        try {
            fee = Double.parseDouble(feeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid fee value", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- Insert into database ---
        boolean inserted = dbHelper.insertBatch(name, startDate, fee);

        if (inserted) {
            Toast.makeText(this, "Batch added successfully!", Toast.LENGTH_SHORT).show();
            etBatchName.setText("");
            etStartDate.setText("");
            etFee.setText("");
        } else {
            Toast.makeText(this, "Error adding batch", Toast.LENGTH_SHORT).show();
        }
    }
}