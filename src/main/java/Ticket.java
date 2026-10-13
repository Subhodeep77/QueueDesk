import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public abstract class Ticket implements Comparable<Ticket> {
    private static int nextId = 1;
    private int id;
    private String title;
    private String description;
    private String requester;

    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum Status {
        OPEN, IN_PROGRESS, RESOLVED, CLOSED
    }

    private final LocalDateTime createdAt;
    private Priority priority;
    private Status status = Status.OPEN;
    private String assignee;
    private Set<String> tags = new LinkedHashSet<>();

    public Ticket(String title, String description, String requester, Priority priority) {
        this.id = nextId++;
        this.title = title;
        this.description = description;
        this.requester = requester;
        this.priority = priority;
        this.createdAt = LocalDateTime.now();
    }

    public Ticket(String title, String requester) {
        this(title, "No description provided", requester, Priority.LOW);
    }

    public static int totalCreated() {
        return nextId - 1;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRequester() {
        return requester;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Priority getPriority() {
        return priority;
    }

    public Status getStatus() {
        return status;
    }

    public String getAssignee() {
        return assignee;
    }

    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public void setDescription(String description) {
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("description cannot be empty.");
        this.description = description;
    }

    public void setPriority(Priority priority) {
        if (priority == null)
            throw new IllegalArgumentException("priority can not be set null.");
        this.priority = priority;
    }

    public void setStatus(Status status) {
        if (status == null)
            throw new IllegalArgumentException("status cannot be set null.");
        this.status = status;
    }

    public void setAssignee(String assignee) {
        if (assignee != null && assignee.isBlank())
            throw new IllegalArgumentException("assignee can not be empty.");
        this.assignee = assignee == null ? null : assignee.trim();
    }

    public void addTag(String tag) {
        if (tag == null || tag.isBlank())
            throw new IllegalArgumentException("tag can not be empty.");
        tags.add(tag.trim());
    }

    public void removeTag(String tag) {
        if (tag == null || tag.isBlank()) {
            throw new IllegalArgumentException("Tag cannot be empty.");
        }
        tags.remove(tag.trim());
    }

    public long ageInHours() {
        return Duration.between(this.createdAt, LocalDateTime.now()).toHours();
    }

    abstract public double estimateEffortHours();

    @Override
    public int compareTo(Ticket other) {
        int result = Integer.compare(
            other.getPriority().ordinal(), this.getPriority().ordinal()
        );
        if(result != 0) return result;
        result = this.getCreatedAt().compareTo(other.getCreatedAt());
        if(result != 0) return result;
        return Integer.compare(this.getId(), other.getId());
    }

}
