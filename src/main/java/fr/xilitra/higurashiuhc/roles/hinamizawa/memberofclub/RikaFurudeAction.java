package fr.xilitra.higurashiuhc.roles.hinamizawa.memberofclub;

import fr.xilitra.higurashiuhc.HigurashiUHC;
import fr.xilitra.higurashiuhc.clans.Clans;
import fr.xilitra.higurashiuhc.event.higurashi.EpisodeUpdate;
import fr.xilitra.higurashiuhc.event.higurashi.RoleSelected;
import fr.xilitra.higurashiuhc.event.watanagashi.WatanagashiChangeEvent;
import fr.xilitra.higurashiuhc.game.PlayerState;
import fr.xilitra.higurashiuhc.game.task.taskClass.CloseRikaTask;
import fr.xilitra.higurashiuhc.player.HPlayer;
import fr.xilitra.higurashiuhc.roles.Role;
import fr.xilitra.higurashiuhc.roles.RoleAction;
import fr.xilitra.higurashiuhc.utils.DeathReason;
import fr.xilitra.higurashiuhc.utils.HideNametag;
import fr.xilitra.higurashiuhc.utils.WataEnum;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class RikaFurudeAction implements RoleAction, Listener {

    private final CloseRikaTask watanagashiTask = new CloseRikaTask();
    private int lives = 3;
    private boolean ressucite = false;

    private int deathMalusTime = 2;

    @Override
    public Role getLinkedRole(){
        return Role.RIKA_FURUDE;
    }

    @Override
    public String getDescription() {
        return
                "§6Vous êtes §9Rika Furude (fille) :\n\n" +
                        "§9Rika §6doit gagner avec §9Hinamizawa §6tout en faisant partie du §bClub§9.\n" +
                        "§6Elle possède 8 cœurs ainsi que 3 vies et connaît l’identité de Hanyu.\n" +
                        "§6Une fois à sa deuxième vie, Rika verra les pseudos des joueurs brouillés jusqu’à la fin de la partie.\n" +
                        "§6Lors de sa dernière vie, elle n’aura plus que 5 cœurs permanents.\n" +
                        "§6Une fois dans la partie, §9Rika§6 peut sacrifier une de ses vies pour ressusciter l’un de ses amis morts (Keiichi/Satoko/Mion/Shion/Rena).\n" +
                        "§6Si §9Hanyu§6 vient à mourir, §9Rika§6 n’aura plus la possibilité de ressusciter et mourra définitivement si elle vient à se faire tuer.\n" +
                        "§6Si §9Rika§6 meurt définitivement, alors §9Hinamizawa§6 n’aura que 2 jours pour gagner la partie sinon les membres du camp seront touchés par l'effet weakness permanent.\n" +
                        "§6Si elle se fait tuer avant Watanagashi de la main des mercenaires, §4Miyo Takano§6 sera dévoilé aux yeux de tous.\n" +
                        "§6Après Watanagashi, si l’un des membres du club se trouve à côté de Rika dans un rayon de 20 blocs, il gagne l’effet résistance.\n\n" +
                        "§9Hanyu §6est §7'joueur'";
    }

    @EventHandler
    public void onRoleSelected(RoleSelected event) {

        HPlayer player = event.getPlayer();
        Player bPlayer = player.getPlayer();

        if (bPlayer != null && player.getRole().isRole(Role.RIKA_FURUDE)) {
            bPlayer.setHealth(16);
            bPlayer.setMaxHealth(16);
        }

    }

    @EventHandler
    public void onWataChange(WatanagashiChangeEvent event) {
        if (event.getWataEnum() == WataEnum.AFTER)
            this.watanagashiTask.runTaskTimer(1, 1);
        else
            this.watanagashiTask.stopTask();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        HPlayer hPlayer = HigurashiUHC.getGameManager().getHPlayer(p.getUniqueId());

        if (hPlayer == null) return;

        if (hPlayer.getRole().isRole(Role.RIKA_FURUDE)) {
            HPlayer hanyu = Role.HANYU.getHPlayer();

            if (hanyu == null || hanyu.getPlayer() == null) return;

            HanyuAction hanyuActionRole = (HanyuAction) hanyu.getRole().getRoleAction();

            Location hanyuLocation = hanyu.getPlayer().getLocation();
            Location rikaLocation = p.getLocation();

            if (hanyuLocation.distanceSquared(rikaLocation) < 30 * 30) {

                if (hanyuActionRole.isInvisible()) return;

                hanyuActionRole.setInvisible(true);

                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.hidePlayer(p);
                }

            } else {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    players.showPlayer(p);
                }
            }
        }

    }

    @EventHandler
    public void onEpisodeUpdate(EpisodeUpdate episodeUpdate){

        HPlayer rika = this.getLinkedRole().getHPlayer();
        if(rika == null)
            return;
        if(rika.getPlayerState().isState(PlayerState.SPECTATE, PlayerState.DISCONNECTED) && deathMalusTime>=1){
            deathMalusTime-=1;
            if(deathMalusTime == 0){
                Bukkit.broadcastMessage("§5Cela fait maintenant 2 jours que §9Rika §5est décédée.  \n" +
                        "\n" +
                        "§5Tous les membres du village d’§9Hinamizawa §5possèdent maintenant l’effet weakness permanent. ");
                for (HPlayer hPlayer : HigurashiUHC.getGameManager().getHPlayerList().values()) {
                    if (hPlayer.getPlayer() != null && hPlayer.getClans().isClans(Clans.HINAMIZAWA)) {
                        hPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 1, true, false));
                        hPlayer.getPlayer().playSound(hPlayer.getPlayer().getLocation(), Sound.GHAST_CHARGE, 5, 5);
                    }

                    ((SatokoHojoAction) Role.SATOKO_HOJO.getRoleAction()).removeTraps(hPlayer);
                }
            }else{
                Bukkit.broadcastMessage("§5Il ne reste plus que " + deathMalusTime + " jour(s) au village pour remporter la partie.");
            }
        }

    }

    public void resurrection(HPlayer rikaFurudePlayer, HPlayer resuPlayer) {
        Player rika = rikaFurudePlayer.getPlayer();
        Player target = resuPlayer.getPlayer();

        if (rika == null || target == null) return;

        if (!resuPlayer.getRole().isRole(Role.SATOKO_HOJO, Role.KEIICHI_MAEBARA, Role.MION_SONOZAKI, Role.SHION_SONOSAKI, Role.RENA_RYUGU))
            return;

        if (Role.HANYU.getHPlayer() != null && !Role.HANYU.getHPlayer().getPlayerState().isState(PlayerState.WAITING_DEATH, PlayerState.SPECTATE)) {
            if (resuPlayer.getPlayerState().isState(PlayerState.WAITING_DEATH)) {
                if (rikaFurudePlayer.getRole().isRole(Role.RIKA_FURUDE)) {
                    RikaFurudeAction rikaFurudeAction = (RikaFurudeAction) rikaFurudePlayer.getRole().getRoleAction();

                    if (rikaFurudeAction.getLives() <= 1) {
                        rika.sendMessage("Vous n'avez pas assez de vie pour réssuciter " + target.getName());
                        return;
                    }

                    rikaFurudeAction.remove1Live();
                    resuPlayer.setPlayerState(PlayerState.INGAME);
                    target.setGameMode(GameMode.SURVIVAL);
                    target.setHealth(target.getMaxHealth());
                    ressucite = true;
                }
            }
        }
    }

    public boolean getRessucite() {
        return ressucite;
    }

    public void remove1Live() {
        this.lives--;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    @Override
    public void onKill(HPlayer killer, HPlayer killed, DeathReason dr) {
        HPlayer miyo = Role.MIYO_TAKANO.getHPlayer();

        if (miyo != null) {

            Bukkit.broadcastMessage(miyo.getName() + " est " + miyo.getName());

        }
    }

    @Override
    public void onDeath(HPlayer killed, DeathReason dr) {

        if (killed.getPlayer() == null)
            return;

        Entity killer = killed.getKiller();
        HPlayer killerHPlayer = null;
        Player player = killed.getPlayer();

        if (killer != null)
            killerHPlayer = HigurashiUHC.getGameManager().getHPlayer(killer.getUniqueId());

        if (killer instanceof Player) {

            if (killerHPlayer != null && HigurashiUHC.getGameManager().isWataState(WataEnum.BEFORE)) {
                if (killerHPlayer.getClans().isClans(Clans.MERCENAIRE)) {
                    for (HPlayer miyo : HigurashiUHC.getGameManager().getHPlayerList().values()) {
                        if (miyo.getRole().isRole(Role.MIYO_TAKANO)) {
                            Bukkit.broadcastMessage("Miyo Takano est " + miyo.getName());
                        }
                    }
                }
            }

            if (killed.getRole().isRole(Role.RIKA_FURUDE)) {
                RikaFurudeAction rikaFurudeAction = (RikaFurudeAction) killed.getRole().getRoleAction();

                rikaFurudeAction.remove1Live();

                if (rikaFurudeAction.getLives() == 2) {
                    killed.getPlayer().setMaxHealth(16);
                    killed.getPlayer().setHealth(16);
                }

                if (rikaFurudeAction.getLives() == 1) {

                    killed.getPlayer().setMaxHealth(10);
                    killed.getPlayer().setHealth(10);

                    for (HPlayer players : HigurashiUHC.getGameManager().getHPlayerList().values()) {
                        HideNametag.unhide(killed.getPlayer(), players.getPlayer());
                    }
                }

                if (rikaFurudeAction.getLives() == 0) {

                    player.sendMessage("§7Vous venez de perdre une de vos vies. §5Vous n’avez plus de vie.");

                    if (player.getPlayer().getInventory().getContents().length > 0) {
                        for (ItemStack itemStack : player.getInventory().getContents()) {
                            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                            player.getInventory().removeItem(itemStack);
                        }
                    }

                    if (player.getPlayer().getInventory().getArmorContents().length > 0) {
                        for (ItemStack itemStack : player.getInventory().getArmorContents()) {
                            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                            player.getInventory().removeItem(itemStack);
                        }
                    }

                    for (HPlayer players : HigurashiUHC.getGameManager().getHPlayerList().values()) {

                        if (players.getPlayer() != null)
                            players.getPlayer().playSound(players.getPlayer().getLocation(), Sound.ENDERDRAGON_DEATH, 5, 5);

                    }

                }else
                    player.sendMessage("§7Vous venez de perdre une de vos vies. §5Il vous reste " + rikaFurudeAction.getLives() + " vies.");

            }

        }

        HPlayer hanyu = Role.HANYU.getHPlayer();

        if (hanyu == null) return;

        if (hanyu.getPlayerState() == PlayerState.INGAME) {

            TextComponent textClick = new TextComponent(ChatColor.GOLD + "[Oui]");
            textClick.setBold(true);
            TextComponent message = new TextComponent("Voulez vous teleporter Rika dans la dimension : ");
            message.addExtra(textClick);

            textClick.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "h dimension rika"));

        }

    }

    @Override
    public void onLeaveRole(HPlayer hPlayer) {

    }

    @Override
    public void onJoinRole(HPlayer hPlayer) {

    }

    @Override
    public void onGameStart() {

        HPlayer hanyu = Role.HANYU.getHPlayer();
        HPlayer rika = this.getLinkedRole().getHPlayer();
        if (hanyu != null && rika != null && rika.getPlayer() != null) {
            rika.getPlayer().sendMessage("Hanyu est incarnée par: " + hanyu.getName());
        }

    }

    @Override
    public void onGameStop() {

    }

    @Override
    public void playerLeave(Player p) {

    }

    @Override
    public boolean acceptReconnect(Player p) {
        return false;
    }
}
