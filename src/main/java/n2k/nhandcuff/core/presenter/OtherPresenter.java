package n2k.nhandcuff.core.presenter;
import n2k.nhandcuff.base.APresenter;
import n2k.nhandcuff.base.IInteractor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
public class OtherPresenter extends APresenter implements Listener {
    public OtherPresenter(IInteractor INTERACTOR) {
        super(INTERACTOR);
    }
    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, super.getInteractor().getPlugin());
    }
    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent EVENT) {
        this.getInteractor().loadEngine(EVENT.getPlayer());
    }
    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent EVENT) {
        this.getInteractor().unloadEngine(EVENT.getPlayer().getName());
    }
}
