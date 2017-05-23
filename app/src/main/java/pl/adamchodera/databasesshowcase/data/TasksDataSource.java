/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.adamchodera.databasesshowcase.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of a data source as a db.
 */
public class TasksDataSource {

    private static TasksDataSource INSTANCE;

    private DatabaseHelper databaseHelper;

    // Prevent direct instantiation
    private TasksDataSource(@NonNull Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static synchronized TasksDataSource getInstance(@NonNull Context context) {
        // Use the application context, which will ensure that you don't accidentally leak an Activity's context.
        // See this article for more information:
        // http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
        if (INSTANCE == null) {
            INSTANCE = new TasksDataSource(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public void closeDatabase() {
        databaseHelper.close();
    }

    public void saveTask(@NonNull TaskEntity task) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoContract.TaskEntry.COLUMN_NAME_TITLE, task.getTitle());
        values.put(TodoContract.TaskEntry.COLUMN_NAME_DESCRIPTION, task.getDescription());
        values.put(TodoContract.TaskEntry.COLUMN_NAME_COMPLETED, task.isCompleted());

        db.insertOrThrow(TodoContract.TaskEntry.TABLE_NAME, null, values);
    }

    public List<TaskEntity> getTasks() {
        List<TaskEntity> tasks = new ArrayList<TaskEntity>();
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                TodoContract.TaskEntry._ID,
                TodoContract.TaskEntry.COLUMN_NAME_TITLE,
                TodoContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TodoContract.TaskEntry.COLUMN_NAME_COMPLETED
        };

        Cursor c = db.query(TodoContract.TaskEntry.TABLE_NAME, projection, null, null, null, null, null);

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String itemId = c.getString(c.getColumnIndexOrThrow(TodoContract.TaskEntry._ID));
                String title = c.getString(c.getColumnIndexOrThrow(TodoContract.TaskEntry.COLUMN_NAME_TITLE));
                String description =
                        c.getString(c.getColumnIndexOrThrow(TodoContract.TaskEntry.COLUMN_NAME_DESCRIPTION));
                boolean completed =
                        c.getInt(c.getColumnIndexOrThrow(TodoContract.TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
                TaskEntity task = new TaskEntity(itemId, title, description, completed);
                tasks.add(task);
            }
        }
        if (c != null) {
            c.close();
        }

        return tasks;
    }

    public TaskEntity getTask(@NonNull String taskId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String[] projection = {
                TodoContract.TaskEntry._ID,
                TodoContract.TaskEntry.COLUMN_NAME_TITLE,
                TodoContract.TaskEntry.COLUMN_NAME_DESCRIPTION,
                TodoContract.TaskEntry.COLUMN_NAME_COMPLETED
        };

        String selection = TodoContract.TaskEntry._ID + " LIKE ?";
        String[] selectionArgs = {taskId};

        Cursor c = db.query(
                TodoContract.TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        TaskEntity task = null;

        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            String itemId = c.getString(c.getColumnIndexOrThrow(TodoContract.TaskEntry._ID));
            String title = c.getString(c.getColumnIndexOrThrow(TodoContract.TaskEntry.COLUMN_NAME_TITLE));
            String description =
                    c.getString(c.getColumnIndexOrThrow(TodoContract.TaskEntry.COLUMN_NAME_DESCRIPTION));
            boolean completed =
                    c.getInt(c.getColumnIndexOrThrow(TodoContract.TaskEntry.COLUMN_NAME_COMPLETED)) == 1;
            task = new TaskEntity(itemId, title, description, completed);
        }
        if (c != null) {
            c.close();
        }

        return task;
    }

    public void completeTask(@NonNull TaskEntity task) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoContract.TaskEntry.COLUMN_NAME_COMPLETED, true);

        String selection = TodoContract.TaskEntry._ID + " LIKE ?";
        String[] selectionArgs = {task.getId()};

        db.update(TodoContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void activateTask(@NonNull TaskEntity task) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoContract.TaskEntry.COLUMN_NAME_COMPLETED, false);

        String selection = TodoContract.TaskEntry._ID + " LIKE ?";
        String[] selectionArgs = {task.getId()};

        db.update(TodoContract.TaskEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void clearCompletedTasks() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String selection = TodoContract.TaskEntry.COLUMN_NAME_COMPLETED + " LIKE ?";
        String[] selectionArgs = {"1"};

        db.delete(TodoContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }

    public void deleteAllTasks() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        db.delete(TodoContract.TaskEntry.TABLE_NAME, null, null);
    }

    public void deleteTask(@NonNull String taskId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        String selection = TodoContract.TaskEntry._ID + " LIKE ?";
        String[] selectionArgs = {taskId};

        db.delete(TodoContract.TaskEntry.TABLE_NAME, selection, selectionArgs);
    }
}
