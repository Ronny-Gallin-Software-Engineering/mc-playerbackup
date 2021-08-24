package de.rgse.mc.playerbackup.network.message;

import de.rgse.mc.playerbackup.network.client.ClientPlayerBackupPacketHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class PlayerBackupSFXMessage {

    private final String playerUuid;

    public PlayerBackupSFXMessage(String playerUuid) {
        this.playerUuid = playerUuid;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public static void encode(PlayerBackupSFXMessage message, PacketBuffer buffer) {
        buffer.writeCharSequence(message.playerUuid, StandardCharsets.UTF_8);
    }

    public static PlayerBackupSFXMessage decode(PacketBuffer buffer) {
        String uuid = buffer.readCharSequence(buffer.readableBytes(), StandardCharsets.UTF_8).toString();
        return new PlayerBackupSFXMessage(uuid);
    }

    public static void handle(PlayerBackupSFXMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPlayerBackupPacketHandler.handlePlayerBackupSFXMessage(message)));

        context.setPacketHandled(true);
    }


}
