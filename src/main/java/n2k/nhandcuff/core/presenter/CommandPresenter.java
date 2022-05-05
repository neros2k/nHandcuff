package n2k.nhandcuff.core.presenter;
import n2k.nhandcuff.base.APresenter;
import n2k.nhandcuff.base.IEngine;
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
        if(ARGS.length == 0) {
            SENDER.sendMessage(MODEL.UNKNOWN_ERROR);
            return false;
        }
        if(!SENDER.hasPermission("nhandcuff.admin")) {
            SENDER.sendMessage(MODEL.PERM_ERROR);
            return false;
        }
        if(ARGS[0].equals("reload")) {
            ((nHandCuff) INTERACTOR.getPlugin()).getJsonConfig().reload();
            SENDER.sendMessage(MODEL.RELOAD_COMMAND);
            return true;
        }
        if(ARGS.length > 1) {
            Player PLAYER = Bukkit.getPlayer(ARGS[1]);
            if(PLAYER != null) {
                IEngine ENGINE = INTERACTOR.getEngine(ARGS[1]);
                if(ARGS[0].equals("list")) {
                    StringBuilder BUILDER = new StringBuilder();
                    ENGINE.getLeashed().forEach(LEASHED -> BUILDER.append(LEASHED).append(" ; "));
                    SENDER.sendMessage(BUILDER.toString());
                }
                if(ARGS[0].equals("clear")) {
                    ENGINE.getLeashed().forEach(LEASHED -> INTERACTOR.uncuffPlayer(
                            Bukkit.getPlayer(LEASHED), PLAYER
                    ));
                }
                return true;
            }
        }
        SENDER.sendMessage(MODEL.UNKNOWN_ERROR);
        return false;
    }
}
