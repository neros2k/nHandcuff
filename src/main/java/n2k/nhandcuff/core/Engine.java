package n2k.nhandcuff.core;
import n2k.nhandcuff.base.IEngine;
import org.bukkit.entity.Player;
public class Engine implements IEngine {
    private Player PLAYER;
    @Override
    public void init() {

    }
    @Override
    public void start(Player PLAYER) {
        this.PLAYER = PLAYER;
    }
    @Override
    public void stop() {

    }
    @Override
    public void cuff() {

    }
    @Override
    public void uncuff() {

    }
    @Override
    public Player getPlayer() {
        return this.PLAYER;
    }
}
