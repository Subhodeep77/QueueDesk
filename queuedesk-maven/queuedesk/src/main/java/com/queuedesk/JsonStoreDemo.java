
package com.queuedesk;

import java.nio.file.Path;
import java.util.List;

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

        // 2. Store them in a List<Ticket>

        List<Ticket> tickets = List.of(bug, feature, access);

        // 3. Create TicketJsonStore

        TicketJsonStore store = new TicketJsonStore();

        // 4. Save the tickets to tickets-test.json

        Path file = Path.of("tickets-test.json");

        store.save(tickets, file);

        System.out.println("Tickets saved successfully!");

        // 5. Load the tickets back

        List<Ticket> loadedTickets = store.load(file);

        System.out.println("\nTickets loaded successfully!");

        // 6. Print their IDs and runtime class names

        for (Ticket ticket : loadedTickets) {

            System.out.println(
                    "ID: " + ticket.getId()
                    + " | Type: " + ticket.getClass().getSimpleName()
                    + " | Title: " + ticket.getTitle()
            );
        }
    }
}
