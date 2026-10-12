
import java.util.List;

public class TriageBoard {

    public static void printTriageBoard(List<Ticket> tickets) {
        for (Ticket t : tickets) {
            System.out.println("Ticket Id: " + t.getId());
            System.out.println("Ticket Title: " + t.getTitle());
            System.out.println("Ticket Status: " + t.getStatus());
            System.out.println("Ticket Age: " + t.ageInHours());
            System.out.println("Ticket Estimated Effort: "
                    + t.estimateEffortHours() + " hours");

            if (t instanceof Escalatable) {
                Escalatable e = (Escalatable) t;

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
