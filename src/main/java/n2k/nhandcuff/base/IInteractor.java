package n2k.nhandcuff.base;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
public interface IInteractor extends IInitializable {
    void loadEngine(Player PLAYER);
    void unloadEngine(String NAME);
    void cuffPlayer(Player PLAYER, Player HOLDER);
    void uncuffPlayer(Player PLAYER, Player HOLDER);
    IEngine getEngine(String NAME);
    JavaPlugin getPlugin();
}
