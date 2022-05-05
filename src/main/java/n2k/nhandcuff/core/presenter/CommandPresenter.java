package n2k.nhandcuff.core.presenter;
import n2k.nhandcuff.base.APresenter;
import n2k.nhandcuff.base.IInteractor;
import n2k.nhandcuff.base.model.ConfigModel;
import n2k.nhandcuff.nHandCuff;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
public class CommandPresenter extends APresenter implements CommandExecutor {
    public CommandPresenter(IInteractor INTERACTOR) {
        super(INTERACTOR);
    }
    @Override
    public void init() {
        PluginCommand COMMAND = super.getInteractor().getPlugin().getCommand("handcuff");
        assert COMMAND != null;
        COMMAND.setExecutor(this);
    }
    @Override
    public boolean onCommand(@NotNull CommandSender SENDER, @NotNull Command COMMAND, @NotNull String STR, @NotNull String @NotNull [] ARGS) {
        ConfigModel MODEL = this.getInteractor().getModel();
        IInteractor INTERACTOR = super.getInteractor();
        if(!(ARGS.length >= 1)) return false;
        if(ARGS[0].equals("reload") && SENDER.hasPermission("nhandcuff.reload")) {
            ((nHandCuff) INTERACTOR.getPlugin()).getJsonConfig().reload();
            SENDER.sendMessage(MODEL.RELOAD_COMMAND);
        }
        if(ARGS.length > 1) {
            Player HOLDER = Bukkit.getPlayer(ARGS[1]);
            if(HOLDER != null && SENDER instanceof Player PLAYER) {
                double DISTANCE = HOLDER.getLocation().distance(PLAYER.getLocation());
                if(DISTANCE > MODEL.COMMAND_DISTANCE && !PLAYER.hasPermission("nhandcuff.bypass")) {
                    if(ARGS[0].equals("cuff") && SENDER.hasPermission("nhandcuff.cuff")) {
                        INTERACTOR.cuffPlayer(PLAYER, HOLDER);
                    }
                    if(ARGS[0].equals("uncuff") && SENDER.hasPermission("nhandcuff.cuff")) {
                        INTERACTOR.uncuffPlayer(PLAYER, HOLDER);
                    }
                }
            }
        }
        return false;
    }
}
