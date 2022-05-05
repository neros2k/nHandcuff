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
import java.util.ArrayList;
public class CuffPresenter extends APresenter implements Listener {
    private final ArrayList<String> COOLDOWN;
    public CuffPresenter(IInteractor INTERACTOR) {
        super(INTERACTOR);
        this.COOLDOWN = new ArrayList<>();
    }
    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, super.getInteractor().getPlugin());
    }
    @EventHandler
    public void onPlayerCuff(@NotNull PlayerInteractAtEntityEvent EVENT) {
        Player HOLDER = EVENT.getPlayer();
        String HOLDER_NAME = HOLDER.getName();
        if(!EVENT.isCancelled() && !this.COOLDOWN.contains(HOLDER_NAME)) {
            if(EVENT.getRightClicked() instanceof Player PLAYER) {
                IInteractor INTERACTOR = this.getInteractor();
                if(HOLDER.hasPermission("nhandcuff.use") && !PLAYER.hasPermission("nhandcuff.bypass")) {
                    IEngine ENGINE = INTERACTOR.getEngine(PLAYER.getName());
                    Material MATERIAL = HOLDER.getInventory().getItemInMainHand().getType();
                    if(ENGINE.getState().isCuffed()) {
                        if(ENGINE.getState().getHolder().equals(HOLDER_NAME)) {
                            INTERACTOR.uncuffPlayer(PLAYER, HOLDER);
                            if(HOLDER.getGameMode() != GameMode.CREATIVE) {
                                HOLDER.getInventory().addItem(new ItemStack(Material.LEAD));
                            }
                        }
                    } else if(MATERIAL == Material.LEAD) {
                        INTERACTOR.cuffPlayer(PLAYER, HOLDER);
                        if(HOLDER.getGameMode() != GameMode.CREATIVE) {
                            ItemStack ITEM = HOLDER.getInventory().getItemInMainHand();
                            ITEM.setAmount(ITEM.getAmount() - 1);
                        }
                    }
                }
                this.COOLDOWN.add(HOLDER_NAME);
                Bukkit.getScheduler().runTaskLater(INTERACTOR.getPlugin(),
                        () -> COOLDOWN.remove(HOLDER_NAME), INTERACTOR.getModel().INTERACT_COOLDOWN);
            }
        }
    }
}
