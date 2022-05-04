package n2k.nhandcuff.core;
import n2k.nhandcuff.base.IEngine;
import n2k.nhandcuff.base.IInteractor;
import n2k.nhandcuff.base.object.State;
import org.bukkit.Bukkit;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
public class Engine implements IEngine {
    private final IInteractor INTERACTOR;
    private final Player PLAYER;
    private final State STATE;
    private Integer BAT_TICK_ID;
    private Integer PLAYER_TICK_ID;
    private Integer CUFF_TICK_ID;
    private Bat BAT;
    public Engine(IInteractor INTERACTOR, @NotNull Player PLAYER, Boolean isCuffed) {
        this.INTERACTOR = INTERACTOR;
        this.PLAYER = PLAYER;
        this.STATE = new State(false);
    }
    @Override
    public void init() {
        if(this.STATE.isCuffed()) this.cuff();
    }
    @Override
    public void start() {
        this.BAT = (Bat) PLAYER.getWorld().spawnEntity(PLAYER.getLocation(), EntityType.BAT);
        this.BAT.setAI(false);
        BukkitScheduler SCHEDULER = Bukkit.getScheduler();
        this.BAT_TICK_ID = SCHEDULER.runTaskTimer(
                this.INTERACTOR.getPlugin(), this::batTick, 0L, 80L)
                .getTaskId();
        this.PLAYER_TICK_ID = SCHEDULER.runTaskTimer(
                this.INTERACTOR.getPlugin(), this::playerTick, 40L, 80L)
                .getTaskId();
    }
    @Override
    public void stop() {
        BukkitScheduler SCHEDULER = Bukkit.getScheduler();
        SCHEDULER.cancelTask(this.BAT_TICK_ID);
        SCHEDULER.cancelTask(this.PLAYER_TICK_ID);
        this.uncuff();
        this.BAT.damage(1000D);
    }
    @Override
    public void cuff() {
        this.CUFF_TICK_ID = Bukkit.getScheduler().runTaskTimer(
                this.INTERACTOR.getPlugin(), this::cuffTick, 0L, 1L)
                .getTaskId();
        this.STATE.setCuffed(true);
    }
    @Override
    public void uncuff() {
        Bukkit.getScheduler().cancelTask(this.CUFF_TICK_ID);
        this.STATE.setCuffed(false);
    }
    @Override
    public void batTick() {
        this.BAT.teleport(this.PLAYER.getLocation());
    }
    @Override
    public void playerTick() {
        this.PLAYER.teleport(this.BAT.getLocation());
    }
    @Override
    public void cuffTick() {
        this.PLAYER.addPotionEffect(
                new PotionEffect(PotionEffectType.SLOW, 1, 1)
        );
    }
    @Override
    public Player getPlayer() {
        return this.PLAYER;
    }
    @Override
    public IInteractor getInteractor() {
        return this.INTERACTOR;
    }
    @Override
    public State getState() {
        return this.STATE;
    }
    @Override
    public Bat getBat() {
        return this.BAT;
    }
}
