package de.rgse.mc.playerbackup.model;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class PersistentPlayer {

    private final String uuid;

    private final CompoundNBT player;

    public PersistentPlayer(ServerPlayerEntity player) {
        uuid = player.getUUID().toString();

        this.player = new CompoundNBT();
        player.save(this.player);
        player.addAdditionalSaveData(this.player);
    }

    public String getUuid() {
        return uuid;
    }

    public CompoundNBT getPlayer() {
        return player;
    }
}
