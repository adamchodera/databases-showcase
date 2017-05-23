package pl.adamchodera.databasesshowcase;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.adamchodera.databasesshowcase.data.TaskEntity;
import pl.adamchodera.databasesshowcase.data.TasksDataSource;

public class TasksListActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TasksDataSource tasksDataSource;
    private TasksListAdapter tasksListAdapter;
    private LoadTasksFromDatabaseAsyncTask loadTasksFromDatabaseAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tasksDataSource = TasksDataSource.getInstance(this);

        initTaskList();
    }

    private void initTaskList() {
        tasksListAdapter = new TasksListAdapter();
        recyclerView.setAdapter(tasksListAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTasksFromDatabaseAsyncTask = new LoadTasksFromDatabaseAsyncTask();
        loadTasksFromDatabaseAsyncTask.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();

        loadTasksFromDatabaseAsyncTask.cancel(true);
        tasksDataSource.closeDatabase();
    }

    @OnClick(R.id.add_task_button)
    public void goToAddNoteActivity() {
        Intent intent = new Intent(this, TaskDetailsActivity.class);
        startActivity(intent);
    }

    private class LoadTasksFromDatabaseAsyncTask extends AsyncTask<String, Void, List<TaskEntity>> {

        @Override
        protected List<TaskEntity> doInBackground(String... params) {
            return tasksDataSource.getTasks();
        }

        @Override
        protected void onPostExecute(final List<TaskEntity> tasks) {
            super.onPostExecute(tasks);

            tasksListAdapter.setTasks(tasks);
            tasksListAdapter.notifyDataSetChanged();
        }
    }
}
