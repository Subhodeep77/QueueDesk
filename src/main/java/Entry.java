import java.util.Scanner;

public class Entry {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int ticketNumber = sc.nextInt();
        int numberOfCustomers = sc.nextInt();

        int servedCustomers = 0;
        int totalVipTickets = 0;
        int totalPriorityTickets = 0;

        for (int i = 1; i <= numberOfCustomers; i++) {
            int currTicketNumber = ticketNumber + i - 1;
            servedCustomers++;
            if (i % 10 == 0) {
                totalPriorityTickets++;
                System.out.println("Now serving ticket #" + currTicketNumber + " PriorityRecheck");
            } else if (i % 5 == 0) {
                totalVipTickets++;
                System.out.println("Now serving ticket #" + currTicketNumber + " VipLane");
            }
            else System.out.println("Now serving ticket #" + currTicketNumber);
        }

        System.out.println("Total tickets: " + servedCustomers);
        System.out.println("VIP tickets: " + totalVipTickets);
        System.out.println("Priority tickets: " + totalPriorityTickets);

        sc.close();

        // Ticket t1 = new Ticket("Application crashes during login","App closes after valid login", "Rahul Sharma", Ticket.Priority.CRITICAL);
        // System.out.println("Ticket Id: " + t1.getId());
        // System.out.println("Ticket Priority: " + t1.getPriority());
        // System.out.println("Ticket Status: " + t1.getStatus());
        // System.out.println("Ticket Description: " + t1.getDescription());
        // System.out.println("EffortHours: " + t1.ageInHours());
        // Ticket t2 = new Ticket("Night Theme Feature","Subhodeep Paramanik");
        // System.out.println("Ticket Id: " + t2.getId());
        // System.out.println("Ticket Priority: " + t2.getPriority());
        // System.out.println("Ticket Status: " + t2.getStatus());
        // System.out.println("Ticket Description: " + t2.getDescription());
        // System.out.println("EffortHours: " + t2.ageInHours());
        // System.out.println("Total Tickets Created: " + Ticket.totalCreated());
    }
}