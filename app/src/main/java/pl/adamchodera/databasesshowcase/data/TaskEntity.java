package pl.adamchodera.databasesshowcase.data;

public final class TaskEntity {

    private final String id;

    private final String title;

    private final String description;

    private final boolean completed;

    public TaskEntity(String id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return completed;
    }
}
