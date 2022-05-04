package n2k.nhandcuff.core.presenter;
import n2k.nhandcuff.base.APresenter;
import n2k.nhandcuff.base.IInteractor;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
public class OtherPresenter extends APresenter implements Listener {
    public OtherPresenter(IInteractor INTERACTOR) {
        super(INTERACTOR);
    }
    @Override
    public void init() {
        Bukkit.getServer().getPluginManager().registerEvents(this, super.getInteractor().getPlugin());
    }
}
