package de.rgse.mc.playerbackup.network.message;

import de.rgse.mc.playerbackup.network.client.ClientPlayerBackupPacketHandler;
import me.shedaniel.architectury.networking.NetworkManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

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
        CompoundTag compoundTag = buffer.readNbt();
        return new RestorePlayerMessage(compoundTag);
    }

    public void handle(Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext packetContext = contextSupplier.get();
        packetContext.queue(() -> ClientPlayerBackupPacketHandler.handleRestorePlayerMessage(this));
    }
}
