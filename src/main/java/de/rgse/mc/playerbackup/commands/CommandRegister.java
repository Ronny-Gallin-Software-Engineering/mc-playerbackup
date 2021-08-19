package de.rgse.mc.playerbackup.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import de.rgse.mc.playerbackup.PlayerBackupMod;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class CommandRegister {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> playerbackup = dispatcher.register(
                Commands.literal(PlayerBackupMod.MOD_ID)
                        .then(BackupPlayerCommand.register(dispatcher))
                        .then(RestorePlayerCommand.register(dispatcher))
        );

        dispatcher.register(Commands.literal("pb").redirect(playerbackup));
    }
}
