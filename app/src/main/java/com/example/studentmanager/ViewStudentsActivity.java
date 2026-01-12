package com.example.studentmanager;

import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.studentmanager.databinding.ActivityViewStudentsBinding;

import java.util.ArrayList;

public class ViewStudentsActivity extends AppCompatActivity {

    private ActivityViewStudentsBinding binding;
    private DatabaseHelper dbHelper;
    private ArrayList<Student> studentList;
    private StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewStudentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Students");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);

        studentList = new ArrayList<>();
        adapter = new StudentAdapter(this, studentList, dbHelper);

        binding.rvStudents.setLayoutManager(new LinearLayoutManager(this));
        binding.rvStudents.setAdapter(adapter);

        loadStudents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadStudents();
    }

    private void loadStudents() {
        studentList.clear();
        Cursor cursor = dbHelper.getAllStudents();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                studentList.add(new Student(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3)
                ));
            }
            cursor.close();
        }

        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
