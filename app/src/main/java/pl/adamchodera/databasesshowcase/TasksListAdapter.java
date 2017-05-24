package pl.adamchodera.databasesshowcase;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import pl.adamchodera.databasesshowcase.data.TaskEntity;

public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.ViewHolder> {

    private final TaskCompleteListener taskCompleteListener;
    private List<TaskEntity> tasks;

    public TasksListAdapter(final TaskCompleteListener taskCompleteListener) {
        this.taskCompleteListener = taskCompleteListener;
    }

    @Override
    public TasksListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_task, parent, false);

        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(TasksListAdapter.ViewHolder viewHolder, int position) {
        final TaskEntity task = tasks.get(position);
        final String titleWithId = task.getTitle() + " ID:" + task.getId();

        viewHolder.taskTitleView.setText(titleWithId);
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Context context = v.getContext();
                Intent intent = new Intent(context, TaskDetailsActivity.class);
                intent.putExtra(TaskDetailsActivity.INTENT_EXTRA_TASK_ID, task.getId());
                context.startActivity(intent);
            }
        });

        viewHolder.checkbox.setChecked(false);

        viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                task.setCompleted(true);
                taskCompleteListener.onTaskCompleted(task);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (tasks == null) {
            return 0;
        }
        return tasks.size();
    }

    public void setTasks(final List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ConstraintLayout rootView;
        public TextView taskTitleView;
        public CheckBox checkbox;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = (ConstraintLayout) itemView.findViewById(R.id.item_task_root_view);
            taskTitleView = (TextView) itemView.findViewById(R.id.task_title);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }

    public interface TaskCompleteListener {
        void onTaskCompleted(final TaskEntity task);
    }
}
