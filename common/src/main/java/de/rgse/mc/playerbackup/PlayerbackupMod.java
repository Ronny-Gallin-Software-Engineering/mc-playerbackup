package de.rgse.mc.playerbackup;

import de.rgse.mc.playerbackup.argumenttypes.BackupArgumentType;
import de.rgse.mc.playerbackup.commands.CommandRegister;
import de.rgse.mc.playerbackup.config.PlayerBackupConfig;
import de.rgse.mc.playerbackup.network.PlayerBackupNetwork;
import de.rgse.mc.playerbackup.service.FileHandler;
import de.rgse.mc.playerbackup.service.PlayerList;
import de.rgse.mc.playerbackup.sound.Sounds;
import me.shedaniel.architectury.event.events.LifecycleEvent;
import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.commands.synchronization.EmptyArgumentSerializer;
import org.apache.logging.log4j.LogManager;

public class PlayerbackupMod {
    public static final String MOD_ID = "playerbackup";

    private PlayerbackupMod() {
    }

    public static void init() {
        PlayerBackupConfig.instance();
        PlayerBackupNetwork.init();
        Sounds.init();

        ArgumentTypes.register(PlayerbackupMod.MOD_ID + ":backup", BackupArgumentType.class, new EmptyArgumentSerializer<>(BackupArgumentType::new));

        PlayerList.init();

        LifecycleEvent.SERVER_STARTING.register(s -> {
            FileHandler.init(s);
            CommandRegister.register(s.getCommands().getDispatcher());
            LogManager.getLogger(PlayerbackupMod.class).info("{} server successfully initialized", PlayerbackupMod.class.getSimpleName());
        });
    }
}