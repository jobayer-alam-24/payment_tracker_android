package com.testapp.paymenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BatchDetailsActivity extends AppCompatActivity {

    private TextView batchNameText, totalStudents, createdAt;
    private int batchId;
    private String batchName, startDate;
    private Button btnHome;
    private double fee;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batch_details);
        batchNameText = findViewById(R.id.batchName);
        totalStudents = findViewById(R.id.totalStudents);
        createdAt = findViewById(R.id.createdOn);
        btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BatchDetailsActivity.this, MainActivity.class));
            }
        });
        dbHelper = new DatabaseHelper(this);
        // Receive data
        batchId = getIntent().getIntExtra("batchId", -1);
        batchName = getIntent().getStringExtra("BATCH_NAME");
        startDate = getIntent().getStringExtra("BATCH_START_DATE");
        fee = getIntent().getDoubleExtra("BATCH_FEE", 0);


        Button btnCreateStudent = findViewById(R.id.btnCreateStudent);
        Button btnViewStudents = findViewById(R.id.btnViewStudents);

        // Show batch info
        batchNameText.setText("Batch Name: " + batchName);
        int totalCount = dbHelper.getStudentsByBatch(batchId).size();
        totalStudents.setText("Total Students: " + totalCount);
        createdAt.setText("Created On: " + startDate);


        // Create Student button
        btnCreateStudent.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateStudentActivity.class);
            intent.putExtra("batchId", batchId);
            startActivity(intent);
        });

        // View Students button
        btnViewStudents.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewStudentsActivity.class);
            intent.putExtra("batchId", batchId);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int totalCount = dbHelper.getStudentsByBatch(batchId).size();
        totalStudents.setText("Total Students: " + totalCount);
    }
}
