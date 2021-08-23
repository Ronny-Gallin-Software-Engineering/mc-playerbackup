package de.rgse.mc.playerbackup.service;

import de.rgse.mc.playerbackup.exceptions.FileReadException;
import de.rgse.mc.playerbackup.exceptions.FileWriteException;
import de.rgse.mc.playerbackup.exceptions.NoBackupFoundException;
import de.rgse.mc.playerbackup.model.PersistentPlayer;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

import java.util.List;

public class PlayerSerializer {

    private static PlayerSerializer instance;

    public static PlayerSerializer instance() {
        if (null == instance) {
            instance = new PlayerSerializer();
        }

        return instance;
    }

    private PlayerSerializer() {
    }

    public List<String> getBackupsForPlayer(ServerPlayerEntity player) {
        return FileHandler.instance().getBackupTimeStamps(player.getUUID().toString());
    }

    public void serialize(PersistentPlayer player) throws FileWriteException {
        FileHandler.instance().writeFile(player.getUuid(), player.getPlayer());
    }

    public CompoundNBT deserialize(ServerPlayerEntity player, String backupDate) throws FileReadException {
        return FileHandler.instance().readFile(player.getUUID().toString(), backupDate);
    }

    public CompoundNBT deserialize(ServerPlayerEntity player) throws NoBackupFoundException, FileReadException {
        return FileHandler.instance().readLatestFile(player.getUUID().toString());
    }

    public void deserialize(PlayerEntity player, CompoundNBT tag) {
        ListNBT inventory = (ListNBT) tag.get("Inventory");

        int xpLevel = tag.getInt("XpLevel");
        int xpTotal = tag.getInt("XpTotal");
        int xpProgress = tag.getInt("XpP");
        int health = tag.getInt("Health");

        assert inventory != null;
        player.inventory.load(inventory);
        player.setHealth(health);

        if (player instanceof ClientPlayerEntity) {
            ((ClientPlayerEntity) player).setExperienceValues(xpProgress, xpTotal, xpLevel);

        } else if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            serverPlayer.setExperiencePoints(xpTotal);
            serverPlayer.setExperienceLevels(xpLevel);
        }
    }
}
