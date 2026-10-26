import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {
        public static void main(String[] args)  {

                List<Ticket> tickets = new ArrayList<>();

                BugTicket bug = new BugTicket(
                                BugTicket.Severity.CRITICAL,
                                "Login fails with valid credentials",
                                "Login failure",
                                "Users cannot log in",
                                "Subho",
                                Ticket.Priority.HIGH);

                AccessRequestTicket access = new AccessRequestTicket(
                                "Production Dashboard",
                                AccessRequestTicket.AccessLevel.ADMIN,
                                "Admin access",
                                "Requesting administrator access",
                                "Subho",
                                Ticket.Priority.MEDIUM);

                FeatureRequestTicket feature = new FeatureRequestTicket(
                                25,
                                "Improve user productivity",
                                "Dashboard filters",
                                "Add date-based filtering",
                                "Subho",
                                Ticket.Priority.LOW);
                bug.escalate();
                tickets.add(bug);
                tickets.add(access);
                tickets.add(feature);

                TriageBoard.printTriageBoard(tickets);
                TicketFileLoader loader = new TicketFileLoader();

                try {
                        List<Ticket> collTickets = loader.load(Path.of("tickets.txt"));

                        for (Ticket t : collTickets) {
                                System.out.println(t);
                        }

                } catch (IOException e) {
                        System.out.println("File reading error: " + e.getMessage());

                } catch (InvalidTicketDataException e) {
                        System.out.println("Invalid ticket file: " + e.getMessage());
                }
        }
}