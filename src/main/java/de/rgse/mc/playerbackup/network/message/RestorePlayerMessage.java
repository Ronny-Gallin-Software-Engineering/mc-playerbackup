package de.rgse.mc.playerbackup.network.message;

import de.rgse.mc.playerbackup.network.client.ClientPlayerBackupPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RestorePlayerMessage {

    private final CompoundTag player;

    public RestorePlayerMessage(CompoundTag player) {
        this.player = player;
    }

    public CompoundTag getPlayer() {
        return player;
    }

    public static void encode(RestorePlayerMessage message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.player);
    }

    public static RestorePlayerMessage decode(FriendlyByteBuf buffer) {
        CompoundTag CompoundTag = buffer.readNbt();
        return new RestorePlayerMessage(CompoundTag);
    }

    public static void handle(RestorePlayerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> ClientPlayerBackupPacketHandler.handleRestorePlayerMessage(message));
        context.setPacketHandled(true);
    }
}
