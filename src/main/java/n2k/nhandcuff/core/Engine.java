package n2k.nhandcuff.core;
import n2k.nhandcuff.base.IEngine;
import n2k.nhandcuff.base.IInteractor;
import n2k.nhandcuff.base.object.State;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import java.util.Objects;
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
    @Override
    public void init() {
        if(this.STATE.isCuffed()) this.cuff();
    }
    @Override
    public void start() {
        this.BAT = (Bat) PLAYER.getWorld().spawnEntity(PLAYER.getLocation(), EntityType.BAT);
        this.BAT.setAI(false);
        this.BAT.setCollidable(false);
        BukkitScheduler SCHEDULER = Bukkit.getScheduler();
        this.BAT_TICK_ID = SCHEDULER.runTaskTimer(
                this.INTERACTOR.getPlugin(), this::batTick, 0L, 1L)
                .getTaskId();
    }
    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(this.BAT_TICK_ID);
        this.BAT.remove();
        if(this.STATE.isCuffed()) {
            this.drop();
            this.uncuff();
        }
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
    public void cuffTick() {
        Player HOLDER = Bukkit.getPlayer(this.STATE.getHolder());
        if(HOLDER != null) {
            Location LOCATION = this.PLAYER.getLocation();
            Location HOLDER_LOCATION = HOLDER.getLocation();
            double DISTANCE = LOCATION.distanceSquared(HOLDER_LOCATION);
            if(DISTANCE > 7) {
                double MULTIPLY = DISTANCE/120;
                Vector VECTOR = HOLDER_LOCATION.toVector().subtract(LOCATION.toVector()).multiply(MULTIPLY);
                this.PLAYER.setVelocity(VECTOR);
            }
            if(!(this.BAT.getLeashHolder() instanceof Player)) {
                this.getInteractor().uncuffPlayer(this.PLAYER, HOLDER);
                this.drop();
            }
        }
        this.PLAYER.addPotionEffect(
                new PotionEffect(PotionEffectType.SLOW, 20, 2)
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
    @Override
    public ArrayList<String> getLeashed() {
        return this.LEASHED;
    }
    private void drop() {
        Location LOCATION = this.PLAYER.getLocation();
        Objects.requireNonNull(LOCATION.getWorld()).dropItem(LOCATION, new ItemStack(Material.LEAD));
    }
}
