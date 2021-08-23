package de.rgse.mc.playerbackup;

import de.rgse.mc.playerbackup.commands.CommandRegister;
import de.rgse.mc.playerbackup.network.PlayerBackupPacketHandler;
import de.rgse.mc.playerbackup.service.FileHandler;
import de.rgse.mc.playerbackup.service.PlayerBackupConfig;
import de.rgse.mc.playerbackup.sound.SoundList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PlayerBackupMod.MOD_ID)
public class PlayerBackupMod {

    public static final String MOD_ID = "playerbackup";

    private static final Logger LOGGER = LogManager.getLogger();

    public PlayerBackupMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        SoundList.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event) {
        PlayerBackupConfig.instance();
        PlayerBackupPacketHandler.init();
        LOGGER.info("{} successfully initialized", PlayerBackupPacketHandler.class.getSimpleName());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        FileHandler.init(event.getServer());

        CommandRegister.register(event.getServer().getCommands().getDispatcher());
        PlayerBackupPacketHandler.init();
        LOGGER.info("{} successfully initialized", MOD_ID);
    }
}
