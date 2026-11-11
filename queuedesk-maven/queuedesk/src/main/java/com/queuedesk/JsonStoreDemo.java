
package com.queuedesk;

import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class JsonStoreDemo {

    public static void main(String[] args)
            throws InvalidTicketDataException {

        // 1. Create three different ticket objects

        BugTicket bug = new BugTicket(
                BugTicket.Severity.HIGH,
                "Application crashes on login",
                "Login Crash",
                "Application crashes when user logs in",
                "Subho",
                Ticket.Priority.CRITICAL
        );

        FeatureRequestTicket feature = new FeatureRequestTicket(
                25,
                "Add dark mode support",
                "Dark Mode",
                "Add dark mode to the application",
                "Rahul",
                Ticket.Priority.MEDIUM
        );

        AccessRequestTicket access = new AccessRequestTicket(
                "Production Database",
                AccessRequestTicket.AccessLevel.READ_ONLY,
                "Database Access",
                "Need read access to production database",
                "Amit",
                Ticket.Priority.HIGH
        );

        // 2. Set additional inherited fields

        bug.setStatus(Ticket.Status.IN_PROGRESS);
        bug.setAssignee("Developer A");
        bug.addTag("production");
        bug.addTag("critical");

        feature.setStatus(Ticket.Status.OPEN);
        feature.setAssignee("Developer B");
        feature.addTag("enhancement");

        access.setStatus(Ticket.Status.RESOLVED);
        access.setAssignee("Developer C");
        access.addTag("database");

        // 3. Store them in a List<Ticket>

        List<Ticket> tickets = List.of(bug, feature, access);

        // 4. Create TicketJsonStore and save

        TicketJsonStore store = new TicketJsonStore();

        Path file = Path.of("tickets-test.json");

        store.save(tickets, file);

        System.out.println("Tickets saved successfully!");

        // 5. Load the tickets back

        List<Ticket> loadedTickets = store.load(file);

        System.out.println("Tickets loaded successfully!");

        // 6. Verify common fields

        for (int i = 0; i < tickets.size(); i++) {

            Ticket original = tickets.get(i);
            Ticket loaded = loadedTickets.get(i);

            check(original.getId() == loaded.getId(), "ID");
            check(Objects.equals(original.getTitle(), loaded.getTitle()), "Title");
            check(Objects.equals(original.getDescription(), loaded.getDescription()), "Description");
            check(Objects.equals(original.getRequester(), loaded.getRequester()), "Requester");
            check(original.getPriority() == loaded.getPriority(), "Priority");
            check(original.getStatus() == loaded.getStatus(), "Status");
            check(Objects.equals(original.getCreatedAt(), loaded.getCreatedAt()), "CreatedAt");
            check(Objects.equals(original.getAssignee(), loaded.getAssignee()), "Assignee");
            check(Objects.equals(original.getTags(), loaded.getTags()), "Tags");

            // Verify concrete runtime type

            check(original.getClass() == loaded.getClass(), "Runtime type");

            System.out.println(
                    "\nID: " + loaded.getId()
                    + " | Type: " + loaded.getClass().getSimpleName()
                    + " | Title: " + loaded.getTitle()
            );
        }

        // 7. Verify subclass-specific fields

        BugTicket loadedBug = (BugTicket) loadedTickets.get(0);

        check(
                bug.getSeverity() == loadedBug.getSeverity(),
                "Bug severity"
        );

        check(
                Objects.equals(
                        bug.getStepsToReproduce(),
                        loadedBug.getStepsToReproduce()
                ),
                "Steps to reproduce"
        );

        FeatureRequestTicket loadedFeature =
                (FeatureRequestTicket) loadedTickets.get(1);

        check(
                feature.getVotes() == loadedFeature.getVotes(),
                "Feature votes"
        );

        check(
                Objects.equals(
                        feature.getBusinessJustification(),
                        loadedFeature.getBusinessJustification()
                ),
                "Business justification"
        );

        AccessRequestTicket loadedAccess =
                (AccessRequestTicket) loadedTickets.get(2);

        check(
                Objects.equals(
                        access.getSystemName(),
                        loadedAccess.getSystemName()
                ),
                "System name"
        );

        check(
                access.getAccessLevel() == loadedAccess.getAccessLevel(),
                "Access level"
        );

        System.out.println("\nAll JSON round-trip checks passed!");
    }

    private static void check(boolean condition, String field) {

        if (!condition) {
            throw new AssertionError(
                    "JSON round-trip verification failed for: " + field
            );
        }
    }
}
