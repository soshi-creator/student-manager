package com.example.studentmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private Context context;
    private ArrayList<Student> students;
    private DatabaseHelper dbHelper;

    public StudentAdapter(Context context, ArrayList<Student> students, DatabaseHelper dbHelper) {
        this.context = context;
        this.students = students;
        this.dbHelper = dbHelper;
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
            Intent intent = new Intent(context, EditStudentActivity.class);
            intent.putExtra("id", student.getId());
            context.startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
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
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails;
        Button btnEdit, btnDelete;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
