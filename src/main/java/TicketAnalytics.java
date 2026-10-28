import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class TicketAnalytics {
    private TicketAnalytics() {
    }

    public static Map<Ticket.Priority, Long> countByPriority(List<Ticket> tickets) {
        if (tickets == null) {
            throw new IllegalArgumentException("Tickets list cannot be null.");
        }

        return tickets.stream()
                .collect(Collectors.groupingBy(
                        Ticket::getPriority,
                        Collectors.counting()));
    }

    public static Map<String,List<Ticket>> groupByAssignee(List<Ticket> tickets) {
        if(tickets == null) {
            throw new IllegalArgumentException("Ticket list cannot be null.");
        } 
        return tickets.stream()
                .collect(Collectors.groupingBy(ticket -> ticket.getAssignee() == null ? "Unassigned" : ticket.getAssignee()));
    }

    public static Double averageEffortHours(List<Ticket> tickets) {
        if(tickets == null) throw new IllegalArgumentException("Ticket list cannot be null.");
        return tickets.stream()
                .mapToDouble(Ticket::estimateEffortHours)
            .average()
            .orElse(0.0);
    }

}
