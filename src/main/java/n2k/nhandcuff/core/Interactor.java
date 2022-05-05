package n2k.nhandcuff.core;
import n2k.nhandcuff.base.APresenter;
import n2k.nhandcuff.base.IEngine;
import n2k.nhandcuff.base.IInteractor;
import n2k.nhandcuff.base.model.ConfigModel;
import n2k.nhandcuff.core.presenter.CommandPresenter;
import n2k.nhandcuff.core.presenter.CuffPresenter;
import n2k.nhandcuff.core.presenter.OtherPresenter;
import n2k.nhandcuff.nHandCuff;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class Interactor implements IInteractor {
    private final List<APresenter> PRESENTER_LIST;
    private final Map<String, IEngine> ENGINE_MAP;
    private final JavaPlugin PLUGIN;
    public Interactor(JavaPlugin PLUGIN) {
        this.PRESENTER_LIST = new ArrayList<>();
        this.ENGINE_MAP = new HashMap<>();
        this.PLUGIN = PLUGIN;
    }
    @Override
    public void init() {
        this.PRESENTER_LIST.addAll(List.of(
                new CommandPresenter(this),
                new CuffPresenter(this),
                new OtherPresenter(this)
        ));
        this.PRESENTER_LIST.forEach(APresenter::init);
    }
    @Override
    public void loadEngine(@NotNull Player PLAYER) {
        String NAME = PLAYER.getName();
        if(!this.ENGINE_MAP.containsKey(NAME)) {
            IEngine ENGINE = new Engine(this, PLAYER, false);
            ENGINE.init();
            ENGINE.start();
            this.ENGINE_MAP.put(NAME, ENGINE);
        }
    }
    @Override
    public void unloadEngine(String NAME) {
        if(this.ENGINE_MAP.containsKey(NAME)) {
            this.ENGINE_MAP.get(NAME).stop();
            this.ENGINE_MAP.remove(NAME);
        }
    }
    @Override
    public void reloadEngine(@NotNull Player PLAYER) {
        this.unloadEngine(PLAYER.getName());
        this.loadEngine(PLAYER);
    }
    @Override
    public void cuffPlayer(@NotNull Player PLAYER, Player HOLDER) {
        String NAME = PLAYER.getName();
        Bat BAT = this.getEngine(PLAYER.getName()).getBat();
        IEngine ENGINE = this.getEngine(NAME);
        BAT.setLeashHolder(HOLDER);
        ENGINE.getState().setHolder(HOLDER.getName());
        ENGINE.cuff();
        this.getEngine(HOLDER.getName()).getLeashed().add(NAME);
        this.playSound(List.of(PLAYER, HOLDER), Sound.valueOf(this.getModel().CUFF_SOUND));
        PLAYER.sendMessage(this.getModel().CUFF_MESSAGE);
        HOLDER.sendMessage(this.getModel().PLAYER_CUFF_MESSAGE.replace("{player}", NAME));
    }
    @Override
    public void uncuffPlayer(@NotNull Player PLAYER, @NotNull Player HOLDER) {
        String NAME = PLAYER.getName();
        Bat BAT = this.getEngine(PLAYER.getName()).getBat();
        IEngine ENGINE = this.getEngine(NAME);
        BAT.setLeashHolder(null);
        ENGINE.uncuff();
        ENGINE.getState().setHolder("");
        this.getEngine(HOLDER.getName()).getLeashed().remove(NAME);
        this.playSound(List.of(PLAYER, HOLDER), Sound.valueOf(this.getModel().UNCUFF_SOUND));
    }
    @Override
    public IEngine getEngine(String NAME) {
        if(this.ENGINE_MAP.containsKey(NAME)) {
            return this.ENGINE_MAP.get(NAME);
        }
        return null;
    }
    @Override
    public JavaPlugin getPlugin() {
        return this.PLUGIN;
    }
    @Override
    public Map<String, IEngine> getEngineMap() {
        return this.ENGINE_MAP;
    }
    @Override
    public ConfigModel getModel() {
        return ((nHandCuff) this.getPlugin()).getJsonConfig().getJson();
    }
    private void playSound(@NotNull List<Player> PLAYERS, Sound SOUND) {
        PLAYERS.forEach(PLAYER -> PLAYER.playSound(PLAYER.getLocation(), SOUND, 0.5F, 1));
    }
}
