package com.testapp.paymenttracker;

import android.app.AlertDialog;
import android.content.Intent;
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

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;

    public StudentAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentName.setText(student.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), StudentPaymentActivity.class);
            intent.putExtra("studentId", student.getId());
            intent.putExtra("studentName", student.getName());
            v.getContext().startActivity(intent);
        });

        holder.editBtn.setOnClickListener(v -> {
            // Show an AlertDialog with EditText
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setTitle("Edit Student Name");

            final EditText input = new EditText(v.getContext());
            input.setText(student.getName()); // prefill current name
            builder.setView(input);

            builder.setPositiveButton("Update", (dialog, which) -> {
                String newName = input.getText().toString().trim();
                if (!newName.isEmpty()) {
                    DatabaseHelper dbHelper = new DatabaseHelper(v.getContext());
                    boolean updated = dbHelper.updateStudent(student.getId(), newName);
                    if (updated) {
                        student.setName(newName); // update local list
                        notifyItemChanged(position);
                        Toast.makeText(v.getContext(), "Student updated successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(v.getContext(), "Update failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
        });
        holder.itemView.setOnLongClickListener(v -> {
            // Show confirmation dialog
            new androidx.appcompat.app.AlertDialog.Builder(v.getContext())
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete " + student.getName() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Delete from database
                        DatabaseHelper dbHelper = new DatabaseHelper(v.getContext());
                        boolean deleted = dbHelper.deleteStudent(student.getId());
                        if (deleted) {
                            // Remove from RecyclerView
                            studentList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, studentList.size());
                            Toast.makeText(v.getContext(), "Student deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(v.getContext(), "Error deleting student", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                    .show();
            return true; // consume the long click
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;
        Button editBtn;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            editBtn = itemView.findViewById(R.id.btnEditStudent);
        }
    }
}
