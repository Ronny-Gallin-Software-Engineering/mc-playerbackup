package de.rgse.mc.playerbackup.service;

import de.rgse.mc.playerbackup.exceptions.FileReadException;
import de.rgse.mc.playerbackup.exceptions.FileWriteException;
import de.rgse.mc.playerbackup.exceptions.NoBackupFoundException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;

public class ServerPlayerSerializer extends PlayerSerializer<ServerPlayer> {

    private static ServerPlayerSerializer instance;

    public static ServerPlayerSerializer instance() {
        if (null == instance) {
            instance = new ServerPlayerSerializer();
        }

        return instance;
    }

    private ServerPlayerSerializer() {
    }

    public void serialize(ServerPlayer player) throws FileWriteException {
        String uuid = player.getStringUUID();

        CompoundTag tag = new CompoundTag();
        player.save(tag);
        player.addAdditionalSaveData(tag);

        FileHandler.instance().writeFile(uuid, tag);
    }

    public CompoundTag deserialize(ServerPlayer player, String backupDate) throws FileReadException {
        return FileHandler.instance().readFile(player.getUUID().toString(), backupDate);
    }

    public CompoundTag deserialize(ServerPlayer player) throws NoBackupFoundException, FileReadException {
        return FileHandler.instance().readLatestFile(player.getUUID().toString());
    }

    protected void deserializeNetworksideSpecific(ServerPlayer player, CompoundTag tag) {
        int xpLevel = tag.getInt("XpLevel");
        int xpTotal = tag.getInt("XpTotal");

        player.setExperiencePoints(xpTotal);
        player.setExperienceLevels(xpLevel);
    }
}
