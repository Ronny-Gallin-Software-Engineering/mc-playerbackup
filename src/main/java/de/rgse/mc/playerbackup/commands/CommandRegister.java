package de.rgse.mc.playerbackup.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.rgse.mc.playerbackup.PlayerBackupMod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CommandRegister {

    private CommandRegister() {
    }

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
                Commands.literal(PlayerBackupMod.MOD_ID)
                        .then(BackupPlayerCommand.register())
                        .then(RestorePlayerCommand.register())
        );
    }
}
