package com.testapp.paymenttracker;

import android.app.AlertDialog;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BatchAdapter extends RecyclerView.Adapter<BatchAdapter.BatchViewHolder> {

    private List<Batch> batchList;

    public BatchAdapter(List<Batch> batchList) {
        this.batchList = batchList;
    }

    @NonNull
    @Override
    public BatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_batch, parent, false);
        return new BatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BatchViewHolder holder, int position) {
        Batch batch = batchList.get(position);
        holder.tvBatchName.setText(batch.getName());
        holder.tvStartDate.setText("Start Date: " + batch.getStartDate());
        holder.tvFee.setText("Monthly Fee: " + batch.getFee());
        holder.batchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), BatchDetailsActivity.class);
                intent.putExtra("batchId", batch.getId());
                intent.putExtra("BATCH_NAME", batch.getName());
                intent.putExtra("BATCH_FEE", batch.getFee());
                intent.putExtra("BATCH_START_DATE", batch.getStartDate());
                v.getContext().startActivity(intent);
            }
        });
        holder.btnDeleteBatch.setOnClickListener(v -> {
            // Show a PIN dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Enter PIN to delete");

            // Create an EditText for PIN input
            final EditText input = new EditText(v.getContext());
            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                String enteredPin = input.getText().toString();
                if (enteredPin.equals("123")) {
                    // Correct PIN, delete batch
                    DatabaseHelper dbHelper = new DatabaseHelper(v.getContext());
                    boolean deleted = dbHelper.deleteBatch(batch.getId());
                    if (deleted) {
                        batchList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, batchList.size());
                        Toast.makeText(v.getContext(), "Batch deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "Error deleting batch", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(v.getContext(), "Incorrect PIN", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }

    @Override
    public int getItemCount() {
        return batchList.size();
    }

    static class BatchViewHolder extends RecyclerView.ViewHolder {
        TextView tvBatchName, tvStartDate, tvFee;
        Button btnDeleteBatch;
        View batchCard;
        public BatchViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBatchName = itemView.findViewById(R.id.tvBatchName);
            tvStartDate = itemView.findViewById(R.id.tvStartDate);
            tvFee = itemView.findViewById(R.id.tvFee);
            btnDeleteBatch = itemView.findViewById(R.id.btnDeleteBatch);
            batchCard = itemView.findViewById(R.id.batchCard);
        }
    }
}
