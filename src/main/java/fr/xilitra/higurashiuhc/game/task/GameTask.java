package fr.xilitra.higurashiuhc.game.task;

import fr.xilitra.higurashiuhc.HigurashiUHC;
import fr.xilitra.higurashiuhc.player.HPlayer;
import fr.xilitra.higurashiuhc.roles.Role;
import fr.xilitra.higurashiuhc.roles.police.KuraudoOishi;
import fr.xilitra.higurashiuhc.utils.TimeUtils;
import fr.xilitra.higurashiuhc.utils.packets.Scoreboard;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.TimerTask;
import java.util.UUID;

public class GameTask extends TimerTask {

    private int time = 0;
    private int decount = 3;


    @Override
    public void run() {

        String formatTime = TimeUtils.formatTime(time);
        int player_remaining = HigurashiUHC.getGameManager().getPlayers().size() - HigurashiUHC.getGameManager().getSpectator().size();

        for(Map.Entry<UUID, Scoreboard> scoreboard : HigurashiUHC.getScoreboardMap().entrySet()){
            scoreboard.getValue().setLines(
                    "",
                    ChatColor.GRAY + "Joueur restant : " + ChatColor.GOLD + player_remaining,
                    "",
                    ChatColor.RED + "Titre de game",
                    "",
                    ChatColor.GRAY + "Temps de la partie : " + ChatColor.GOLD + formatTime,
                    "",
                    ChatColor.GRAY + "Episode : " + ChatColor.GOLD + HigurashiUHC.getGameManager().getEpisode(),
                    "",
                    ChatColor.DARK_PURPLE + "Okami Servers"
            );

            HigurashiUHC.addScoreboard(scoreboard.getKey(), scoreboard.getValue());
            decount--;
        }

        if(HigurashiUHC.getGameManager().getEpisode() >= 6){
            HPlayer player = HigurashiUHC.getGameManager().getPlayerWithRole(Role.KURAUDO_OISHI);

            if(player != null){

                KuraudoOishi oishi = (KuraudoOishi) player.getRole();

                if(!oishi.isCoupableIsDesigned()){
                    player.getPlayer().setMaxHealth(2.5);
                }

            }

        }

        int timePhase = HigurashiUHC.getInstance().getConfig().getInt("phase-time");

        timePhase = timePhase * 60;

        if(time == timePhase){
            HigurashiUHC.getGameManager().setEpisode(HigurashiUHC.getGameManager().getEpisode() + 1);
        }
        time++;
    }
}
