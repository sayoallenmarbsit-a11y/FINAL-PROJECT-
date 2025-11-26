package com.act1.mytodolistapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;
    private FloatingActionButton btnAdd, btnDelete;
    private EditText etTitle, etContent;
    private View bottomInputPanel;
    private Button btnSaveBottom;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recycler_notes);
        btnAdd = findViewById(R.id.btn_add);
        btnDelete = findViewById(R.id.btn_delete);
        etTitle = findViewById(R.id.et_title);
        etContent = findViewById(R.id.et_content);
        bottomInputPanel = findViewById(R.id.bottom_input_panel);
        btnSaveBottom = findViewById(R.id.btn_save_bottom);

        // Load tasks from DB
        taskList = dbHelper.getAllTasks();
        if (taskList == null) taskList = new ArrayList<>();

        taskAdapter = new TaskAdapter(taskList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        // Listen for selection changes to enable delete button
        taskAdapter.setOnTaskSelectedListener(task -> btnDelete.setEnabled(task != null));

        btnAdd.setOnClickListener(v -> bottomInputPanel.setVisibility(View.VISIBLE));

        btnSaveBottom.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();
            if (!title.isEmpty()) {
                String dateTime = getCurrentDateTimeString();
                Task task = new Task(title, content, dateTime);

                // Save to DB
                dbHelper.addTask(task);
                taskAdapter.addTask(task);

                etTitle.setText("");
                etContent.setText("");
                bottomInputPanel.setVisibility(View.GONE);
            }
        });

        btnDelete.setOnClickListener(v -> {
            Task selectedTask = taskAdapter.getSelectedTask();
            if (selectedTask != null) {
                dbHelper.deleteTask(selectedTask);
                taskAdapter.removeSelectedTask();
                btnDelete.setEnabled(false);
            }
        });
    }

    private String getCurrentDateTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
