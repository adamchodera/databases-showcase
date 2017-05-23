package pl.adamchodera.databasesshowcase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.adamchodera.databasesshowcase.data.TaskEntity;
import pl.adamchodera.databasesshowcase.data.TasksDataSource;

public class TaskDetailsActivity extends AppCompatActivity {

    private static final String INTENT_TASK_ID = "ARG_task_id";

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.title_edit_text)
    EditText titleEditText;

    @BindView(R.id.description_edit_text)
    EditText descriptionEditText;

    private TasksDataSource tasksDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
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
        String title = titleEditText.getText().toString();
        String description = descriptionEditText.getText().toString();

        final TaskEntity task = new TaskEntity(title, description, false);

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
                titleEditText.setEnabled(false);
                descriptionEditText.setEnabled(false);
            } else {
                message = "Error occurred..";
            }

            Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
