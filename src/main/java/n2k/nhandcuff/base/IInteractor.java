package n2k.nhandcuff.base;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
public interface IInteractor {
    void loadEngine(Player PLAYER);
    void unloadEngine(String NAME);
    void cuffPlayer(Player PLAYER);
    void uncuffPlayer(Player PLAYER);
    void bind(Player BINDED, Player BINDER);
    void unbind(Player BINDED);
    IEngine getEngine(String NAME);
    JavaPlugin getPlugin();
}
