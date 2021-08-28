package fr.xilitra.higurashiuhc.roles.mercenaires;

import fr.xilitra.higurashiuhc.HigurashiUHC;
import fr.xilitra.higurashiuhc.roles.Role;
import fr.xilitra.higurashiuhc.event.higurashi.RoleSelected;
import fr.xilitra.higurashiuhc.game.Gender;
import fr.xilitra.higurashiuhc.game.clans.MercenaireClan;
import fr.xilitra.higurashiuhc.player.HPlayer;
import fr.xilitra.higurashiuhc.roles.RoleList;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class MiyoTakano extends Role implements Listener {

    private int order = 2;

    public MiyoTakano() {
        super("Miyo Takano", Gender.FEMME, MercenaireClan.getClans(), 1);
    }


    @EventHandler
    public void onRoleSelected(RoleSelected e){
        HPlayer player = e.getPlayer();

        if(player.getRole().equals(this)){

            RoleList[] mercenenaire = {RoleList.MERCENAIRE, RoleList.OKONOGI};

            for(HPlayer hPlayer : HigurashiUHC.getGameManager().getPlayerList().values()){
                for(RoleList role : mercenenaire){
                    if(hPlayer.getRole().equals(role.getRole())){

                        player.getPlayer().sendMessage(ChatColor.RED + "--Liste des Mercenaires--");
                        player.getPlayer().sendMessage(ChatColor.GREEN + hPlayer.getRole().getName() + " : " + ChatColor.GOLD + hPlayer.getName());

                    }
                }
            }

        }

    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public void onKill(EntityDamageEvent de, HPlayer killer, HPlayer killed) {

    }

    @Override
    public void onDeath(EntityDamageEvent de, HPlayer killed) {

    }
}
