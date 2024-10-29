package cn.plumc.pInvRollback.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerInventorySaveEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public PlayerInventorySaveEvent(Player player, String reason) {

    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
