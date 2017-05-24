package pl.adamchodera.databasesshowcase;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import pl.adamchodera.databasesshowcase.data.TaskEntity;

public class TasksListAdapter extends RecyclerView.Adapter<TasksListAdapter.ViewHolder> {

    private List<TaskEntity> tasks;

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

        public LinearLayout rootView;
        public TextView taskTitleView;

        public ViewHolder(View itemView) {
            super(itemView);

            rootView = (LinearLayout) itemView.findViewById(R.id.item_task_root_view);
            taskTitleView = (TextView) itemView.findViewById(R.id.task_title);
        }
    }
}
