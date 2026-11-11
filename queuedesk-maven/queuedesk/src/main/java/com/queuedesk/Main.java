
package com.queuedesk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        // ==========================================
        // APPLICATION INITIALIZATION
        // ==========================================

        List<Ticket> tickets;

        try {
            tickets = loadOrMigrateTickets();

        } catch (IOException | InvalidTicketDataException e) {

            System.out.println(
                    "Unable to initialize tickets: " + e.getMessage()
            );

            return;
        }

        // ==========================================
        // TRIAGE BOARD
        // ==========================================

        System.out.println("\n========== TRIAGE BOARD ==========");

        TriageBoard.printTriageBoard(tickets);


        // ==========================================
        // TICKET ANALYTICS
        // ==========================================

        System.out.println("\n========== TICKET ANALYTICS ==========");

        // 1. Count tickets by priority

        Map<Ticket.Priority, Long> priorityCount =
                TicketAnalytics.countByPriority(tickets);

        System.out.println("\nTickets by priority:");

        priorityCount.forEach((priority, count) ->
                System.out.println(priority + " : " + count)
        );


        // 2. Group tickets by assignee

        Map<String, List<Ticket>> assigneeGroups =
                TicketAnalytics.groupByAssignee(tickets);

        System.out.println("\nTickets grouped by assignee:");

        assigneeGroups.forEach((assignee, assignedTickets) -> {

            System.out.println("\nAssignee: " + assignee);

            assignedTickets.forEach(ticket ->
                    System.out.println("  " + ticket.getTitle())
            );
        });


        // 3. Average effort hours

        double averageEffort =
                TicketAnalytics.averageEffortHours(tickets);

        System.out.println("\nAverage effort hours: " + averageEffort);


        // 4. Total effort hours

        double totalEffort =
                TicketAnalytics.totalEffortHours(tickets);

        System.out.println("Total effort hours: " + totalEffort);


        // 5. Oldest open ticket

        Optional<Ticket> oldestOpen =
                TicketAnalytics.oldestOpenTicket(tickets);

        System.out.println("\nOldest open ticket:");

        oldestOpen.ifPresentOrElse(
                ticket -> System.out.println(ticket.getTitle()),
                () -> System.out.println("No open tickets found.")
        );


        // 6. Combined unique tag line

        String tagLine = TicketAnalytics.tagLine(tickets);

        System.out.println("\nAll unique tags:");
        System.out.println(tagLine);


        // ==========================================
        // REPOSITORY TESTING
        // ==========================================

        System.out.println("\n========== REPOSITORY TESTING ==========");

        Repository<Ticket> repository = new Repository<>();

        for (Ticket ticket : tickets) {
            repository.add(ticket);
        }


        // 7. findByIdSafe() with map()

        System.out.println("\nTesting findByIdSafe() with map():");

        if (!tickets.isEmpty()) {

            int ticketId = tickets.get(0).getId();

            String ticketTitle = repository.findByIdSafe(ticketId)
                    .map(Ticket::getTitle)
                    .orElse("Ticket not found");

            System.out.println("Ticket title: " + ticketTitle);
        }


        // 8. findByIdSafe() with ifPresentOrElse()

        System.out.println(
                "\nTesting findByIdSafe() with ifPresentOrElse():"
        );

        if (tickets.size() > 1) {

            int ticketId = tickets.get(1).getId();

            repository.findByIdSafe(ticketId)
                    .ifPresentOrElse(
                            ticket -> System.out.println(
                                    "Found ticket: " + ticket.getTitle()
                            ),
                            () -> System.out.println("Ticket not found.")
                    );
        }


        // 9. findByIdSafe() with orElseThrow()

        System.out.println(
                "\nTesting findByIdSafe() with orElseThrow():"
        );

        if (tickets.size() > 2) {

            int ticketId = tickets.get(2).getId();

            try {

                Ticket ticket = repository.findByIdSafe(ticketId)
                        .orElseThrow(() ->
                                new TicketNotFoundException(
                                        "Ticket not found having id: "
                                                + ticketId + "."
                                )
                        );

                System.out.println("Found ticket: " + ticket.getTitle());

            } catch (TicketNotFoundException e) {

                System.out.println(e.getMessage());
            }
        }


        // 10. Testing missing ticket using Optional.empty()

        System.out.println("\nTesting missing ticket:");

        repository.findByIdSafe(999)
                .ifPresentOrElse(
                        ticket -> System.out.println(ticket.getTitle()),
                        () -> System.out.println(
                                "Ticket with ID 999 not found."
                        )
                );


        // ==========================================
        // JSON PERSISTENCE AND REPORT GENERATION
        // ==========================================

        System.out.println("\n========== JSON PERSISTENCE ==========");

        TicketJsonStore jsonStore = new TicketJsonStore();

        try {

            // Save current ticket state to JSON

            jsonStore.save(tickets, Path.of("tickets.json"));

            System.out.println(
                    "Tickets saved to JSON successfully!"
            );


            // Generate ticket report

            TicketReportGenerator.generateReport(
                    tickets,
                    Path.of("report.txt")
            );

            System.out.println(
                    "Ticket report generated successfully!"
            );

        } catch (InvalidTicketDataException e) {

            System.out.println(
                    "JSON persistence error: " + e.getMessage()
            );

        } catch (IOException e) {

            System.out.println(
                    "Report generation error: " + e.getMessage()
            );
        }
    }


    // ==========================================
    // LOAD OR MIGRATE TICKETS
    // ==========================================

    private static List<Ticket> loadOrMigrateTickets()
            throws IOException, InvalidTicketDataException {

        Path jsonFile = Path.of("tickets.json");
        Path legacyFile = Path.of("tickets.txt");

        TicketJsonStore jsonStore = new TicketJsonStore();


        // Case 1: JSON file already exists

        if (Files.exists(jsonFile)) {

            System.out.println("Loading tickets from JSON...");

            return jsonStore.load(jsonFile);
        }


        // Case 2: Legacy file exists, migrate it to JSON

        if (Files.exists(legacyFile)) {

            System.out.println(
                    "Migrating legacy tickets to JSON..."
            );

            TicketFileLoader loader = new TicketFileLoader();

            List<Ticket> tickets = loader.load(legacyFile);

            jsonStore.save(tickets, jsonFile);

            System.out.println(
                    "Migration completed successfully!"
            );

            return tickets;
        }


        // Case 3: Neither file exists

        System.out.println(
                "No existing ticket data found. Creating sample tickets..."
        );

        List<Ticket> tickets = createSampleTickets();

        jsonStore.save(tickets, jsonFile);

        System.out.println(
                "Initial tickets saved to JSON successfully!"
        );

        return tickets;
    }


    // ==========================================
    // CREATE INITIAL SAMPLE TICKETS
    // ==========================================

    private static List<Ticket> createSampleTickets() {

        List<Ticket> tickets = new ArrayList<>();

        BugTicket bug = new BugTicket(
                BugTicket.Severity.CRITICAL,
                "Login fails with valid credentials",
                "Login failure",
                "Users cannot log in",
                "Subho",
                Ticket.Priority.HIGH
        );

        AccessRequestTicket access = new AccessRequestTicket(
                "Production Dashboard",
                AccessRequestTicket.AccessLevel.ADMIN,
                "Admin access",
                "Requesting administrator access",
                "Subho",
                Ticket.Priority.MEDIUM
        );

        FeatureRequestTicket feature = new FeatureRequestTicket(
                25,
                "Improve user productivity",
                "Dashboard filters",
                "Add date-based filtering",
                "Subho",
                Ticket.Priority.LOW
        );

        bug.escalate();

        tickets.add(bug);
        tickets.add(access);
        tickets.add(feature);

        return tickets;
    }
}
