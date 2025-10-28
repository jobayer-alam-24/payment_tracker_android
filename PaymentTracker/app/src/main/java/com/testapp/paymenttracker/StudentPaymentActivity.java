package com.testapp.paymenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class StudentPaymentActivity extends AppCompatActivity {

    private TextView tvStudentName;
    private CheckBox[] monthCheckboxes = new CheckBox[12];
    private Button btnSavePayments;

    private int studentId;
    private DatabaseHelper dbHelper;
    private Student student;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_payment);

        tvStudentName = findViewById(R.id.tvStudentName);
        btnSavePayments = findViewById(R.id.btnSavePayments);



        // Map checkboxes
        monthCheckboxes[0] = findViewById(R.id.cbJan);
        monthCheckboxes[1] = findViewById(R.id.cbFeb);
        monthCheckboxes[2] = findViewById(R.id.cbMar);
        monthCheckboxes[3] = findViewById(R.id.cbApr);
        monthCheckboxes[4] = findViewById(R.id.cbMay);
        monthCheckboxes[5] = findViewById(R.id.cbJun);
        monthCheckboxes[6] = findViewById(R.id.cbJul);
        monthCheckboxes[7] = findViewById(R.id.cbAug);
        monthCheckboxes[8] = findViewById(R.id.cbSep);
        monthCheckboxes[9] = findViewById(R.id.cbOct);
        monthCheckboxes[10] = findViewById(R.id.cbNov);
        monthCheckboxes[11] = findViewById(R.id.cbDec);

        dbHelper = new DatabaseHelper(this);

        // Get student info from Intent
        studentId = getIntent().getIntExtra("studentId", -1);
        String studentName = getIntent().getStringExtra("studentName");
        tvStudentName.setText(studentName);

        // Load monthly payment status
        boolean[] paidStatus = dbHelper.getMonthlyPaymentStatus(studentId);
        for (int i = 0; i < 12; i++) {
            monthCheckboxes[i].setChecked(paidStatus[i]);
        }

        // Save button
        btnSavePayments.setOnClickListener(v -> {
            for (int i = 0; i < 12; i++) {
                dbHelper.updateMonthlyPayment(studentId, i + 1, monthCheckboxes[i].isChecked());
            }
            Toast.makeText(this, "Payments updated successfully", Toast.LENGTH_SHORT).show();
        });
    }
}
