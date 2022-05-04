package n2k.nhandcuff.base.object;
public class State {
    private String BINDER;
    private Boolean CUFFED;
    public State(Boolean CUFFED) {
        this.CUFFED = CUFFED;
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
