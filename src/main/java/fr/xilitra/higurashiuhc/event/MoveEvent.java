package fr.xilitra.higurashiuhc.event;

import fr.xilitra.higurashiuhc.HigurashiUHC;
import fr.xilitra.higurashiuhc.game.GameStates;
import fr.xilitra.higurashiuhc.game.clans.hinamizawa.MemberOfClub;
import fr.xilitra.higurashiuhc.game.task.DeathTask;
import fr.xilitra.higurashiuhc.player.HPlayer;
import fr.xilitra.higurashiuhc.roles.Role;
import fr.xilitra.higurashiuhc.roles.hinamizawa.memberofclub.SatokoHojo;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MoveEvent implements Listener {

    @EventHandler
    public void onMoveEvent(PlayerMoveEvent e){
        Player p = e.getPlayer();

        HPlayer hPlayer = HigurashiUHC.getGameManager().getPlayer(p.getUniqueId());

        HPlayer rika = null;

        if(HigurashiUHC.getGameManager().getStates() != GameStates.GAME) return;

        for(HPlayer player : HigurashiUHC.getGameManager().getPlayers().values()){
            if(player.getRole().getClass().equals(Role.RIKA_FURUDE.getRole())){
                rika = player;
            }
        }

        for(MemberOfClub.roleList roleList : MemberOfClub.roleList.values()){
            if(roleList.getRole().equals(hPlayer.getRole().getClass())){

                if(rika != null){
                    if (p.getLocation().distanceSquared(rika.getPlayer().getLocation()) < 20 * 20) {

                        if(rika.getPlayer().getGameMode() != GameMode.SPECTATOR){
                            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 1, false));
                        }
                    }
                }
            }
        }

        if(hPlayer.getDeathTask() instanceof DeathTask){
           if(((DeathTask) hPlayer.getDeathTask()).isStarted()){
               e.setCancelled(true);
           }else {
               e.setCancelled(false);
           }
        }

        Location blockLoc = e.getTo().getBlock().getLocation();

        if(SatokoHojo.blockTraps.contains(blockLoc)){
            for(HPlayer hplayers : HigurashiUHC.getGameManager().getPlayers().values()){
                if(hplayers.getRole().getClass().equals(Role.SATOKO_HOJO.getRole())){
                    hplayers.getPlayer().sendMessage(p.getName() + " est " + hPlayer.getName());
                }
            }
            SatokoHojo.blockTraps.remove(blockLoc);
        }

    }

}
