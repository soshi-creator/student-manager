package com.example.studentmanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studentmanager.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseHelper dbHelper;
    private ArrayList<Student> studentList;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Student Manager");

        dbHelper = new DatabaseHelper(this);

        studentList = new ArrayList<>();
        adapter = new StudentAdapter(this, studentList, dbHelper, this::showEditDialog);
        binding.rvStudents.setLayoutManager(new LinearLayoutManager(this));
        binding.rvStudents.setAdapter(adapter);

        loadStudents();

        binding.btnAddStudent.setOnClickListener(v -> showAddStudentDialog());

        binding.btnDeleteAll.setOnClickListener(v -> {
            if (studentList.isEmpty()) {
                Toast.makeText(this, "No students to delete", Toast.LENGTH_SHORT).show();
                return;
            }

            new MaterialAlertDialogBuilder(this)
                    .setTitle("Delete All Students")
                    .setMessage("Are you sure you want to delete all students?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dbHelper.deleteAllStudents();
                        loadStudents();
                        Toast.makeText(this, "All students deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    private void loadStudents() {
        studentList.clear();
        studentList.addAll(dbHelper.getAllStudentsList());
        adapter.notifyDataSetChanged();

        if (studentList.isEmpty()) {
            binding.emptyStateView.setVisibility(View.VISIBLE);
            binding.rvStudents.setVisibility(View.GONE);
            binding.btnDeleteAll.setEnabled(false);
        } else {
            binding.emptyStateView.setVisibility(View.GONE);
            binding.rvStudents.setVisibility(View.VISIBLE);
            binding.btnDeleteAll.setEnabled(true);
        }
    }

    private void showAddStudentDialog() {
        showStudentDialog(null);
    }

    private void showEditDialog(Student student) {
        showStudentDialog(student);
    }

    private void showStudentDialog(Student studentToEdit) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_student, null);
        TextInputEditText etName = dialogView.findViewById(R.id.etName);
        TextInputEditText etAge = dialogView.findViewById(R.id.etAge);
        TextInputEditText etCourse = dialogView.findViewById(R.id.etCourse);

        boolean isEdit = studentToEdit != null;
        if (isEdit) {
            etName.setText(studentToEdit.getName());
            etAge.setText(String.valueOf(studentToEdit.getAge()));
            etCourse.setText(studentToEdit.getCourse());
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(isEdit ? "Edit Student" : "Add Student")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String ageStr = etAge.getText().toString().trim();
                    String course = etCourse.getText().toString().trim();

                    if (name.isEmpty() || ageStr.isEmpty() || course.isEmpty()) {
                        Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int age;
                    try {
                        age = Integer.parseInt(ageStr);
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Invalid age", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (isEdit) {
                        dbHelper.updateStudent(studentToEdit.getId(), name, age, course);
                        Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show();
                    } else {
                        dbHelper.addStudent(name, age, course);
                        Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
                    }

                    loadStudents();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}