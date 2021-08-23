package de.rgse.mc.playerbackup.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.rgse.mc.playerbackup.argumenttypes.BackupArgumentType;
import de.rgse.mc.playerbackup.commands.model.ArgumentedPlayerCommand;
import de.rgse.mc.playerbackup.commands.model.ReflectivePlayerCommand;
import de.rgse.mc.playerbackup.exceptions.NoBackupFoundException;
import de.rgse.mc.playerbackup.model.DefaultStyle;
import de.rgse.mc.playerbackup.network.PlayerBackupPacketHandler;
import de.rgse.mc.playerbackup.service.ArgumentService;
import de.rgse.mc.playerbackup.service.FileHandler;
import de.rgse.mc.playerbackup.service.PlayerBackupConfig;
import de.rgse.mc.playerbackup.service.PlayerSerializer;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.BiFunction;

public class RestorePlayerCommand {

    private static final Logger LOGGER = LogManager.getLogger();

    private RestorePlayerCommand() {
    }

    public static LiteralArgumentBuilder<CommandSource> register() {
        return Commands.literal("restore")
                .requires(PlayerBackupConfig.instance().getPermissions().getSelfRestore()::permitted)
                .then(registerSubCommand())
                .executes(new ReflectivePlayerCommand(onRun()));
    }

    private static ArgumentBuilder<CommandSource, ?> registerSubCommand() {
        return Commands.argument(ArgumentService.PLAYER_ARGUMENT_NAME, EntityArgument.player())
                .requires(PlayerBackupConfig.instance().getPermissions().getRestore()::permitted)
                .then(registerSubCommandBackupDate())
                .executes(new ArgumentedPlayerCommand(onRun()));
    }

    private static ArgumentBuilder<CommandSource, ?> registerSubCommandBackupDate() {
        return Commands.argument(ArgumentService.DATE_ARGUMENT_NAME, new BackupArgumentType())
                .executes(new ArgumentedPlayerCommand(onRun()));
    }

    private static BiFunction<CommandContext<CommandSource>, ServerPlayerEntity, Integer> onRun() {
        return (context, player) -> {
            try {
                Optional<String> backup = ArgumentService.instance().getBackup(context);
                CompoundNBT compoundNBT = backup.isPresent() ? PlayerSerializer.instance().deserialize(player, backup.get()) : PlayerSerializer.instance().deserialize(player);

                PlayerSerializer.instance().deserialize(player, compoundNBT);

                PlayerBackupPacketHandler.send(player);

                if (PlayerBackupConfig.instance().isDeleteOnRestore()) {
                    if (backup.isPresent()) {
                        FileHandler.instance().delete(player.getStringUUID(), backup.get());

                    } else {
                        FileHandler.instance().deleteLatest(player.getStringUUID());
                    }
                }

                IFormattableTextComponent playerDisplayName = player.getDisplayName().copy();
                playerDisplayName.getStyle().withColor(DefaultStyle.COLOR);
                TranslationTextComponent translationTextComponent = new TranslationTextComponent("msg.successfully_restored");

                context.getSource().sendSuccess(new StringTextComponent("").append(playerDisplayName).append(translationTextComponent), false);

                return 0;

            } catch (NoBackupFoundException exception) {
                String command = "playerbackup backup " + player.getScoreboardName();
                TranslationTextComponent translationTextComponent = new TranslationTextComponent("msg.no_backup_yet", command);
                context.getSource().sendFailure(translationTextComponent);
                return -1;

            } catch (Exception exception) {
                LOGGER.error("error executing command", exception);
                context.getSource().sendFailure(new StringTextComponent(exception.getMessage()));
                return -1;
            }
        };
    }
}
