package de.rgse.mc.playerbackup.commands;

import com.mojang.brigadier.CommandDispatcher;
import de.rgse.mc.playerbackup.PlayerbackupMod;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class CommandRegister {

    private CommandRegister() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal(PlayerbackupMod.MOD_ID)
                        .then(BackupPlayerCommand.register())
                        .then(RestorePlayerCommand.register())
        );
    }
}
