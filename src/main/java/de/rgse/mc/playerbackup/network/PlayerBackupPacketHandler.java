package de.rgse.mc.playerbackup.network;

import de.rgse.mc.playerbackup.PlayerBackupMod;
import de.rgse.mc.playerbackup.network.message.RestorePlayerMessage;
import de.rgse.mc.playerbackup.network.message.PlayerBackupSFXMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Random;

public class PlayerBackupPacketHandler {

    private static final String PROTOCOL_VERSION = "1.0.0";

    private PlayerBackupPacketHandler() {
    }

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(PlayerBackupMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int messageId = new Random().nextInt();

    public static void init() {
        INSTANCE.registerMessage(messageId++, RestorePlayerMessage.class, RestorePlayerMessage::encode, RestorePlayerMessage::decode, RestorePlayerMessage::handle);
        INSTANCE.registerMessage(messageId++, PlayerBackupSFXMessage.class, PlayerBackupSFXMessage::encode, PlayerBackupSFXMessage::decode, PlayerBackupSFXMessage::handle);
    }

    public static void send(ServerPlayerEntity serverPlayer) {
        CompoundNBT compoundNBT = new CompoundNBT();
        serverPlayer.save(compoundNBT);
        serverPlayer.addAdditionalSaveData(compoundNBT);

        INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new RestorePlayerMessage(compoundNBT));
        sendFX(serverPlayer);
    }

    public static void sendFX(ServerPlayerEntity serverPlayer) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayer), new PlayerBackupSFXMessage(serverPlayer.getStringUUID()));
    }
}
