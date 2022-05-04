package n2k.nhandcuff.base.object;
public class State {
    private final String NAME;
    private String BINDER;
    private Boolean CUFFED;
    public State(String NAME, Boolean CUFFED) {
        this.NAME = NAME;
        this.CUFFED = CUFFED;
    }
    public String getName() {
        return this.NAME;
    }
    public String getBinder() {
        return this.BINDER;
    }
    public Boolean isCuffed() {
        return this.CUFFED;
    }
    public void setBinder(String BINDER) {
        this.BINDER = BINDER;
    }
    public void setCuffed(Boolean CUFFED) {
        this.CUFFED = CUFFED;
    }
}
