package de.rgse.mc.playerbackup.network.message;

import de.rgse.mc.playerbackup.sound.SoundList;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.function.Supplier;

public class PlayerBackupSFXMessage {

    private final String playerUuid;

    public PlayerBackupSFXMessage(String playerUuid) {
        this.playerUuid = playerUuid;
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
        context.enqueueWork(() ->
                // Make sure it's only executed on the physical client
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> executeOnClient(message))
        );

        context.setPacketHandled(true);
    }

    private static void executeOnClient(PlayerBackupSFXMessage message) {
        UUID uuid = UUID.fromString(message.playerUuid);
        PlayerEntity playerByUUID = Minecraft.getInstance().level.getPlayerByUUID(uuid);

        Minecraft.getInstance().level.playSound(Minecraft.getInstance().player, playerByUUID, SoundList.RESTORE_SOUND.get(), SoundCategory.PLAYERS, 1.0f, 1f);
    }
}
