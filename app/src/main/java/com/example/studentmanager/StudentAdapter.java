package com.example.studentmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context context;
    private ArrayList<Student> students;
    private DatabaseHelper dbHelper;
    private StudentActionListener actionListener;

    public interface StudentActionListener {
        void onEdit(Student student);
    }

    public StudentAdapter(Context context, ArrayList<Student> students, DatabaseHelper dbHelper, StudentActionListener listener) {
        this.context = context;
        this.students = students;
        this.dbHelper = dbHelper;
        this.actionListener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = students.get(position);
        holder.tvName.setText(student.getName());
        holder.tvDetails.setText("Age: " + student.getAge() + " | Course: " + student.getCourse());

        holder.btnEdit.setOnClickListener(v -> {
            if (actionListener != null) {
                actionListener.onEdit(student);
            }
        });

        holder.btnDelete.setOnClickListener(v -> {
            showDeleteConfirmationDialog(student, position);
        });
    }

    private void showDeleteConfirmationDialog(Student student, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Student");
        builder.setMessage("Are you sure you want to delete " + student.getName() + "?");

        builder.setPositiveButton("Delete", (dialog, which) -> {
            boolean deleted = dbHelper.deleteStudent(student.getId());
            if (deleted) {
                students.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, students.size());
                Toast.makeText(context, "Student deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Error deleting student", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails;
        MaterialButton btnEdit, btnDelete;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}