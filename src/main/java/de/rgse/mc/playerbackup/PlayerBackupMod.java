package de.rgse.mc.playerbackup;

import de.rgse.mc.playerbackup.argumenttypes.BackupArgumentType;
import de.rgse.mc.playerbackup.commands.CommandRegister;
import de.rgse.mc.playerbackup.config.PlayerBackupConfig;
import de.rgse.mc.playerbackup.network.PlayerBackupNetwork;
import de.rgse.mc.playerbackup.service.FileHandler;
import de.rgse.mc.playerbackup.sound.SoundList;
import net.minecraft.command.arguments.ArgumentSerializer;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(PlayerBackupMod.MOD_ID)
@Mod.EventBusSubscriber(modid = PlayerBackupMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlayerBackupMod {

    public static final String MOD_ID = "playerbackup";

    private static final Logger LOGGER = LogManager.getLogger();

    public PlayerBackupMod() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(this::setup);
        SoundList.SOUNDS.register(bus);
        MinecraftForge.EVENT_BUS.register(this);

        ArgumentTypes.register(MOD_ID + ":backup", BackupArgumentType.class, new ArgumentSerializer<>(BackupArgumentType::new));
    }

    private void setup(final FMLCommonSetupEvent event) {
        PlayerBackupConfig.instance();
        PlayerBackupNetwork.init();
        LOGGER.info("{} successfully initialized", PlayerBackupNetwork.class.getSimpleName());
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        FileHandler.init(event.getServer());

        CommandRegister.register(event.getServer().getCommands().getDispatcher());
        PlayerBackupNetwork.init();
        LOGGER.info("{} successfully initialized", MOD_ID);
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getPlayer().level.isClientSide()) {
            String stringUUID = event.getPlayer().getStringUUID();
            PlayerBackupNetwork.sendSync(stringUUID);

        } else {
            LOGGER.info("loggin event ignored");
        }
    }
}
