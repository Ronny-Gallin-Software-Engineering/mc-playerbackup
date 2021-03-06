package de.rgse.mc.playerbackup.forge;

import de.rgse.mc.playerbackup.PlayerbackupMod;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = PlayerbackupMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(PlayerbackupMod.MOD_ID)
public class PlayerBackupModForge {

    public PlayerBackupModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(PlayerbackupMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        PlayerbackupMod.init();
    }

}
