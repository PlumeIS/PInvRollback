package cn.plumc.invrollback.listeners;

import cn.plumc.invrollback.PInvRollback;
import cn.plumc.invrollback.events.PInvRollbackShouldSaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class RollbackListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    public void onRollbackSaving(PInvRollbackShouldSaveEvent event) {
        if (event.isCancelled()) {return;}
        PInvRollback.rollbackManager.create(event.getPlayer(), event.getType(), event.getMessage(), event.getMaxProfiles());
    }
}
