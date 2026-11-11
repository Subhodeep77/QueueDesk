package com.queuedesk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BugTicket.class, name = "bug"),
        @JsonSubTypes.Type(value = FeatureRequestTicket.class, name = "feature"),
        @JsonSubTypes.Type(value = AccessRequestTicket.class, name = "access")
})

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

    protected Ticket(
            int id,
            String title,
            String description,
            String requester,
            Priority priority,
            Status status,
            LocalDateTime createdAt,
            String assignee,
            Set<String> tags) {

        if (id <= 0)
            throw new IllegalArgumentException("Invalid ticket ID.");

        if (priority == null || status == null || createdAt == null) {
            throw new IllegalArgumentException("Invalid ticket state.");
        }

        this.id = id;
        this.title = title;
        this.description = description;
        this.requester = requester;
        this.priority = priority;
        this.status = status;
        this.createdAt = createdAt;
        this.assignee = assignee;

        this.tags = tags == null
                ? new LinkedHashSet<>()
                : new LinkedHashSet<>(tags);

        nextId = Math.max(nextId, id + 1);
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

    @JsonIgnore
    public String getShortDescription() {
        int maxLength = 50;

        if (description.length() <= maxLength) {
            return description;
        }

        return description.substring(0, maxLength) + "...";
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
        this.description = description.trim();
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
                other.getPriority().ordinal(), this.getPriority().ordinal());
        if (result != 0)
            return result;
        result = this.getCreatedAt().compareTo(other.getCreatedAt());
        if (result != 0)
            return result;
        return Integer.compare(this.getId(), other.getId());
    }

}
