package n2k.nhandcuff.core;
import n2k.nhandcuff.base.IEngine;
import n2k.nhandcuff.base.IInteractor;
import n2k.nhandcuff.base.ISaver;
import n2k.nhandcuff.base.object.State;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;
public class Interactor implements IInteractor {
    private final ISaver SAVER;
    private final Map<String, IEngine> ENGINE_MAP;
    private final JavaPlugin PLUGIN;
    public Interactor(JavaPlugin PLUGIN) {
        this.SAVER = new Saver();
        this.ENGINE_MAP = new HashMap<>();
        this.PLUGIN = PLUGIN;
    }
    @Override
    public void init() {
        this.SAVER.init();
    }
    @Override
    public void loadEngine(@NotNull Player PLAYER) {
        String NAME = PLAYER.getName();
        if(!this.ENGINE_MAP.containsKey(NAME)) {
            State STATE = this.SAVER.getByName(NAME);
            IEngine ENGINE = new Engine(this, PLAYER, STATE.isCuffed());
            ENGINE.init();
            ENGINE.start();
            this.ENGINE_MAP.put(NAME, ENGINE);
        }
    }
    @Override
    public void unloadEngine(String NAME) {
        if(this.ENGINE_MAP.containsKey(NAME)) {
            this.ENGINE_MAP.get(NAME).stop();
        }
    }
    @Override
    public void cuffPlayer(@NotNull Player PLAYER) {
        String NAME = PLAYER.getName();
        if(this.ENGINE_MAP.containsKey(NAME)) {
            this.ENGINE_MAP.get(NAME).cuff();
        }
    }
    @Override
    public void uncuffPlayer(@NotNull Player PLAYER) {
        String NAME = PLAYER.getName();
        if(this.ENGINE_MAP.containsKey(NAME)) {
            this.ENGINE_MAP.get(NAME).uncuff();
        }
    }
    @Override
    public void bind(@NotNull Player BINDED, Player BINDER) {
        Bat BAT = this.getEngine(BINDED.getName()).getBat();
        if(!BAT.isLeashed()) BAT.setLeashHolder(BINDED);
    }
    @Override
    public void unbind(@NotNull Player BINDED) {
        Bat BAT = this.getEngine(BINDED.getName()).getBat();
        if(BAT.isLeashed()) BAT.setLeashHolder(null);
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
}
