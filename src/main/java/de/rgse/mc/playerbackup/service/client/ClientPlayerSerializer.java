package de.rgse.mc.playerbackup.service.client;

import de.rgse.mc.playerbackup.service.PlayerSerializer;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.nbt.CompoundNBT;

public class ClientPlayerSerializer extends PlayerSerializer<ClientPlayerEntity> {

    private static ClientPlayerSerializer instance;

    public static ClientPlayerSerializer instance() {
        if (null == instance) {
            instance = new ClientPlayerSerializer();
        }

        return instance;
    }

    private ClientPlayerSerializer() {
    }

    @Override
    protected void deserializeNetworksideSpecific(ClientPlayerEntity player, CompoundNBT tag) {
        int xpLevel = tag.getInt("XpLevel");
        int xpTotal = tag.getInt("XpTotal");
        int xpProgress = tag.getInt("XpP");

        player.setExperienceValues(xpProgress, xpTotal, xpLevel);
    }
}
