package n2k.nhandcuff.base;
import n2k.nhandcuff.base.object.State;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import java.util.ArrayList;
public interface IEngine extends IInitializable {
    void start();
    void stop();
    void cuff();
    void uncuff();
    void batTick();
    void cuffTick();
    Player getPlayer();
    IInteractor getInteractor();
    State getState();
    Bat getBat();
    ArrayList<String> getLeashed();
}
