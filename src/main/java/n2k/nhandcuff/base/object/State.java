package n2k.nhandcuff.base.object;
public class State {
    private String HOLDER;
    private Boolean CUFFED;
    public State(Boolean CUFFED) {
        this.HOLDER = "";
        this.CUFFED = CUFFED;
    }
    public String getHolder() {
        return this.HOLDER;
    }
    public Boolean isCuffed() {
        return this.CUFFED;
    }
    public void setHolder(String HOLDER) {
        this.HOLDER = HOLDER;
    }
    public void setCuffed(Boolean CUFFED) {
        this.CUFFED = CUFFED;
    }
}
