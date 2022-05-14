package de.rgse.mc.playerbackup.network.client;

import de.rgse.mc.playerbackup.network.message.RestorePlayerMessage;
import de.rgse.mc.playerbackup.network.message.SyncAvailableBackupsMessage;
import de.rgse.mc.playerbackup.service.client.Backups;
import de.rgse.mc.playerbackup.service.client.ClientPlayerSerializer;
import de.rgse.mc.playerbackup.sound.SoundList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class ClientPlayerBackupPacketHandler {

    private ClientPlayerBackupPacketHandler() {
    }

    public static void handleRestorePlayerMessage(RestorePlayerMessage message) {
        ClientPlayerSerializer.instance().deserialize(Minecraft.getInstance().player, message.getPlayer());
    }

    public static void handlePlayerBackupSFXMessage() {
        LocalPlayer player = Minecraft.getInstance().player;
        player.playSound(SoundList.RESTORE_SOUND.get(), 0.8F, 0.8F + player.level.random.nextFloat() * 0.4F);
    }

    public static void handleSyncBackupsMessage(SyncAvailableBackupsMessage message) {
        Backups.instance().setAvailableBackups(message.getBackups());
    }
}
