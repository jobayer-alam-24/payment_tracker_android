package com.testapp.paymenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewStudentsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewStudents;
    private StudentAdapter studentAdapter;
    private DatabaseHelper dbHelper;
    private Button back;
    private int batchId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);

        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewStudentsActivity.this, BatchesActivity.class));
            }
        });

        batchId = getIntent().getIntExtra("batchId", -1);

        if (batchId == -1) {
            Toast.makeText(this, "Error: Invalid batch ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        loadStudentsForBatch(batchId);
    }

    private void loadStudentsForBatch(int batchId) {
        List<Student> studentList = dbHelper.getStudentsByBatch(batchId);

        if (studentList.isEmpty()) {
            Toast.makeText(this, "No students found for this batch", Toast.LENGTH_SHORT).show();
        }

        studentAdapter = new StudentAdapter(studentList);
        recyclerViewStudents.setAdapter(studentAdapter);
    }
}
