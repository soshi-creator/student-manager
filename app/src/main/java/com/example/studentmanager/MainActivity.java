package com.example.studentmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.studentmanager.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Student Manager");

        dbHelper = new DatabaseHelper(this);

        binding.btnAdd.setOnClickListener(v -> {
            String name = binding.etName.getText().toString().trim();
            String ageStr = binding.etAge.getText().toString().trim();
            String course = binding.etCourse.getText().toString().trim();

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

            boolean inserted = dbHelper.addStudent(name, age, course);

            if (inserted) {
                Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
                binding.etName.setText("");
                binding.etAge.setText("");
                binding.etCourse.setText("");
            } else {
                Toast.makeText(this, "Error adding student", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnView.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ViewStudentsActivity.class));
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_exit) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
