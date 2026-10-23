import java.util.PriorityQueue;

public class TriageQueues {
    private final PriorityQueue<Ticket> naturalQueue = new PriorityQueue<>();
    private final PriorityQueue<Ticket> customQueue = new PriorityQueue<>(TicketComparators.BY_ASSIGNEE_THEN_PRIORITY);

    public boolean offerNatural(Ticket t) {
        return naturalQueue.offer(t);
    }

    public boolean offerCustom(Ticket t) {
        return customQueue.offer(t);
    }

    public Ticket pollNatural() {
        return naturalQueue.poll();
    }

    public Ticket pollCustom() {
        return customQueue.poll();
    }
}
