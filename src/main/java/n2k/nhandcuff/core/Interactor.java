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
    /**
     * Инициализирует слушателей событий.
     */
    @Override
    public void init() {
        this.PRESENTER_LIST.addAll(List.of(
                new CommandPresenter(this),
                new CuffPresenter(this),
                new OtherPresenter(this)
        ));
        this.PRESENTER_LIST.forEach(APresenter::init);
    }
    /**
     * Загружает и запускает движок по экземпляру класса игрока.
     * @param PLAYER Игрок. Логично?
     */
    @Override
    public void loadEngine(@NotNull Player PLAYER) {
        String NAME = PLAYER.getName();
        if(this.ENGINE_MAP.containsKey(NAME)) return;
        IEngine ENGINE = new Engine(this, PLAYER, false);
        ENGINE.init();
        ENGINE.start();
        this.ENGINE_MAP.put(NAME, ENGINE);
    }
    /**
     * Выгружает и приостанавливает работу движка по никнейму игрока.
     * @param NAME Ник игрока.
     */
    @Override
    public void unloadEngine(String NAME) {
        if(!this.ENGINE_MAP.containsKey(NAME)) return;
        IEngine ENGINE = this.ENGINE_MAP.get(NAME);
        ENGINE.stop();
        this.ENGINE_MAP.remove(NAME);
    }
    /**
     * Заковывает игрока наручники. Привязывает игрока к держателю.
     * Отправляет сообщение, проигрывает звук и делает лог.
     * @param PLAYER Игрок, который будет закован.
     * @param HOLDER Игрок-держатель.
     */
    @Override
    public void cuffPlayer(@NotNull Player PLAYER, Player HOLDER) {
        String NAME = PLAYER.getName();
        Bat BAT = this.getEngine(PLAYER.getName()).getBat();
        IEngine ENGINE = this.getEngine(NAME);
        BAT.setLeashHolder(HOLDER);
        ENGINE.getState().setHolder(HOLDER.getName());
        ENGINE.cuff();
        this.getEngine(HOLDER.getName()).getLeashed().add(NAME);
        this.playCuffSound(List.of(PLAYER, HOLDER), Sound.valueOf(this.getModel().CUFF_SOUND));
        PLAYER.sendMessage(this.getModel().CUFF_MESSAGE);
        HOLDER.sendMessage(this.getModel().PLAYER_CUFF_MESSAGE.replace("{player}", NAME));
        if(this.getModel().CONSOLE_LOG) this.getPlugin().getLogger().info(NAME + " cuffed | Holder: " + HOLDER.getName());
    }
    /**
     * Снимает с игрока наручники. Отвязывает от игрока держателя.
     * Проигрывает звук, делает лог, лечит от рака, избавляет от тревоги и готовит пельмени.
     * @param PLAYER Игрок, закованный в наручники.
     * @param HOLDER Игрок-держатель.
     */
    @Override
    public void uncuffPlayer(@NotNull Player PLAYER, @NotNull Player HOLDER) {
        String NAME = PLAYER.getName();
        Bat BAT = this.getEngine(PLAYER.getName()).getBat();
        IEngine ENGINE = this.getEngine(NAME);
        BAT.setLeashHolder(null);
        ENGINE.uncuff();
        ENGINE.getState().setHolder("");
        this.getEngine(HOLDER.getName()).getLeashed().remove(NAME);
        this.playCuffSound(List.of(PLAYER, HOLDER), Sound.valueOf(this.getModel().UNCUFF_SOUND));
        if(this.getModel().CONSOLE_LOG) this.getPlugin().getLogger().info(NAME + " uncuffed | Ex holder: " + HOLDER.getName());
    }
    /**
     * Дает тебе движок по имени игрока.
     * @param NAME Имя игрока, соответсвенно.
     * @return Вернет тебе null если ты быдло. А так, дает то, что ты от него ждешь.
     */
    @Override
    public IEngine getEngine(String NAME) {
        return this.ENGINE_MAP.getOrDefault(NAME, null);
    }
    /**
     * Дает плагин.
     * @return У тебя своих нет?
     */
    @Override
    public JavaPlugin getPlugin() {
        return this.PLUGIN;
    }
    /**
     * Возвращает мапу с движками.
     * @return Я чето не так сказал? null не даст, не переживай.
     */
    @Override
    public Map<String, IEngine> getEngineMap() {
        return this.ENGINE_MAP;
    }
    /**
     * Загадочный для многих функционал, понятный лишь мне.
     * @return Загадочная штука, что дает доступ к параметрам конфига. Бойся.
     */
    @Override
    public ConfigModel getModel() {
        return ((nHandCuff) this.getPlugin()).getJsonConfig().getJson();
    }

    /**
     * Проигрывает со смешного анекдота.
     * @param PLAYERS Игрок, с которым эта штука будет взаимодействовать.
     * @param SOUND Анекдот.
     */
    private void playCuffSound(@NotNull List<Player> PLAYERS, Sound SOUND) {
        PLAYERS.forEach(PLAYER -> PLAYER.playSound(PLAYER.getLocation(), SOUND, 0.5F, 1));
    }
}
