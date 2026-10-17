import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repository<T extends Ticket> {
    private final List<T> tickets = new ArrayList<>();

    public void add(T ticket) {
        if (ticket != null)
            tickets.add(ticket);
        else
            throw new IllegalArgumentException("ticket can not be null.");
    }

    public List<T> all() {
        return Collections.unmodifiableList(tickets);
    }

    public T findById(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("invalid id.");
        for (T t : tickets) {
            if (t.getId() == id)
                return t;
        }
        throw new TicketNotFoundException("Ticket not found.");
    }

    public Map<String, List<T>> groupByAssignee() {
        Map<String, List<T>> mappedTicket = new HashMap<>();
        for (T t : tickets) {
            mappedTicket.computeIfAbsent(t.getAssignee(), key -> new ArrayList<>()).add(t);
        }
        for (Map.Entry<String, List<T>> entry : mappedTicket.entrySet()) {
            entry.setValue(Collections.unmodifiableList(entry.getValue()));
        }
        return Collections.unmodifiableMap(mappedTicket);
    }
}
