public interface Escalatable {
    public void escalate();
    public int escalationLevel();
    public boolean isEscalated();
    default String escalationBadge() {
        return isEscalated() ? "[ESCALATED]" : "[NORMAL]";
    }
}
