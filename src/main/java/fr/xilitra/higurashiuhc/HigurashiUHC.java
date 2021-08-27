package fr.xilitra.higurashiuhc;

import fr.xilitra.higurashiuhc.command.*;
import fr.xilitra.higurashiuhc.command.DebugCmd;
import fr.xilitra.higurashiuhc.command.HigurashiCmd;
import fr.xilitra.higurashiuhc.command.RessuciteCmd;
import fr.xilitra.higurashiuhc.event.*;
import fr.xilitra.higurashiuhc.game.GameManager;
import fr.xilitra.higurashiuhc.gui.config.MapMenu;
import fr.xilitra.higurashiuhc.roles.hinamizawa.memberofclub.*;
import fr.xilitra.higurashiuhc.utils.CustomCraft;
import fr.xilitra.higurashiuhc.utils.packets.Scoreboard;
import me.lulu.datounms.model.biome.BiomeData;
import me.lulu.datounms.v1_8_R3.BiomeReplacer;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class HigurashiUHC extends JavaPlugin {

    private static HigurashiUHC instance;
    private static GameManager manager;
    private static Map<UUID, Scoreboard> scoreboardMap = new HashMap<>();

    @Override
    public void onEnable() {
        instance = this;
        manager = new GameManager();

        //register default config
        saveDefaultConfig();

        //registry
        registerEvents();
        registerCommands();

        //register custom craft
        CustomCraft.addRecipeBaseball();

        manager.config();

        BiomeReplacer biomeReplacer = new BiomeReplacer();
        biomeReplacer.swap(BiomeData.JUNGLE, BiomeData.FOREST);
        biomeReplacer.swap(BiomeData.OCEAN, BiomeData.FOREST);

    }

    private void registerEvents(){

        //general listener
        this.getServer().getPluginManager().registerEvents(new InteractListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(), this);
        this.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        this.getServer().getPluginManager().registerEvents(new PickupListener(), this);
        this.getServer().getPluginManager().registerEvents(new ConfigListener(), this);
        this.getServer().getPluginManager().registerEvents(new DamageListener(), this);
        this.getServer().getPluginManager().registerEvents(new CraftEvent(), this);
        this.getServer().getPluginManager().registerEvents(new MoveEvent(), this);
        this.getServer().getPluginManager().registerEvents(new DeathListener(), this);
        this.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        this.getServer().getPluginManager().registerEvents(new EpisodeListener(), this);
        this.getServer().getPluginManager().registerEvents(new MapMenu(), this);

    }

    private void registerCommands(){
        this.getCommand("ressucite").setExecutor(new RessuciteCmd());
        this.getCommand("h").setExecutor(new HigurashiCmd());
        this.getCommand("debug").setExecutor(new DebugCmd());
        this.getCommand("vote").setExecutor(new VoteCmd());
        this.getCommand("coupable").setExecutor(new CoupableCmd());
    }

    public static HigurashiUHC getInstance(){
        return instance;
    }

    public static GameManager getGameManager(){
        return manager;
    }

    public static Map<UUID, Scoreboard> getScoreboardMap(){
        return scoreboardMap;
    }

    public static void addScoreboard(UUID uuid, Scoreboard scoreboard){
        scoreboardMap.put(uuid, scoreboard);
    }

    @Override
    public void onDisable() {
        for(Player p : Bukkit.getOnlinePlayers()){
            p.getInventory().clear();
            p.kickPlayer("Redémarrage du serveur.");
        }

        WorldBorder worldBorder = Bukkit.getWorld("world").getWorldBorder();
        worldBorder.reset();
    }
}
