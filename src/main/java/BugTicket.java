public class BugTicket extends Ticket implements Escalatable {
    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    private Severity severity;
    private String stepsToReproduce;
    private int escalationLevel = 0;
    
    public BugTicket(Severity severity, String stepsToReproduce, String title, String description, String requester, Priority priority) {
        super(title,description,requester,priority);
        this.severity = severity;
        this.stepsToReproduce = stepsToReproduce;
    }
    @Override 
    public double estimateEffortHours() {
        switch (severity) {
            case LOW:
                return 1.0;
            case MEDIUM:
                return 3.0;
            case HIGH:
                return 6.0;
            case CRITICAL:
                return 10.0;
            default:
                throw new IllegalStateException("Unknown severity");
        }
    }
    @Override 
    public void escalate() {
        if(escalationLevel < 3) escalationLevel++;
    }
    @Override 
    public boolean isEscalated() {
        return escalationLevel > 0;
    }
    @Override 
    public int escalationLevel() {
        return escalationLevel;
    }
    public Severity getSeverity() {
        return severity;
    }
    public String getStepsToReproduce() {
        return stepsToReproduce;
    }
    public void setSeverity(Severity severity) {
        if(severity == null) throw new IllegalArgumentException("severity can not be set null.");
        this.severity = severity;
    }
    public void setStepsToReproduce(String stepsToReproduce) {
        if(stepsToReproduce == null || stepsToReproduce.isBlank()) throw new IllegalArgumentException("steps to reproduce can not be empty.");
    }
}