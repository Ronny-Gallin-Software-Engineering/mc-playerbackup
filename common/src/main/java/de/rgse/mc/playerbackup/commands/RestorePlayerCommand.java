package de.rgse.mc.playerbackup.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.rgse.mc.playerbackup.argumenttypes.BackupArgumentType;
import de.rgse.mc.playerbackup.commands.model.ArgumentedPlayerCommand;
import de.rgse.mc.playerbackup.commands.model.ReflectivePlayerCommand;
import de.rgse.mc.playerbackup.config.PlayerBackupConfig;
import de.rgse.mc.playerbackup.service.ArgumentService;
import de.rgse.mc.playerbackup.service.BackupService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

public class RestorePlayerCommand {

    private RestorePlayerCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("restore")
                .requires(PlayerBackupConfig.instance().getPermissions().getSelfRestore()::permitted)
                .then(registerSubCommand())
                .executes(new ReflectivePlayerCommand(BackupService.instance()::restorePlayer));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> registerSubCommand() {
        return Commands.argument(ArgumentService.PLAYER_ARGUMENT_NAME, EntityArgument.player())
                .requires(PlayerBackupConfig.instance().getPermissions().getRestore()::permitted)
                .then(registerSubCommandBackupDate())
                .executes(new ArgumentedPlayerCommand(BackupService.instance()::restorePlayer));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> registerSubCommandBackupDate() {
        return Commands.argument(ArgumentService.DATE_ARGUMENT_NAME, new BackupArgumentType())
                .executes(new ArgumentedPlayerCommand(BackupService.instance()::restorePlayer));
    }
}
