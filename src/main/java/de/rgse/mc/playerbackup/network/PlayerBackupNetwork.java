package de.rgse.mc.playerbackup.network;

import de.rgse.mc.playerbackup.PlayerBackupMod;
import de.rgse.mc.playerbackup.network.message.PlayerBackupSFXMessage;
import de.rgse.mc.playerbackup.network.message.RestorePlayerMessage;
import de.rgse.mc.playerbackup.network.message.SyncAvailableBackupsMessage;
import de.rgse.mc.playerbackup.service.BackupService;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fmllegacy.network.NetworkRegistry;
import net.minecraftforge.fmllegacy.network.PacketDistributor;
import net.minecraftforge.fmllegacy.network.simple.SimpleChannel;
import net.minecraftforge.fmllegacy.server.ServerLifecycleHooks;

import java.util.List;
import java.util.UUID;

public class PlayerBackupNetwork {

    private static final String PROTOCOL_VERSION = "1.0.0";

    private PlayerBackupNetwork() {
    }

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PlayerBackupMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(0, RestorePlayerMessage.class, RestorePlayerMessage::encode, RestorePlayerMessage::decode, RestorePlayerMessage::handle);
        INSTANCE.registerMessage(1, PlayerBackupSFXMessage.class, PlayerBackupSFXMessage::encode, PlayerBackupSFXMessage::decode, PlayerBackupSFXMessage::handle);
        INSTANCE.registerMessage(2, SyncAvailableBackupsMessage.class, SyncAvailableBackupsMessage::encode, SyncAvailableBackupsMessage::decode, SyncAvailableBackupsMessage::handle);
    }

    public static void sendRestore(ServerPlayer serverPlayer) {
        CompoundTag compoundTag = new CompoundTag();
        serverPlayer.save(compoundTag);
        serverPlayer.addAdditionalSaveData(compoundTag);

        INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new RestorePlayerMessage(compoundTag));
    }

    public static void sendFX(ServerPlayer serverPlayer) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerBackupSFXMessage());
    }

    public static void sendSync() {
        List<String> backups = BackupService.instance().getBackups();
        INSTANCE.send(PacketDistributor.ALL.noArg(), new SyncAvailableBackupsMessage(backups));
    }

    public static void sendSync(String uuid) {
        ServerPlayer player = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayer(UUID.fromString(uuid));
        List<String> backups = BackupService.instance().getBackups();
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new SyncAvailableBackupsMessage(backups));
    }
}
