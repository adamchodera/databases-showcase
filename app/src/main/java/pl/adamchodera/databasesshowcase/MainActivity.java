package pl.adamchodera.databasesshowcase;

import android.content.Intent;
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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private TasksDataSource tasksDataSource;
    private TasksListAdapter tasksListAdapter;

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
        final List<TaskEntity> tasks = tasksDataSource.getTasks();
        tasksListAdapter.setTasks(tasks);
        tasksListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();

        tasksDataSource.closeDatabase();
    }

    @OnClick(R.id.add_task_button)
    public void goToAddNoteActivity() {
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        startActivity(intent);
    }
}
