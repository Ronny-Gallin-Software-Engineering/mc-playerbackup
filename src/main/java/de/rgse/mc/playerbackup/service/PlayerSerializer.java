package de.rgse.mc.playerbackup.service;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

public abstract class PlayerSerializer<T extends PlayerEntity> {

    protected PlayerSerializer() {
    }

    protected abstract void deserializeNetworksideSpecific(T player, CompoundNBT tag);

    public void deserialize(T player, CompoundNBT tag) {
        ListNBT inventory = (ListNBT) tag.get("Inventory");

        int health = tag.getInt("Health");

        assert inventory != null;
        player.inventory.load(inventory);
        player.setHealth(health);

        deserializeNetworksideSpecific(player, tag);
    }
}
