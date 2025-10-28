package com.testapp.paymenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateStudentActivity extends AppCompatActivity {

    private int batchId;
    private EditText etStudentName;
    private Button btnSaveStudent, home;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_student);


        batchId = getIntent().getIntExtra("batchId", -1);

        home = findViewById(R.id.home);
        etStudentName = findViewById(R.id.etStudentName);
        btnSaveStudent = findViewById(R.id.btnSaveStudent);
        dbHelper = new DatabaseHelper(this);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateStudentActivity.this, ViewStudentsActivity.class);
                intent.putExtra("batchId", batchId);
                startActivity(intent);
            }
        });
        btnSaveStudent.setOnClickListener(v -> {
            String studentName = etStudentName.getText().toString().trim();

            if (studentName.isEmpty()) {
                Toast.makeText(this, "Please enter a student name", Toast.LENGTH_SHORT).show();
                return;
            }


            boolean inserted = dbHelper.insertStudent(batchId, studentName);

            if (inserted) {
                Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show();
                etStudentName.setText(""); // clear input
            } else {
                Toast.makeText(this, "Error adding student", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
