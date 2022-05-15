package de.rgse.mc.playerbackup.network.message;

import de.rgse.mc.playerbackup.network.client.ClientPlayerBackupPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerBackupSFXMessage {

    public static void encode(PlayerBackupSFXMessage message, FriendlyByteBuf buffer) {
    }

    public static PlayerBackupSFXMessage decode(FriendlyByteBuf buffer) {
        return new PlayerBackupSFXMessage();
    }

    public static void handle(PlayerBackupSFXMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientPlayerBackupPacketHandler::handlePlayerBackupSFXMessage));

        context.setPacketHandled(true);
    }
}
