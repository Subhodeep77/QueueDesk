import java.time.Duration;
import java.time.LocalDateTime;

public abstract class Ticket {
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
        return nextId-1;
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
    
    public void setDescription(String description) {
        if(description == null || description.isBlank()) throw new IllegalArgumentException("description cannot be empty.");
        this.description = description;
    }

    public void setPriority(Priority priority) {
        if(priority == null) throw new IllegalArgumentException("priority can not be set null.");
        this.priority = priority;
    }

    public void setStatus(Status status) {
        if(priority == null) throw new IllegalArgumentException("status cannot be set null.");
        this.status = status;
    }

    public long ageInHours() {
        return Duration.between(this.createdAt, LocalDateTime.now()).toHours();
    }

    abstract public double estimateEffortHours();
    
}

