package n2k.nhandcuff.base;
import n2k.nhandcuff.base.object.RState;
public interface ISaver {
    void save(RState STATE);
    RState getByName(String NAME);
}
