package de.rgse.mc.playerbackup.service;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.player.Player;

public abstract class PlayerSerializer<T extends Player> {

    protected PlayerSerializer() {
    }

    protected abstract void deserializeNetworksideSpecific(T player, CompoundTag tag);

    public void deserialize(T player, CompoundTag tag) {
        ListTag inventory = (ListTag) tag.get("Inventory");

        int health = tag.getInt("Health");

        assert inventory != null;
        player.getInventory().load(inventory);
        player.setHealth(health);

        deserializeNetworksideSpecific(player, tag);
    }
}
