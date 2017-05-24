package pl.adamchodera.databasesshowcase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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

    @BindView(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;

    private TasksDataSource tasksDataSource;
    private SaveOrEditTaskInDatabaseAsyncTask saveOrEditTaskInDatabaseAsyncTask;
    private boolean editMode = true;
    private TaskEntity currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        ButterKnife.bind(this);

        tasksDataSource = TasksDataSource.getInstance(this);

        final String taskId = getIntent().getStringExtra(INTENT_TASK_ID);
        if (taskId != null) {
            // user clicked task from the list
            setReadonlyMode();
            currentTask = tasksDataSource.getTask(taskId);

            titleEditText.setText(currentTask.getTitle());
            descriptionEditText.setText(currentTask.getDescription());
        } else {
            currentTask = new TaskEntity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (saveOrEditTaskInDatabaseAsyncTask != null) {
            saveOrEditTaskInDatabaseAsyncTask.cancel(true);
        }
        tasksDataSource.closeDatabase();
    }

    @OnClick(R.id.floating_action_button)
    public void onFabClicked() {
        if (editMode) {
            // during edit mode we display "done" aka "save" icon on fab button
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();

            currentTask.setTitle(title);
            currentTask.setDescription(description);

            saveOrEditTaskInDatabaseAsyncTask = new SaveOrEditTaskInDatabaseAsyncTask(currentTask);
            saveOrEditTaskInDatabaseAsyncTask.execute();
        } else {
            // user clicked on the pencil icon so he wants to enable edit mode
            setEditMode();
        }
    }

    private void setReadonlyMode() {
        editMode = false;
        titleEditText.setEnabled(false);
        descriptionEditText.setEnabled(false);
        floatingActionButton.setImageResource(R.drawable.ic_mode_edit_white_24px);
    }

    private void setEditMode() {
        editMode = true;
        titleEditText.setEnabled(true);
        descriptionEditText.setEnabled(true);
        floatingActionButton.setImageResource(R.drawable.ic_done_white_24px);
    }

    private class SaveOrEditTaskInDatabaseAsyncTask extends AsyncTask<String, Void, Boolean> {

        private final TaskEntity task;

        SaveOrEditTaskInDatabaseAsyncTask(final TaskEntity task) {
            this.task = task;
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (task.isTaskInDatabase()) {
                tasksDataSource.updateTask(task);
                return true;
            } else {
                // we're saving new task in database
                final long taskId = tasksDataSource.saveTask(task);
                currentTask.setId(taskId);
                return currentTask.isTaskInDatabase();
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            super.onPostExecute(success);

            String message;
            if (success) {
                message = "Task saved!";
                setReadonlyMode();
            } else {
                message = "Error occurred..";
                setEditMode();
            }

            Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG)
                    .show();
        }
    }
}
