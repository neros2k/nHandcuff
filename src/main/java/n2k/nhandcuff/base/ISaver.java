package n2k.nhandcuff.base;
import n2k.nhandcuff.base.object.State;
public interface ISaver extends IInitializable {
    void save(State STATE);
    State getByName(String NAME);
}
