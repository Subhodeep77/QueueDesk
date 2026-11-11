package com.queuedesk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Set;

public class AccessRequestTicket extends Ticket implements Escalatable {
    private String systemName;

    public enum AccessLevel {
        READ_ONLY, STANDARD, ADMIN
    }

    private AccessLevel accessLevel;
    private int escalationLevel = 0;

    public AccessRequestTicket(String systemName, AccessLevel accessLevel, String title, String description,
            String requester, Priority priority) {
        super(title, description, requester, priority);
        this.systemName = systemName;
        this.accessLevel = accessLevel;
    }

    @JsonCreator
    public AccessRequestTicket(
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("requester") String requester,
            @JsonProperty("priority") Priority priority,
            @JsonProperty("status") Status status,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("assignee") String assignee,
            @JsonProperty("tags") Set<String> tags,
            @JsonProperty("systemName") String systemName,
            @JsonProperty("accessLevel") AccessLevel accessLevel,
            @JsonProperty("escalationLevel") int escalationLevel) {

        super(id, title, description, requester, priority,
                status, createdAt, assignee, tags);
                
        if (systemName == null || systemName.isBlank()) {
            throw new IllegalArgumentException("System Name cannot be empty.");
        }

        if (accessLevel == null) {
            throw new IllegalArgumentException("Access level cannot be null.");
        }

        if (escalationLevel < 0 || escalationLevel > 3) {
            throw new IllegalArgumentException("Invalid escalation level.");
        }

        this.systemName = systemName;
        this.accessLevel = accessLevel;
        this.escalationLevel = escalationLevel;
    }

    @Override
    public double estimateEffortHours() {
        switch (accessLevel) {
            case READ_ONLY:
                return 0.5;
            case STANDARD:
                return 1.0;
            case ADMIN:
                return 3.0;
            default:
                throw new IllegalArgumentException("Unknown access level.");
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

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        if (systemName == null || systemName.isBlank())
            throw new IllegalArgumentException("System Name can not be empty.");
        this.systemName = systemName.trim();
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        if (accessLevel == null)
            throw new IllegalArgumentException("access level can not be set null.");
        this.accessLevel = accessLevel;
    }

}
