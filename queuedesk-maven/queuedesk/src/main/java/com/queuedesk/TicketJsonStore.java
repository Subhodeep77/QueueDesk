package com.queuedesk;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TicketJsonStore {

    private final ObjectMapper mapper;

    public TicketJsonStore() {
        this.mapper = JacksonConfig.getMapper();
    }

    public void save(List<Ticket> tickets, Path file)
            throws InvalidTicketDataException {

        try {
            mapper.writerFor(new TypeReference<List<Ticket>>() {
            })
                    .writeValue(file.toFile(), tickets);

        } catch (IOException e) {
            throw new InvalidTicketDataException(
                    "Failed to save tickets to JSON.", e);
        }
    }

    public List<Ticket> load(Path file) throws InvalidTicketDataException {
        try {
            return mapper.readValue(
                    file.toFile(),
                    new TypeReference<List<Ticket>>() {
                    });
        } catch (IOException e) {
            throw new InvalidTicketDataException(
                    "Failed to load tickets from JSON.", e);
        }
    }

}