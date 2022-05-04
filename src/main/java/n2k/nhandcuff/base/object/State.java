package n2k.nhandcuff.base.object;
public class State {
    private final String NAME;
    private Boolean CUFFED;
    public State(String NAME, Boolean CUFFED) {
        this.NAME = NAME;
        this.CUFFED = CUFFED;
    }
    public String getName() {
        return this.NAME;
    }
    public Boolean isCuffed() {
        return this.CUFFED;
    }
    public void setCuffed(Boolean CUFFED) {
        this.CUFFED = CUFFED;
    }
}
