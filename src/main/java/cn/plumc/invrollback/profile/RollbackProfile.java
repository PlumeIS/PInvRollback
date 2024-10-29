package cn.plumc.invrollback;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;

import java.util.UUID;

public class RollbackProfile {
    public final UUID player;
    public final String type;
    public final String reason;
    public final long time;
    public final long id;
    private final InventoryProfile inventory;

    public RollbackProfile(Player player, String type, String reason) {
        this.player = player.getUniqueId();
        this.type = type;
        this.reason = reason;
        this.time = System.currentTimeMillis();
        this.id = PInvRollback.rollbackManager.getNewId();
        this.inventory = InventoryProfile.parse(player.getInventory());
    }

    public RollbackProfile(JsonObject rollbackProfile){
        this.player = UUID.fromString(rollbackProfile.get("player").getAsString());
        this.type = rollbackProfile.get("type").getAsString();
        this.reason = rollbackProfile.get("reason").getAsString();
        this.time = rollbackProfile.get("time").getAsLong();
        this.id = rollbackProfile.get("id").getAsLong();
        this.inventory = InventoryProfile.read(rollbackProfile.get("inventory").getAsJsonObject());
    }

    public void rollback(Player serverPlayer){
        inventory.rollback(serverPlayer.getInventory());
    }

    public JsonObject toJson(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("player", player.toString());
        jsonObject.addProperty("type", type);
        jsonObject.addProperty("reason", reason);
        jsonObject.addProperty("time", time);
        jsonObject.addProperty("id", id);
        jsonObject.add("inventory", inventory.serialize());
        return jsonObject;
    }
}
