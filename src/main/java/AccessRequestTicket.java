public class AccessRequestTicket extends Ticket implements Escalatable {
    private String systemName;
    public enum AccessLevel {
        READ_ONLY, STANDARD, ADMIN
    }
    private AccessLevel accessLevel;
    private int escalationLevel = 0;
    public AccessRequestTicket(String systemName, AccessLevel accessLevel, String title, String description, String requester, Priority priority) {
        super(title,description,requester,priority);
        this.systemName =systemName;
        this.accessLevel = accessLevel;
    }
    @Override
    public double estimateEffortHours() {
        switch (accessLevel) {
            case READ_ONLY:
                return 0.5;
            case STANDARD:
                return 1.0;
            case ADMIN:
                return 3.0;
            default:
                throw new IllegalArgumentException("Unknown access level.");
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
    public String getSystemName() {
        return systemName;
    }
    public void setSystemName(String systemName) {
        if(systemName == null || systemName.isBlank()) throw new IllegalArgumentException("System Name can not be empty.");
        this.systemName = systemName;
    }
    public AccessLevel getAccessLevel() {
        return accessLevel;
    }
    public void setAccessLevel(AccessLevel accessLevel) {
        if(accessLevel == null) throw new IllegalArgumentException("access level can not be set null.");
        this.accessLevel = accessLevel;
    }

}
