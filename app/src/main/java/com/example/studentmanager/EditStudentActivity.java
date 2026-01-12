package com.example.studentmanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class EditStudentActivity extends AppCompatActivity {

    private EditText etName, etAge, etCourse;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int studentId;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);

        etName = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etCourse = findViewById(R.id.etCourse);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> {
            finish();
        });

        dbHelper = new DatabaseHelper(this);

        studentId = getIntent().getIntExtra("id", -1);
        if (studentId == -1) {
            Toast.makeText(this, "Invalid student ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadStudent();

        btnSave.setOnClickListener(v -> updateStudent());
    }

    private void loadStudent() {
        Cursor cursor = dbHelper.getStudentById(studentId);

        if (cursor != null && cursor.moveToFirst()) {
            etName.setText(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            etAge.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow("age"))));
            etCourse.setText(cursor.getString(cursor.getColumnIndexOrThrow("course")));
            cursor.close();
        } else {
            Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateStudent() {
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

        boolean updated = dbHelper.updateStudent(studentId, name, age, course);
        if (updated) {
            Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error updating student", Toast.LENGTH_SHORT).show();
        }
    }
}