public class FeatureRequestTicket extends Ticket {
    private int votes;
    private String businessJustification;

    public FeatureRequestTicket(int votes, String businessJustification, String title, String description, String requester, Priority priority) {
        super(title,description,requester,priority);
        if(votes < 0) throw new IllegalArgumentException("Votes can not be negative.");
        else this.votes = votes;
        this.businessJustification = businessJustification;
    }

    @Override
    public double estimateEffortHours() {
        return 4.0 + (votes/10);
    }

    public int getVotes() {
        if(votes < 0) throw new IllegalArgumentException("Votes cannot be negative.");
        return votes;
    }

    public void setVotes(int votes) {
        if(votes < 0) throw new IllegalArgumentException("Votes cannot be negative.");
        this.votes = votes;
    }

    public String getBusinessJustification() {
        return businessJustification;
    }

    public void setBusinessJustification(String businessJustification) {
        if(businessJustification == null || businessJustification.isBlank()) throw new IllegalArgumentException("business justification can not be empty");
        this.businessJustification = businessJustification.trim();
    }
    
}