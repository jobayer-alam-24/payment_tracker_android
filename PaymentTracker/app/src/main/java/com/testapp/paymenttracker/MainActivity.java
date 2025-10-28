package com.testapp.paymenttracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView totalTextView;
    private DatabaseHelper dbHelper;
    private Button btn, allBatches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize views
        totalTextView = findViewById(R.id.total);
        btn = findViewById(R.id.btnCreateBatch);
        allBatches = findViewById(R.id.btnViewBatches);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Apply window insets (for full-screen layout handling)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up button clicks
        btn.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CreateBatchActivity.class))
        );

        allBatches.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, BatchesActivity.class))
        );

        // Update total payment when activity first starts
        updateTotalPayment();
    }

    // Refresh total payment each time user comes back to this activity
    @Override
    protected void onResume() {
        super.onResume();
        updateTotalPayment();
    }

    // Method to update and display the total payment
    private void updateTotalPayment() {
        double totalPayment = dbHelper.getTotalFeesForAllBatches();
        totalTextView.setText("Total Payment: " + totalPayment + " BDT");
        totalTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
    }
}
