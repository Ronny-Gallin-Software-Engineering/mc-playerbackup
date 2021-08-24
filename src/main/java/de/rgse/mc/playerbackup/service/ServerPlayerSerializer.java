package de.rgse.mc.playerbackup.service;

import de.rgse.mc.playerbackup.exceptions.FileReadException;
import de.rgse.mc.playerbackup.exceptions.FileWriteException;
import de.rgse.mc.playerbackup.exceptions.NoBackupFoundException;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

import java.util.List;

public class ServerPlayerSerializer extends PlayerSerializer<ServerPlayerEntity> {

    private static ServerPlayerSerializer instance;

    public static ServerPlayerSerializer instance() {
        if (null == instance) {
            instance = new ServerPlayerSerializer();
        }

        return instance;
    }

    private ServerPlayerSerializer() {
    }

    public List<String> getBackupsForPlayer(ServerPlayerEntity player) {
        return FileHandler.instance().getBackupTimeStamps(player.getUUID().toString());
    }

    public void serialize(ServerPlayerEntity player) throws FileWriteException {
        String uuid = player.getStringUUID();

        CompoundNBT tag = new CompoundNBT();
        player.save(tag);
        player.addAdditionalSaveData(tag);

        FileHandler.instance().writeFile(uuid, tag);
    }

    public CompoundNBT deserialize(ServerPlayerEntity player, String backupDate) throws FileReadException {
        return FileHandler.instance().readFile(player.getUUID().toString(), backupDate);
    }

    public CompoundNBT deserialize(ServerPlayerEntity player) throws NoBackupFoundException, FileReadException {
        return FileHandler.instance().readLatestFile(player.getUUID().toString());
    }

    protected void deserializeNetworksideSpecific(ServerPlayerEntity player, CompoundNBT tag) {
        int xpLevel = tag.getInt("XpLevel");
        int xpTotal = tag.getInt("XpTotal");

        player.setExperiencePoints(xpTotal);
        player.setExperienceLevels(xpLevel);
    }
}
