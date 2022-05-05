package n2k.nhandcuff;
import n2k.nhandcuff.base.IEngine;
import n2k.nhandcuff.base.IInteractor;
import n2k.nhandcuff.core.Interactor;
import org.bukkit.plugin.java.JavaPlugin;
public final class nHandCuff extends JavaPlugin {
    private final IInteractor INTERACTOR;
    public nHandCuff() {
        this.INTERACTOR = new Interactor(this);
    }
    @Override
    public void onEnable() {
        this.INTERACTOR.init();
    }

    @Override
    public void onDisable() {
        this.INTERACTOR.getEngineMap().forEach((String NAME, IEngine ENGINE) -> this.INTERACTOR.unloadEngine(NAME));
    }
}
