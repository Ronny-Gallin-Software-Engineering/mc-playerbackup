package de.rgse.mc.playerbackup.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import de.rgse.mc.playerbackup.argumenttypes.BackupArgumentType;
import de.rgse.mc.playerbackup.commands.model.ArgumentedPlayerCommand;
import de.rgse.mc.playerbackup.commands.model.ReflectivePlayerCommand;
import de.rgse.mc.playerbackup.service.ArgumentService;
import de.rgse.mc.playerbackup.service.BackupService;
import de.rgse.mc.playerbackup.config.PlayerBackupConfig;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;

public class RestorePlayerCommand {

    private RestorePlayerCommand() {
    }

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("restore")
                .requires(PlayerBackupConfig.instance().getPermissions().getSelfRestore()::permitted)
                .then(registerSubCommand())
                .executes(new ReflectivePlayerCommand(BackupService.instance()::restorePlayer));
    }

    private static ArgumentBuilder<CommandSource, ?> registerSubCommand() {
        return Commands.argument(ArgumentService.PLAYER_ARGUMENT_NAME, EntityArgument.player())
                .requires(PlayerBackupConfig.instance().getPermissions().getRestore()::permitted)
                .then(registerSubCommandBackupDate())
                .executes(new ArgumentedPlayerCommand(BackupService.instance()::restorePlayer));
    }

    private static ArgumentBuilder<CommandSource, ?> registerSubCommandBackupDate() {
        return Commands.argument(ArgumentService.DATE_ARGUMENT_NAME, new BackupArgumentType())
                .executes(new ArgumentedPlayerCommand(BackupService.instance()::restorePlayer));
    }
}
