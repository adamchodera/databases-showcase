package pl.adamchodera.databasesshowcase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import pl.adamchodera.databasesshowcase.data.DaoSession;
import pl.adamchodera.databasesshowcase.data.Task;
import pl.adamchodera.databasesshowcase.data.TaskDao;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the note DAO
        DaoSession daoSession = ((MyApplication) getApplication()).getDaoSession();
        final TaskDao taskDao = daoSession.getTaskDao();

        Task task = new Task();
        task.setTitle("1");
        task.setDescription("description");

        taskDao.save(task);

        QueryBuilder<Task> qb = taskDao.queryBuilder();
        qb.where(TaskDao.Properties.IsCompleted.eq(false));

        List<Task> notCompletedTasks = qb.list();
    }
}
