package de.rgse.mc.playerbackup.network.message;

import de.rgse.mc.playerbackup.network.client.ClientPlayerBackupPacketHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class RestorePlayerMessage {

    private final CompoundNBT player;

    public RestorePlayerMessage(CompoundNBT player) {
        this.player = player;
    }

    public CompoundNBT getPlayer() {
        return player;
    }

    public static void encode(RestorePlayerMessage message, PacketBuffer buffer) {
        buffer.writeNbt(message.player);
    }

    public static RestorePlayerMessage decode(PacketBuffer buffer) {
        CompoundNBT compoundNBT = buffer.readNbt();
        return new RestorePlayerMessage(compoundNBT);
    }

    public static void handle(RestorePlayerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientPlayerBackupPacketHandler.handleRestorePlayerMessage(message));
        context.setPacketHandled(true);
    }
}
