package com.act1.mytodolistapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "tasks_db";
    private static final int DATABASE_VERSION = 2; // incremented for new column
    private static final String TABLE_TASKS = "tasks";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_DATETIME = "date_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_DATETIME + " TEXT" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    // Add task
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_CONTENT, task.getContent());
        values.put(KEY_DATETIME, task.getDateTime());
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    // Get all tasks
    public List<Task> getAllTasks() {
        List<Task> taskList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE));
                String content = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CONTENT));
                String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(KEY_DATETIME));
                taskList.add(new Task(title, content, dateTime));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return taskList;
    }

    // Delete task
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_TITLE + "=? AND " + KEY_CONTENT + "=? AND " + KEY_DATETIME + "=?",
                new String[]{task.getTitle(), task.getContent(), task.getDateTime()});
        db.close();
    }
}
