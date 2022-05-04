package n2k.nhandcuff.base;
import org.bukkit.entity.Player;
public interface IEngine extends IInitializable {
    void start(Player PLAYER);
    void stop();
    void cuff();
    void uncuff();
    Player getPlayer();
}
