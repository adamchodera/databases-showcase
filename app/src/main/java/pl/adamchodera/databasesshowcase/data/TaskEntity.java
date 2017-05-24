package pl.adamchodera.databasesshowcase.data;

public final class TaskEntity {

    private static final int DEFAULT_ID_FOR_TASK_NOT_SAVED_IN_DB = -1;
    private long id = DEFAULT_ID_FOR_TASK_NOT_SAVED_IN_DB;
    private String title;
    private String description;
    private boolean completed;

    public TaskEntity(long id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public TaskEntity() {
        this(-1, "", "", false);
    }

    public String getStringId() {
        return String.valueOf(id);
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(final boolean completed) {
        this.completed = completed;
    }

    public boolean isTaskInDatabase() {
        // for task which wasn't inserted into database we set default value as -1
        if (id == DEFAULT_ID_FOR_TASK_NOT_SAVED_IN_DB) {
            return false;
        } else {
            return true;
        }
    }
}
