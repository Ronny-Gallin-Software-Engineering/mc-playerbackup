package de.rgse.mc.playerbackup.service;

import com.mojang.brigadier.context.CommandContext;
import de.rgse.mc.playerbackup.config.PlayerBackupConfig;
import de.rgse.mc.playerbackup.exceptions.NoBackupFoundException;
import de.rgse.mc.playerbackup.model.DefaultStyle;
import de.rgse.mc.playerbackup.network.PlayerBackupPacketHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class BackupService {

    private static final Logger LOGGER = LogManager.getLogger();

    private static BackupService instance;

    public static BackupService instance() {
        if (instance == null) {
            instance = new BackupService();
        }

        return instance;
    }

    public int restorePlayer(CommandContext<CommandSource> context, ServerPlayerEntity player) {
        try {
            ServerPlayerSerializer playerSerializer = ServerPlayerSerializer.instance();

            Optional<String> backup = ArgumentService.instance().getBackup(context);
            CompoundNBT compoundNBT = backup.isPresent() ? playerSerializer.deserialize(player, backup.get()) : playerSerializer.deserialize(player);

            playerSerializer.deserialize(player, compoundNBT);

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
    }

    public int backupPlayer(CommandContext<CommandSource> context, ServerPlayerEntity player) {
        try {
            ServerPlayerSerializer.instance().serialize(player);
            PlayerBackupPacketHandler.sendFX(player);

            TranslationTextComponent translationTextComponent = new TranslationTextComponent("msg.successfully_saved");
            IFormattableTextComponent playerDisplayName = player.getDisplayName().copy();
            playerDisplayName.getStyle().withColor(DefaultStyle.COLOR);

            context.getSource().sendSuccess(new StringTextComponent("").append(playerDisplayName).append(translationTextComponent), false);

            return 0;

        } catch (Exception exception) {
            LOGGER.error("error executing command", exception);
            context.getSource().sendFailure(new StringTextComponent(exception.getMessage()));
            return -1;
        }
    }
}
