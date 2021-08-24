package de.rgse.mc.playerbackup.network.client;

import de.rgse.mc.playerbackup.network.message.PlayerBackupSFXMessage;
import de.rgse.mc.playerbackup.network.message.RestorePlayerMessage;
import de.rgse.mc.playerbackup.service.client.ClientPlayerSerializer;
import de.rgse.mc.playerbackup.sound.SoundList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class ClientPlayerBackupPacketHandler {

    private ClientPlayerBackupPacketHandler() {
    }

    public static void handleRestorePlayerMessage(RestorePlayerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        ClientPlayerSerializer.instance().deserialize(Minecraft.getInstance().player, message.getPlayer());
    }

    public static void handlePlayerBackupSFXMessage(PlayerBackupSFXMessage message) {
        UUID uuid = UUID.fromString(message.getPlayerUuid());
        PlayerEntity playerByUUID = Minecraft.getInstance().level.getPlayerByUUID(uuid);

        Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, playerByUUID, SoundList.RESTORE_SOUND.get(), SoundCategory.PLAYERS, 1.0f, 1f);
    }
}
