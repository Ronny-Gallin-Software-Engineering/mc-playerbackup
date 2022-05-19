package de.rgse.mc.playerbackup.fabric;

import de.rgse.mc.playerbackup.PlayerbackupMod;
import net.fabricmc.api.ModInitializer;

public class PlayerBackupModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        PlayerbackupMod.init();
    }
}
