package fr.xilitra.higurashiuhc.event.higurashi;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EpisodeUpdate extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private int episode;


    public EpisodeUpdate(int episode) {
        this.episode = episode;
    }

    public void setEpisode(int episode) {
        this.episode = episode;
    }

    public int getEpisode() {
        return episode;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList(){
        return HANDLERS;
    }
}
