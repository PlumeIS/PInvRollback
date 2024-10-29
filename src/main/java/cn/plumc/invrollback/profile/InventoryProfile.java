package cn.plumc.invrollback;

import com.google.gson.JsonObject;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class InventoryProfile {
    private final JsonObject helmet;
    private final JsonObject chestplate;
    private final JsonObject leggings;
    private final JsonObject boots;

    private final JsonObject offHand;
    private final int handSlot;

    private final JsonObject mainInventory;

    private InventoryProfile(JsonObject helmet, JsonObject chestplate, JsonObject leggings, JsonObject boots, JsonObject offHand, int handSlot, JsonObject mainInventory) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.offHand = offHand;
        this.handSlot = handSlot;
        this.mainInventory = mainInventory;
    }

    public static InventoryProfile parse(PlayerInventory inventory){
        JsonObject helmet = getItemAsJson(inventory.getHelmet());
        JsonObject chestplate = getItemAsJson(inventory.getChestplate());
        JsonObject leggings = getItemAsJson(inventory.getLeggings());
        JsonObject boots = getItemAsJson(inventory.getBoots());

        JsonObject offHand = getItemAsJson(inventory.getItemInOffHand());
        JsonObject mainInventory = new JsonObject();
        for (int i = 0; i < 36; i++) {
            mainInventory.add(String.valueOf(i), getItemAsJson(inventory.getItem(i)));
        }
        return new InventoryProfile(helmet, chestplate, leggings, boots, offHand, inventory.getHeldItemSlot(), mainInventory);
    }

    public static @Nullable JsonObject getItemAsJson(ItemStack item){
        if (item == null) return null;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("item", item.getType().getKey().toString());
        jsonObject.addProperty("data", new String(Base64.getEncoder().encode(item.serializeAsBytes()), StandardCharsets.UTF_8));
        return jsonObject;
    }

    public static @Nullable ItemStack getItemFromJson(JsonObject jsonObject){
        if (jsonObject == null) return null;
        return ItemStack.deserializeBytes()
    }

    public static InventoryProfile read(JsonObject inventory){
        return new InventoryProfile(inventory.getAsJsonObject("helmet"),
                inventory.getAsJsonObject("chestplate"),
                inventory.getAsJsonObject("leggings"),
                inventory.getAsJsonObject("boots"),
                inventory.getAsJsonObject("offHand"),
                inventory.get("handSlot").getAsInt(),
                inventory.get("mainInventory").getAsJsonObject());
    }

    public JsonObject serialize(){
        JsonObject jsonObject = new JsonObject();
        if (helmet!=null) jsonObject.add("helmet", helmet);
        if (chestplate!=null) jsonObject.add("chestplate", chestplate);
        if (leggings!=null) jsonObject.add("leggings", leggings);
        if (boots!=null) jsonObject.add("boots", boots);
        if (offHand!=null) jsonObject.add("offHand", offHand);
        jsonObject.addProperty("handSlot", handSlot);
        jsonObject.add("mainInventory", mainInventory);
        return jsonObject;
    }

    public void rollback(PlayerInventory inventory){
        inventory.clear();

    }
}
