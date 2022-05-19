package de.rgse.mc.playerbackup.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import de.rgse.mc.playerbackup.commands.model.ArgumentedPlayerCommand;
import de.rgse.mc.playerbackup.commands.model.ReflectivePlayerCommand;
import de.rgse.mc.playerbackup.config.PlayerBackupConfig;
import de.rgse.mc.playerbackup.service.BackupService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;

import static de.rgse.mc.playerbackup.service.ArgumentService.PLAYER_ARGUMENT_NAME;

public class BackupPlayerCommand {

    private BackupPlayerCommand() {
    }

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal("backup")
                .requires(PlayerBackupConfig.instance().getPermissions().getSelfBackup()::permitted)
                .then(registerSubCommand())
                .executes(new ReflectivePlayerCommand(BackupService.instance()::backupPlayer));
    }

    private static RequiredArgumentBuilder<CommandSourceStack, EntitySelector> registerSubCommand() {
        return Commands.argument(PLAYER_ARGUMENT_NAME, EntityArgument.player())
                .requires(PlayerBackupConfig.instance().getPermissions().getBackup()::permitted)
                .executes(new ArgumentedPlayerCommand(BackupService.instance()::backupPlayer));
    }
}