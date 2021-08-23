package de.rgse.mc.playerbackup.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.rgse.mc.playerbackup.commands.model.ArgumentedPlayerCommand;
import de.rgse.mc.playerbackup.commands.model.ReflectivePlayerCommand;
import de.rgse.mc.playerbackup.model.PersistentPlayer;
import de.rgse.mc.playerbackup.network.PlayerBackupPacketHandler;
import de.rgse.mc.playerbackup.service.PlayerBackupConfig;
import de.rgse.mc.playerbackup.service.PlayerSerializer;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;

import static de.rgse.mc.playerbackup.service.ArgumentService.PLAYER_ARGUMENT_NAME;

public class BackupPlayerCommand {

    private static final Logger LOGGER = LogManager.getLogger();

    private BackupPlayerCommand() {
    }

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("backup")
                .requires(PlayerBackupConfig.instance().getPermissions().getSelfBackup()::permitted)
                .then(registerSubCommand())
                .executes(new ReflectivePlayerCommand(onRun()));
    }

    private static RequiredArgumentBuilder<CommandSource, EntitySelector> registerSubCommand() {
        return Commands.argument(PLAYER_ARGUMENT_NAME, EntityArgument.player())
                .requires(PlayerBackupConfig.instance().getPermissions().getBackup()::permitted)
                .executes(new ArgumentedPlayerCommand(onRun()));
    }

    private static BiFunction<CommandContext<CommandSource>, ServerPlayerEntity, Integer> onRun() {
        return (context, player) -> {
            try {
                PersistentPlayer persistentPlayer = new PersistentPlayer(player);
                PlayerSerializer.instance().serialize(persistentPlayer);
                PlayerBackupPacketHandler.sendFX(player);

                TranslationTextComponent translationTextComponent = new TranslationTextComponent("msg.successfully_saved");

                context.getSource().sendSuccess(new StringTextComponent("").append(player.getDisplayName()).append(new StringTextComponent(" successfully saved")), false);

                return 0;

            } catch (Exception exception) {
                LOGGER.error("error executing command", exception);
                context.getSource().sendFailure(new StringTextComponent(exception.getMessage()));
                return -1;
            }
        };
    }
}
