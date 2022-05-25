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

                //О́тче наш, И́же еси́ на небесе́х!
                //Да святи́тся имя Твое́,
                //да прии́дет Ца́рствие Твое́,
                //да бу́дет во́ля Твоя,
                //я́ко на небеси́ и на земли́.
                //Хлеб наш насу́щный даждь нам днесь;
                //И оста́ви нам до́лги наша,
                //Якоже и мы оставля́ем должнико́м нашим;
                //И не введи́ нас во искуше́ние,
                //Но изба́ви нас от лука́ваго.

                this.INTERACTOR.init();
            });
        }
    }
    @Override
    public void onDisable() {
        if(this.INTERACTOR != null) {
            this.INTERACTOR.getEngineMap().forEach((String NAME, IEngine ENGINE) -> ENGINE.stop());
        }
    }
    public JSONConfig<ConfigModel> getJsonConfig() {
        return this.JSON_CONFIG;
    }
}
