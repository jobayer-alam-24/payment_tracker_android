package com.testapp.paymenttracker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class StudentPaymentActivity extends AppCompatActivity {

    private TextView tvStudentName;
    private CheckBox[] monthCheckboxes = new CheckBox[12];
    private Button[] customFeeButtons = new Button[12];
    private double[] customFees = new double[12];
    private Button btnSavePayments;

    private int studentId;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_payment);

        tvStudentName = findViewById(R.id.tvStudentName);
        btnSavePayments = findViewById(R.id.btnSavePayments);

        dbHelper = new DatabaseHelper(this);

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

        // Map custom fee buttons
        customFeeButtons[0] = findViewById(R.id.btnCustomJan);
        customFeeButtons[1] = findViewById(R.id.btnCustomFeb);
        customFeeButtons[2] = findViewById(R.id.btnCustomMar);
        customFeeButtons[3] = findViewById(R.id.btnCustomApr);
        customFeeButtons[4] = findViewById(R.id.btnCustomMay);
        customFeeButtons[5] = findViewById(R.id.btnCustomJun);
        customFeeButtons[6] = findViewById(R.id.btnCustomJul);
        customFeeButtons[7] = findViewById(R.id.btnCustomAug);
        customFeeButtons[8] = findViewById(R.id.btnCustomSep);
        customFeeButtons[9] = findViewById(R.id.btnCustomOct);
        customFeeButtons[10] = findViewById(R.id.btnCustomNov);
        customFeeButtons[11] = findViewById(R.id.btnCustomDec);

        // Get student info from Intent
        studentId = getIntent().getIntExtra("studentId", -1);
        String studentName = getIntent().getStringExtra("studentName");
        tvStudentName.setText(studentName);

        // Load monthly payment status
        boolean[] paidStatus = dbHelper.getMonthlyPaymentStatus(studentId);
        for (int i = 0; i < 12; i++) {
            monthCheckboxes[i].setChecked(paidStatus[i]);
        }

        // Custom fee button listeners
        for (int i = 0; i < 12; i++) {
            final int monthIndex = i;
            customFeeButtons[i].setOnClickListener(v -> {
                if (!monthCheckboxes[monthIndex].isChecked()) {
                    Toast.makeText(this, "Check the month first", Toast.LENGTH_SHORT).show();
                    return;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Enter custom fee for " + getMonthName(monthIndex + 1));

                final EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);

                builder.setPositiveButton("OK", (dialog, which) -> {
                    String feeStr = input.getText().toString().trim();
                    if (!feeStr.isEmpty()) {
                        customFees[monthIndex] = Double.parseDouble(feeStr);
                        Toast.makeText(this, "Custom fee set for " + getMonthName(monthIndex + 1), Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            });
        }

        // Main Save Payments
        btnSavePayments.setOnClickListener(v -> {
            for (int i = 0; i < 12; i++) {
                if (monthCheckboxes[i].isChecked()) {
                    int month = i + 1;
                    if (customFees[i] > 0) {
                        dbHelper.updateCustomFee(studentId, month, customFees[i]);
                    } else {
                        dbHelper.updateMonthlyPayment(studentId, month, true); // batch fee
                    }
                }
            }
            Toast.makeText(this, "Payments updated successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private String getMonthName(int month) {
        String[] months = {"January","February","March","April","May","June","July","August","September","October","November","December"};
        return months[month - 1];
    }
}
