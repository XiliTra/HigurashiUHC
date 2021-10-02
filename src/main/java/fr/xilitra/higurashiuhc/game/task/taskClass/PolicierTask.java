package fr.xilitra.higurashiuhc.game.task.taskClass;

import fr.xilitra.higurashiuhc.game.task.JavaTask;
import fr.xilitra.higurashiuhc.player.HPlayer;

public class PolicierTask extends JavaTask {

    private int time = 10 * 60;
    private final HPlayer hPlayer;

    public PolicierTask(HPlayer hPlayer){
        this.hPlayer = hPlayer;
    }

    @Override
    public void execute() {

        if(time == 0){

            hPlayer.getInfoData().setDataInfo("PvIsUsed", false);
            this.stopTask();

        }
        time--;
    }
}
