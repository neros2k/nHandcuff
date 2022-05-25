package n2k.nhandcuff.core;
import n2k.nhandcuff.base.IEngine;
import n2k.nhandcuff.base.IInteractor;
import n2k.nhandcuff.base.model.ConfigModel;
import n2k.nhandcuff.base.object.State;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
public class Engine implements IEngine {
    private final ArrayList<String> LEASHED;
    private final IInteractor INTERACTOR;
    private final Player PLAYER;
    private final State STATE;
    private Integer BAT_TICK_ID;
    private Integer CUFF_TICK_ID;
    private Bat BAT;
    public Engine(IInteractor INTERACTOR, @NotNull Player PLAYER, Boolean CUFFED) {
        this.LEASHED = new ArrayList<>();
        this.INTERACTOR = INTERACTOR;
        this.PLAYER = PLAYER;
        this.STATE = new State(CUFFED);
    }
    /**
     * Инициализирует движок.
     */
    @Override
    public void init() {
        if(this.STATE.isCuffed()) this.cuff();
    }
    /**
     * Запускает движок. Спавнит сущность в виде гномика и его тик.
     */
    @Override
    public void start() {
        Location LOCATION = this.PLAYER.getLocation().add(0.0, this.getInteractor().getModel().Y_SPAWN_ADD, 0.0);
        this.BAT = (Bat) PLAYER.getWorld().spawnEntity(LOCATION, EntityType.BAT);
        this.BAT.setAI(false);
        this.BAT.setCollidable(false);
        this.BAT.setInvisible(true);
        this.BAT.setSilent(true);
        this.BAT.setInvulnerable(true);
        BukkitScheduler SCHEDULER = Bukkit.getScheduler();
        this.BAT_TICK_ID = SCHEDULER.runTaskTimer(
                this.INTERACTOR.getPlugin(), this::batTick, 0L, this.getInteractor().getModel().BAT_TICK
        ).getTaskId();
    }
    /**
     * Останавливает движок. Хуярит гномика.
     */
    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(this.BAT_TICK_ID);
        this.BAT.remove();
        if(this.STATE.isCuffed()) {
            this.drop();
            this.uncuff();
        }
    }
    /**
     * Заковывает игрока. Запускает тик его страданий.
     */
    @Override
    public void cuff() {
        this.CUFF_TICK_ID = Bukkit.getScheduler().runTaskTimer(
                this.INTERACTOR.getPlugin(), this::cuffTick, 0L, this.getInteractor().getModel().CUFF_TICK)
                .getTaskId();
        this.STATE.setCuffed(true);
    }
    /**
     * Освобождает игрока. Отменяет его страдания и убирает веревку.
     */
    @Override
    public void uncuff() {
        Bukkit.getScheduler().cancelTask(this.CUFF_TICK_ID);
        this.BAT.setLeashHolder(null);
        this.STATE.setHolder("");
        this.STATE.setCuffed(false);
    }
    /**
     * Тик гомика.
     */
    @Override
    public void batTick() {
        this.BAT.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 6));
        this.BAT.teleport(this.PLAYER.getLocation());
    }
    /**
     * Тик страданий.
     */
    @Override
    public void cuffTick() {
        Player HOLDER = Bukkit.getPlayer(this.STATE.getHolder());
        ConfigModel MODEL = this.INTERACTOR.getModel();
        if(HOLDER != null) {
            Location LOCATION = this.PLAYER.getLocation();
            Location HOLDER_LOCATION = HOLDER.getLocation();
            double DISTANCE = LOCATION.distanceSquared(HOLDER_LOCATION);
            // Если игрок мертвый его страдания окончены.
            if(HOLDER.isDead() || !this.getInteractor().getEngineMap().containsKey(HOLDER.getName())) {
                this.uncuff();
                this.drop();
                return;
            }
            // Если он сбежал в другой мир его телепортирует к держателю.
            if(HOLDER.getWorld() != this.PLAYER.getWorld()) {
                this.PLAYER.teleport(HOLDER_LOCATION);
                this.BAT.setLeashHolder(HOLDER);
                return;
            }
            // Если держателя телепортировали страдалец телепортируется вместе с ним.
            if(DISTANCE > MODEL.TELEPORT_DISTANCE) {
                this.PLAYER.teleport(HOLDER_LOCATION);
            } else if(DISTANCE > MODEL.BREAK_DISTANCE ||
                     (this.BAT.isLeashed() && !(this.BAT.getLeashHolder() instanceof Player))) {
                this.getInteractor().uncuffPlayer(this.PLAYER, HOLDER);
                this.drop();
            }
            // Если он пытается убежать от страданий его пиздюлит обратно.
            if(DISTANCE > MODEL.VELOCITY_DISTANCE) {
                double MULTIPLY = DISTANCE/MODEL.DISTANCE_MULTIPLIER;
                if(MULTIPLY > MODEL.MAX_MULTIPLY) MULTIPLY = MODEL.MAX_MULTIPLY;
                Vector VECTOR = HOLDER_LOCATION.toVector().subtract(LOCATION.toVector()).multiply(MULTIPLY);
                this.PLAYER.setVelocity(VECTOR);
            }
        }
        // Усугубление страданий. Даже если нет держателя игрок все равно будет страдать.
        this.PLAYER.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20, MODEL.SLOW_AMPLIFIER_LOAD));
    }
    /**
     * Выбрасывает предмет веревки.
     */
    @Override
    public void drop() {
        Location LOCATION = this.PLAYER.getLocation();
        World WORLD = LOCATION.getWorld();
        assert WORLD != null;
        WORLD.dropItem(LOCATION, new ItemStack(Material.LEAD));
    }
    /**
     * Дает тебе игрока.
     * @return А нет, не дает. Тебе никто никогда не даст.
     */
    @Override
    public Player getPlayer() {
        return this.PLAYER;
    }
    /**
     * Возвращает интерактор.
     * @return Че не слышал?
     */
    @Override
    public IInteractor getInteractor() {
        return this.INTERACTOR;
    }
    /**
     * Отправляет тебе состояние движка.
     * @return Волшебное слово сказать не забудь.
     */
    @Override
    public State getState() {
        return this.STATE;
    }
    /**
     * Возвращает сущность в виде гномика.
     * @return Ладно, летучая мышь обычная.
     */
    @Override
    public Bat getBat() {
        return this.BAT;
    }
    /**
     * Не ебу че это.
     * @return Ладно. Список тех, кого держит игрок.
     */
    @Override
    public ArrayList<String> getLeashed() {
        return this.LEASHED;
    }
}
