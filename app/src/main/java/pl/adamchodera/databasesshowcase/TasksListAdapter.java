package pl.adamchodera.databasesshowcase;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.adamchodera.databasesshowcase.data.TaskEntity;

public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.ViewHolder> {

    private List<TaskEntity> tasks;

    @Override
    public TasksListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_task, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TasksListAdapter.ViewHolder viewHolder, int position) {
        TaskEntity task = tasks.get(position);

        TextView textView = viewHolder.taskTitleView;
        textView.setText(task.getTitle() + task.getId());
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void setTasks(final List<TaskEntity> tasks) {
        this.tasks = tasks;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView taskTitleView;

        public ViewHolder(View itemView) {
            super(itemView);

            taskTitleView = (TextView) itemView.findViewById(R.id.task_title);
        }
    }
}
