package n2k.nhandcuff.core;
import n2k.nhandcuff.base.APresenter;
import n2k.nhandcuff.base.IEngine;
import n2k.nhandcuff.base.IInteractor;
import n2k.nhandcuff.core.presenter.CuffPresenter;
import n2k.nhandcuff.core.presenter.OtherPresenter;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
public class Interactor implements IInteractor {
    private final APresenter CUFF_PRESENTER;
    private final APresenter OTHER_PRESENTER;
    private final Map<String, IEngine> ENGINE_MAP;
    private final JavaPlugin PLUGIN;
    public Interactor(JavaPlugin PLUGIN) {
        this.CUFF_PRESENTER = new CuffPresenter(this);
        this.OTHER_PRESENTER = new OtherPresenter(this);
        this.ENGINE_MAP = new HashMap<>();
        this.PLUGIN = PLUGIN;
    }
    @Override
    public void init() {
        this.CUFF_PRESENTER.init();
        this.OTHER_PRESENTER.init();
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
}
