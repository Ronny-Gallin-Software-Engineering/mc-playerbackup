package de.rgse.mc.playerbackup.service.client;

import de.rgse.mc.playerbackup.service.PlayerSerializer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;

public class ClientPlayerSerializer extends PlayerSerializer<LocalPlayer> {

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
    protected void deserializeNetworksideSpecific(LocalPlayer player, CompoundTag tag) {
        int xpLevel = tag.getInt("XpLevel");
        int xpTotal = tag.getInt("XpTotal");
        int xpProgress = tag.getInt("XpP");

        player.setExperienceValues(xpProgress, xpTotal, xpLevel);
    }
}
