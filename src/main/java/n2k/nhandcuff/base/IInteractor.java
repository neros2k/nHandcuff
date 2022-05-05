package n2k.nhandcuff.base;
import n2k.nhandcuff.base.model.ConfigModel;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Map;
public interface IInteractor extends IInitializable {
    void loadEngine(Player PLAYER);
    void unloadEngine(String NAME);
    void reloadEngine(Player PLAYER);
    void cuffPlayer(Player PLAYER, Player HOLDER);
    void uncuffPlayer(Player PLAYER, Player HOLDER);
    IEngine getEngine(String NAME);
    JavaPlugin getPlugin();
    Map<String, IEngine> getEngineMap();
    ConfigModel getModel();
}
