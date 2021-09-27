package fr.xilitra.higurashiuhc.roles.hinamizawa.memberofclub;

import fr.xilitra.higurashiuhc.HigurashiUHC;
import fr.xilitra.higurashiuhc.clans.Clans;
import fr.xilitra.higurashiuhc.game.Gender;
import fr.xilitra.higurashiuhc.game.task.taskClass.HanyuTaskInvisble;
import fr.xilitra.higurashiuhc.player.HPlayer;
import fr.xilitra.higurashiuhc.roles.Role;
import fr.xilitra.higurashiuhc.roles.RoleList;
import fr.xilitra.higurashiuhc.utils.DeathReason;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.Map;

public class Hanyu extends Role implements Listener {

    private boolean isInvisible;
    private boolean dimensionIsUsed;
    private final Map<HPlayer, Location> dimensionLastLoc = new HashMap<>();

    public Hanyu() {
        super("Hanyu", Gender.FEMME, Clans.MEMBER_OF_CLUB, 1);
        this.dimensionIsUsed = false;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e){
        if(!(e.getEntity() instanceof Player)) return;

        Player player = (Player) e.getEntity();

        HPlayer hPlayer = HigurashiUHC.getGameManager().getHPlayer(player.getUniqueId());
        if(hPlayer == null)
            return;

        if(e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) return;

        Entity entityDamager = e.getDamager();

        if(!(entityDamager instanceof Player)) return;

        Player damager = (Player) entityDamager;
        HPlayer hPlayerDamager = HigurashiUHC.getGameManager().getHPlayer(damager.getUniqueId());

        if(hPlayerDamager == null)
            return;

        if(hPlayerDamager.getRole().equals(RoleList.HANYU.getRole()) || hPlayer.getRole().isRole(RoleList.HANYU.getRole())){

            Hanyu hanyu;
            if(hPlayerDamager.getRole().equals(RoleList.HANYU.getRole()))
                hanyu = (Hanyu) hPlayerDamager.getRole();
            else hanyu = (Hanyu) hPlayer.getRole();

            if(hanyu.isInvisible){
                hanyu.setInvisible(false);

                for(Player players : Bukkit.getOnlinePlayers()){
                    players.showPlayer(player);
                    new HanyuTaskInvisble().runTask(1000,1000);
                }
            }

        }


    }



    public void setInvisible(boolean invisible) {
        isInvisible = invisible;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public boolean isDimensionIsUsed() {
        return dimensionIsUsed;
    }

    public void setDimensionIsUsed(boolean dimensionIsUsed) {
        this.dimensionIsUsed = dimensionIsUsed;
    }

    public Map<HPlayer, Location> getDimensionLastLoc() {
        return dimensionLastLoc;
    }

    @Override
    public String getDecription() {
        return "null";
    }

    @Override
    public void onKill(HPlayer killer, HPlayer killed, DeathReason dr) {

    }

    @Override
    public void onDeath(HPlayer killed, DeathReason dr) {

    }

    @Override
    public void playerLeave(Player p) {

    }

    @Override
    public boolean acceptReconnect(Player p) {
        return false;
    }
}
