import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public final class TicketReportGenerator {
    private TicketReportGenerator() {
    };

    public static void generateReport(List<Ticket> tickets, Path outputFile) throws IOException {
        if (tickets == null) {
            throw new IllegalArgumentException("Tickets list cannot be null.");
        }
        if (outputFile == null) {
            throw new IllegalArgumentException("Output file cannot be null.");
        }

        Map<Ticket.Status, Integer> statusMap = new EnumMap<>(Ticket.Status.class);
        Map<Ticket.Priority, Integer> priorityMap = new EnumMap<>(Ticket.Priority.class);
        for (Ticket.Status status : Ticket.Status.values()) {
            statusMap.put(status, 0);
        }
        for (Ticket t : tickets) {
            statusMap.put(t.getStatus(), statusMap.get(t.getStatus()) + 1);
        }
        for (Ticket.Priority priority : Ticket.Priority.values()) {
            priorityMap.put(priority, 0);
        }
        for (Ticket t : tickets) {
            priorityMap.put(t.getPriority(), priorityMap.get(t.getPriority()) + 1);
        }
        List<Ticket> openTicket = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.getStatus() == Ticket.Status.OPEN) {
                openTicket.add(t);
            }
        }
        Ticket oldestOpenTicket = Repository.oldest(openTicket);

        List<Ticket> overdueCriticalTickets = new ArrayList<>();

        for (Ticket t : tickets) {
            if ((t.getStatus() == Ticket.Status.OPEN || t.getStatus() == Ticket.Status.IN_PROGRESS)
                    && t.getPriority() == Ticket.Priority.CRITICAL && t.ageInHours() >= 3) {
                overdueCriticalTickets.add(t);
            }
        }

        StringBuilder report = new StringBuilder();

        report.append("===== QueueDesk End-of-Day Report =====\n\n");

        report.append("Tickets by Status:\n");
        for (Ticket.Status status : Ticket.Status.values()) {
            report.append(status)
                    .append(": ")
                    .append(statusMap.get(status))
                    .append("\n");
        }

        report.append("\nTickets by Priority:\n");
        for (Ticket.Priority priority : Ticket.Priority.values()) {
            report.append(priority)
                    .append(": ")
                    .append(priorityMap.get(priority))
                    .append("\n");
        }

        report.append("\nOldest Open Ticket:\n");
        if (oldestOpenTicket != null) {
            report.append(oldestOpenTicket.getTitle())
                    .append(" (ID: ")
                    .append(oldestOpenTicket.getId())
                    .append(", Age: ")
                    .append(oldestOpenTicket.ageInHours())
                    .append(" hours)\n");
        } else {
            report.append("No open ticket available\n");
        }

        report.append("\nOverdue Critical Tickets:\n");

        if (overdueCriticalTickets.isEmpty()) {
            report.append("No overdue critical tickets present");
        } else {
            for (Ticket t : overdueCriticalTickets) {
                report.append("ID: ")
                        .append(t.getId())
                        .append(", Title: ")
                        .append(t.getTitle())
                        .append(", Age: ")
                        .append(t.ageInHours())
                        .append(" hours\n");
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(outputFile)) {
            writer.write(report.toString());
        }

    }
}
