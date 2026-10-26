import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TicketFileLoader {

    public List<Ticket> load(Path file)
            throws IOException, InvalidTicketDataException {

        List<Ticket> tickets = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                try {
                    if (line.isBlank()) {
                        throw new IllegalArgumentException("Empty record");
                    }

                    String[] fields = line.split("\\|", -1);

                    for (int i = 0; i < fields.length; i++) {
                        fields[i] = fields[i].trim();
                    }

                    if (fields.length < 1) {
                        throw new IllegalArgumentException("Empty record");
                    }

                    String type = fields[0];

                    Ticket ticket;

                    switch (type) {
                        case "BUG" -> {
                            if (fields.length != 10) {
                                throw new IllegalArgumentException(
                                        "BUG requires 10 fields");
                            }

                            ticket = new BugTicket(
                                    BugTicket.Severity.valueOf(fields[8]),
                                    fields[9],
                                    fields[1],
                                    fields[2],
                                    fields[3],
                                    Ticket.Priority.valueOf(fields[4]));
                        }

                        case "FEATURE" -> {
                            if (fields.length != 10) {
                                throw new IllegalArgumentException(
                                        "FEATURE requires 10 fields");
                            }

                            ticket = new FeatureRequestTicket(
                                    Integer.parseInt(fields[8]),
                                    fields[9],
                                    fields[1],
                                    fields[2],
                                    fields[3],
                                    Ticket.Priority.valueOf(fields[4]));
                        }

                        case "ACCESS" -> {
                            if (fields.length != 10) {
                                throw new IllegalArgumentException(
                                        "ACCESS requires 10 fields");
                            }

                            ticket = new AccessRequestTicket(
                                    fields[8],
                                    AccessRequestTicket.AccessLevel.valueOf(fields[9]),
                                    fields[1],
                                    fields[2],
                                    fields[3],
                                    Ticket.Priority.valueOf(fields[4]));
                        }

                        default -> throw new IllegalArgumentException(
                                "Unknown ticket type: " + type);
                    }

                    ticket.setStatus(Ticket.Status.valueOf(fields[5]));

                    if (!fields[6].isEmpty()) {
                        ticket.setAssignee(fields[6]);
                    }

                    if (!fields[7].isEmpty()) {
                        for (String tag : fields[7].split(",")) {
                            ticket.addTag(tag.trim());
                        }
                    }

                    tickets.add(ticket);

                } catch (IllegalArgumentException e) {
                    throw new InvalidTicketDataException(
                            "Invalid ticket data at line " + lineNumber
                                    + ": " + e.getMessage());
                }
            }
        }

        return Collections.unmodifiableList(tickets);
    }
}