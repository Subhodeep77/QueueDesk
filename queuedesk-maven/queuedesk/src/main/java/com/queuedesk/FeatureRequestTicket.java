package com.queuedesk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Set;

public class FeatureRequestTicket extends Ticket {
    private int votes;
    private String businessJustification;

    public FeatureRequestTicket(int votes, String businessJustification, String title, String description,
            String requester, Priority priority) {
        super(title, description, requester, priority);
        if (votes < 0)
            throw new IllegalArgumentException("Votes can not be negative.");
        else
            this.votes = votes;
        this.businessJustification = businessJustification;
    }

    @JsonCreator
    public FeatureRequestTicket(
            @JsonProperty("id") int id,
            @JsonProperty("title") String title,
            @JsonProperty("description") String description,
            @JsonProperty("requester") String requester,
            @JsonProperty("priority") Priority priority,
            @JsonProperty("status") Status status,
            @JsonProperty("createdAt") LocalDateTime createdAt,
            @JsonProperty("assignee") String assignee,
            @JsonProperty("tags") Set<String> tags,
            @JsonProperty("votes") int votes,
            @JsonProperty("businessJustification") String businessJustification) {

        super(id, title, description, requester, priority,
                status, createdAt, assignee, tags);

        if (votes < 0) {
            throw new IllegalArgumentException("Votes cannot be negative.");
        }

        this.votes = votes;
        this.businessJustification = businessJustification;
    }

    @Override
    public double estimateEffortHours() {
        return 4.0 + (votes / 10);
    }

    public int getVotes() {
        if (votes < 0)
            throw new IllegalArgumentException("Votes cannot be negative.");
        return votes;
    }

    public void setVotes(int votes) {
        if (votes < 0)
            throw new IllegalArgumentException("Votes cannot be negative.");
        this.votes = votes;
    }

    public String getBusinessJustification() {
        return businessJustification;
    }

    public void setBusinessJustification(String businessJustification) {
        if (businessJustification == null || businessJustification.isBlank())
            throw new IllegalArgumentException("business justification can not be empty");
        this.businessJustification = businessJustification.trim();
    }

}