package fr.xilitra.higurashiuhc.game.task.taskClass;

import fr.xilitra.higurashiuhc.HigurashiUHC;
import fr.xilitra.higurashiuhc.game.task.BukkitTask;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;

public class GameTask extends BukkitTask {

    public int timeEpisode = HigurashiUHC.getInstance().getConfig().getInt("phase-time") * 60;
    public int worldborderActivation = HigurashiUHC.getInstance().getConfig().getInt("activation-time");
    public int time = HigurashiUHC.getInstance().getConfig().getInt("phase-time") * 60;

    @Override
    public void execute() {

        if (worldborderActivation == 0) {
            worldborderActivation--;
            World world = Bukkit.getWorld("world");

            WorldBorder border = world.getWorldBorder();
            double blockReduce = HigurashiUHC.getInstance().getConfig().getInt("wordborder-time-reduce");
            border.setSize(HigurashiUHC.getInstance().getConfig().getInt("worldborder") - blockReduce);
        } else if (worldborderActivation > 0)
            worldborderActivation--;

        if (time == 0) {
            time = timeEpisode;
            HigurashiUHC.getGameManager().setEpisode(HigurashiUHC.getGameManager().getEpisode() + 1);
        }

        time--;
    }
}
