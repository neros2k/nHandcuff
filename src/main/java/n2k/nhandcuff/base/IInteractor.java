package n2k.nhandcuff.base;
import org.bukkit.entity.Player;
public interface IInteractor extends IInitializable {
    void cuffPlayer(Player PLAYER);
    void uncuffPlayer(Player PLAYER);
}
