package n2k.nhandcuff.core.presenter;
import n2k.nhandcuff.base.APresenter;
import n2k.nhandcuff.base.IEngine;
import n2k.nhandcuff.base.IInteractor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
public class CuffPresenter extends APresenter implements Listener {
    public CuffPresenter(IInteractor INTERACTOR) {
        super(INTERACTOR);
    }
    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, super.getInteractor().getPlugin());
    }
    @EventHandler
    public void onPlayerInteract(@NotNull PlayerInteractAtEntityEvent EVENT) {
        if(!EVENT.isCancelled()) {
            Player PLAYER = EVENT.getPlayer();
            if(EVENT.getRightClicked() instanceof Player BIND_PLAYER) {
                IInteractor INTERACTOR = this.getInteractor();
                IEngine ENGINE = INTERACTOR.getEngine(BIND_PLAYER.getName());
                Material MATERIAL = PLAYER.getInventory().getItemInMainHand().getType();
                if(ENGINE.getState().isCuffed() && ENGINE.getState().getBinder().equals(PLAYER.getName())) {
                    INTERACTOR.uncuffPlayer(BIND_PLAYER);
                    INTERACTOR.unbind(BIND_PLAYER);
                    PLAYER.getInventory().addItem(new ItemStack(Material.LEAD));
                } else if(MATERIAL == Material.LEAD) {
                    INTERACTOR.cuffPlayer(BIND_PLAYER);
                    INTERACTOR.bind(PLAYER, BIND_PLAYER);
                    if(PLAYER.getGameMode() != GameMode.CREATIVE) {
                        ItemStack ITEM = PLAYER.getInventory().getItemInMainHand();
                        ITEM.setAmount(ITEM.getAmount() - 1);
                    }
                }
            }
        }
    }
}
