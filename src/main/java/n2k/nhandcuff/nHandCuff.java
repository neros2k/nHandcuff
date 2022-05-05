package n2k.nhandcuff;
import n2k.nhandcuff.base.IEngine;
import n2k.nhandcuff.base.IInteractor;
import n2k.nhandcuff.base.model.ConfigModel;
import n2k.nhandcuff.core.Interactor;
import neros2k.jcapi.JCApi;
import neros2k.jcapi.JSONConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Optional;
public final class nHandCuff extends JavaPlugin {
    private final IInteractor INTERACTOR;
    private JSONConfig<ConfigModel> JSON_CONFIG;
    public nHandCuff() {
        this.INTERACTOR = new Interactor(this);
        this.JSON_CONFIG = null;
    }
    @Override
    public void onEnable() {
        if(Bukkit.getPluginManager().isPluginEnabled("JSONConfigAPI")) {
            Optional<JSONConfig<ConfigModel>> JSON_CONFIG_OPT = JCApi.getNew(this, ConfigModel.class, "config.json");
            JSON_CONFIG_OPT.ifPresent(CONFIG -> {
                this.JSON_CONFIG = CONFIG;
                CONFIG.reload();
                this.INTERACTOR.init();
            });
        }
    }
    @Override
    public void onDisable() {
        if(this.INTERACTOR != null) {
            Bukkit.getScheduler().runTaskLater(this, () -> {
                this.INTERACTOR.getEngineMap().forEach((String NAME, IEngine ENGINE) -> this.INTERACTOR.unloadEngine(NAME));
            }, 20L);
        }
    }
    public JSONConfig<ConfigModel> getJsonConfig() {
        return this.JSON_CONFIG;
    }
}
