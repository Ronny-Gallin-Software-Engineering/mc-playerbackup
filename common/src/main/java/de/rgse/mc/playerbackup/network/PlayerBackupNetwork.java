package de.rgse.mc.playerbackup.network;

import de.rgse.mc.playerbackup.PlayerbackupMod;
import de.rgse.mc.playerbackup.network.message.PlayerBackupSFXMessage;
import de.rgse.mc.playerbackup.network.message.RestorePlayerMessage;
import de.rgse.mc.playerbackup.network.message.SyncAvailableBackupsMessage;
import de.rgse.mc.playerbackup.service.BackupService;
import de.rgse.mc.playerbackup.service.PlayerList;
import dev.architectury.networking.NetworkChannel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

public class PlayerBackupNetwork {

    private PlayerBackupNetwork() {
    }

    public static final NetworkChannel INSTANCE = NetworkChannel.create(new ResourceLocation(PlayerbackupMod.MOD_ID, "main"));

    public static void init() {
        INSTANCE.register(RestorePlayerMessage.class, RestorePlayerMessage::encode, RestorePlayerMessage::decode, RestorePlayerMessage::handle);
        INSTANCE.register(PlayerBackupSFXMessage.class, (m, b) -> {
        }, b -> PlayerBackupSFXMessage.decode(), PlayerBackupSFXMessage::handle);
        INSTANCE.register(SyncAvailableBackupsMessage.class, SyncAvailableBackupsMessage::encode, SyncAvailableBackupsMessage::decode, SyncAvailableBackupsMessage::handle);
    }

    public static void sendRestore(ServerPlayer serverPlayer) {
        CompoundTag compoundTag = new CompoundTag();
        serverPlayer.save(compoundTag);
        serverPlayer.addAdditionalSaveData(compoundTag);

        INSTANCE.sendToPlayer(serverPlayer, new RestorePlayerMessage(compoundTag));
    }

    public static void sendFX(ServerPlayer serverPlayer) {
        INSTANCE.sendToPlayer(serverPlayer, new PlayerBackupSFXMessage());
    }

    public static void sendSync() {
        List<String> backups = BackupService.instance().getBackups();
        INSTANCE.sendToPlayers(PlayerList.getInstance().getPlayers(), new SyncAvailableBackupsMessage(backups));
    }

    public static void sendSync(ServerPlayer player) {
        List<String> backups = BackupService.instance().getBackups();
        INSTANCE.sendToPlayer(player, new SyncAvailableBackupsMessage(backups));
    }
}
