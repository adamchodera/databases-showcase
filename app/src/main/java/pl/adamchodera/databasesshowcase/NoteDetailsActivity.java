package pl.adamchodera.databasesshowcase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.adamchodera.databasesshowcase.data.TaskEntity;
import pl.adamchodera.databasesshowcase.data.TasksDataSource;

public class NoteDetailsActivity extends AppCompatActivity {

    private TasksDataSource tasksDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        ButterKnife.bind(this);

        tasksDataSource = TasksDataSource.getInstance(this);

//        final String taskId = "1";
//        final TaskEntity task = tasksDataSource.getTask(taskId);
    }

    @Override
    protected void onStop() {
        super.onStop();

        tasksDataSource.closeDatabase();
    }

    @OnClick(R.id.save_task_button)
    public void saveTask() {
        final TaskEntity task = new TaskEntity(null, "tytuł notatki", "podtyułe", false);

        tasksDataSource.saveTask(task);
    }
}
