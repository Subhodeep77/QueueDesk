package com.queuedesk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Set;

public class BugTicket extends Ticket implements Escalatable {
    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    private Severity severity;
    private String stepsToReproduce;
    private int escalationLevel = 0;

    public BugTicket(Severity severity, String stepsToReproduce, String title, String description, String requester,
            Priority priority) {
        super(title, description, requester, priority);
        this.severity = severity;
        this.stepsToReproduce = stepsToReproduce;
    }

    @JsonCreator
    public BugTicket(
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("requester") String requester,
            @JsonProperty("priority") Priority priority,
            @JsonProperty("status") Status status,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("assignee") String assignee,
            @JsonProperty("tags") Set<String> tags,
            @JsonProperty("severity") Severity severity,
            @JsonProperty("stepsToReproduce") String stepsToReproduce,
            @JsonProperty("escalationLevel") int escalationLevel) {

        super(id, title, description, requester, priority,
                status, createdAt, assignee, tags);
        if (severity == null) {
            throw new IllegalArgumentException("Severity cannot be null.");
        }

        if (stepsToReproduce == null || stepsToReproduce.isBlank()) {
            throw new IllegalArgumentException("Steps to reproduce cannot be empty.");
        }

        if (escalationLevel < 0 || escalationLevel > 3) {
            throw new IllegalArgumentException("Invalid escalation level.");
        }
        this.severity = severity;
        this.stepsToReproduce = stepsToReproduce;
        this.escalationLevel = escalationLevel;
    }

    @Override
    public double estimateEffortHours() {
        switch (severity) {
            case LOW:
                return 1.0;
            case MEDIUM:
                return 3.0;
            case HIGH:
                return 6.0;
            case CRITICAL:
                return 10.0;
            default:
                throw new IllegalStateException("Unknown severity");
        }
    }

    @Override
    public void escalate() {
        if (escalationLevel < 3)
            escalationLevel++;
    }

    @JsonIgnore
    @Override
    public boolean isEscalated() {
        return escalationLevel > 0;
    }

    @JsonGetter("escalationLevel")
    @Override
    public int escalationLevel() {
        return escalationLevel;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getStepsToReproduce() {
        return stepsToReproduce;
    }

    public void setSeverity(Severity severity) {
        if (severity == null)
            throw new IllegalArgumentException("severity can not be set null.");
        this.severity = severity;
    }

    public void setStepsToReproduce(String stepsToReproduce) {
        if (stepsToReproduce == null || stepsToReproduce.isBlank())
            throw new IllegalArgumentException("steps to reproduce can not be empty.");
        this.stepsToReproduce = stepsToReproduce.trim();
    }
}