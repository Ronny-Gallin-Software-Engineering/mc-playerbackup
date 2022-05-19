package de.rgse.mc.playerbackup.network.message;

import de.rgse.mc.playerbackup.network.client.ClientPlayerBackupPacketHandler;
import dev.architectury.networking.NetworkManager;

import java.util.function.Supplier;

public class PlayerBackupSFXMessage {
    public static PlayerBackupSFXMessage decode() {
        return new PlayerBackupSFXMessage();
    }

    public void handle(Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();
        context.queue(ClientPlayerBackupPacketHandler::handlePlayerBackupSFXMessage);
    }
}
