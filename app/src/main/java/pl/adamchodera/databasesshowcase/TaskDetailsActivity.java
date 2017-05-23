package pl.adamchodera.databasesshowcase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.adamchodera.databasesshowcase.data.TaskEntity;
import pl.adamchodera.databasesshowcase.data.TasksDataSource;

public class TaskDetailsActivity extends AppCompatActivity {

    private static final String INTENT_TASK_ID = "ARG_task_id";
    private TasksDataSource tasksDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        ButterKnife.bind(this);

        tasksDataSource = TasksDataSource.getInstance(this);

        final String taskId = getIntent().getStringExtra(INTENT_TASK_ID);
        if (taskId == null) {
            // new task
        } else {
            // edit mode
            final TaskEntity task = tasksDataSource.getTask(taskId);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        tasksDataSource.closeDatabase();
    }

    @OnClick(R.id.save_task_button)
    public void saveTask() {
        final TaskEntity task = new TaskEntity(null, "tytuł notatki", "podtyułe", false);

        new SaveTaskInDatabaseAsyncTask(task).execute();
    }

    private class SaveTaskInDatabaseAsyncTask extends AsyncTask<String, Void, Boolean> {

        private final TaskEntity task;

        SaveTaskInDatabaseAsyncTask(final TaskEntity task) {
            this.task = task;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                tasksDataSource.saveTask(task);
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);

            String message;
            if (success) {
                message = "Task saved!";
            } else {
                message = "Error occurred..";
            }

            Toast.makeText(TaskDetailsActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
