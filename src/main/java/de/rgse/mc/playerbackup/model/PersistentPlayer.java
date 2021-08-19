package de.rgse.mc.playerbackup.model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class PersistentPlayer {

    @Expose
    private String uuid;
    @Expose
    private JsonArray inventory;
    @Expose
    private int totalExperience;

    public PersistentPlayer(ServerPlayerEntity player) {
        Gson gson = new Gson();

        uuid = player.getUUID().toString();
        totalExperience = player.totalExperience;
        inventory = new JsonArray();

        LazyOptional<IItemHandler> capability = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        capability.ifPresent(ih -> {
            for (int i = 0; i < ih.getSlots(); i++) {
                ItemStack stackInSlot = ih.getStackInSlot(i);
                CompoundNBT compoundNBT = stackInSlot.serializeNBT();

                JsonObject jsonObject = gson.fromJson(compoundNBT.toString(), JsonObject.class);
                inventory.add(jsonObject);
            }
        });
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public JsonArray getInventory() {
        return inventory;
    }

    public void setInventory(JsonArray inventory) {
        this.inventory = inventory;
    }

    public int getTotalExperience() {
        return totalExperience;
    }

    public void setTotalExperience(int totalExperience) {
        this.totalExperience = totalExperience;
    }
}
