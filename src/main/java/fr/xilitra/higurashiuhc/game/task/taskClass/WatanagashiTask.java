package fr.xilitra.higurashiuhc.game.task.taskClass;

import fr.xilitra.higurashiuhc.clans.Clans;
import fr.xilitra.higurashiuhc.game.task.BukkitTask;
import fr.xilitra.higurashiuhc.player.HPlayer;
import fr.xilitra.higurashiuhc.roles.Role;
import fr.xilitra.higurashiuhc.roles.hinamizawa.memberofclub.RikaFurudeAction;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class WatanagashiTask extends BukkitTask {
    @Override
    public void execute() {

        Role rf = Role.RIKA_FURUDE;
        if(rf.getHPlayer() == null)
            return;

        Player rikaPlayer = rf.getHPlayer().getPlayer();
        if(rikaPlayer == null)
            return;

        Location rikaLocation = rikaPlayer.getLocation();

        for(HPlayer hPlayer : Clans.MEMBER_OF_CLUB.getHPlayerList()){

            Player player = hPlayer.getPlayer();
            if(player == null || hPlayer.hasMalediction() || player.hasPotionEffect(PotionEffectType.DAMAGE_RESISTANCE))
                return;

            Location playerLocation = player.getLocation();

            if(playerLocation.getWorld().getName().equals(rikaLocation.getWorld().getName()))
                continue;

            double diffX = rikaLocation.getX()-playerLocation.getX();
            double diffY = rikaLocation.getY()-playerLocation.getY();
            double diffZ = rikaLocation.getY()-playerLocation.getZ();

            double rayon = Math.sqrt( diffX * diffX + diffY * diffY + diffZ * diffZ );

            if(rayon <= 20)
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20, 1));

        }

    }
}
