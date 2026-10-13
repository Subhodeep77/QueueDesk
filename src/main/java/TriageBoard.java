
import java.util.List;

public class TriageBoard {

    public static void printTriageBoard(List<Ticket> tickets) {
        for (Ticket t : tickets) {
            System.out.println("Ticket Estimated Effort: "
                    + t.estimateEffortHours() + " hours");
            System.out.println("Ticket: " + t);
            if (t instanceof Escalatable e) {
                System.out.println("Escalated: " + e.isEscalated());
                System.out.println("Escalation Level: "
                        + e.escalationLevel());
                System.out.println("Escalation Badge: "
                        + e.escalationBadge());
            }

            System.out.println("------------------------------");
        }
    }
}
