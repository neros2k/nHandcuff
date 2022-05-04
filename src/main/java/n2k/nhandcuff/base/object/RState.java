package n2k.nhandcuff.base.object;
public record RState(String NAME, Boolean HANDCUFFED) {
    public String getName() {
        return this.NAME;
    }
    public Boolean isHandcuffed() {
        return this.HANDCUFFED;
    }
}
