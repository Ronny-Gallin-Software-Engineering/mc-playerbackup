package de.rgse.mc.playerbackup.service;

import com.mojang.brigadier.context.CommandContext;
import de.rgse.mc.playerbackup.config.PlayerBackupConfig;
import de.rgse.mc.playerbackup.exceptions.NoBackupFoundException;
import de.rgse.mc.playerbackup.model.DefaultStyle;
import de.rgse.mc.playerbackup.network.PlayerBackupNetwork;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
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

    public int restorePlayer(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        try {
            ServerPlayerSerializer playerSerializer = ServerPlayerSerializer.instance();

            Optional<String> backup = ArgumentService.instance().getBackup(context);
            CompoundTag compoundNBT = backup.isPresent() ? playerSerializer.deserialize(player, backup.get()) : playerSerializer.deserialize(player);

            playerSerializer.deserialize(player, compoundNBT);

            PlayerBackupNetwork.sendRestore(player);
            PlayerBackupNetwork.sendFX(player);
            PlayerBackupNetwork.sendSync();

            if (PlayerBackupConfig.instance().isDeleteOnRestore()) {
                if (backup.isPresent()) {
                    FileHandler.instance().delete(player.getStringUUID(), backup.get());

                } else {
                    FileHandler.instance().deleteLatest(player.getStringUUID());
                }
            }

            MutableComponent playerDisplayName = getDisplayName(player);
            TranslatableComponent translationTextComponent = new TranslatableComponent("msg.successfully_restored");

            context.getSource().sendSuccess(new TextComponent("").append(playerDisplayName).append(translationTextComponent), false);

            return 0;

        } catch (NoBackupFoundException exception) {
            String command = "playerbackup backup " + player.getScoreboardName();
            TranslatableComponent translationTextComponent = new TranslatableComponent("msg.no_backup_yet", command);
            context.getSource().sendFailure(translationTextComponent);
            return -1;

        } catch (Exception exception) {
            LOGGER.error("error executing command", exception);
            context.getSource().sendFailure(new TextComponent(exception.getMessage()));
            return -1;
        }
    }

    public int backupPlayer(CommandContext<CommandSourceStack> context, ServerPlayer player) {
        try {
            ServerPlayerSerializer.instance().serialize(player);
            PlayerBackupNetwork.sendFX(player);
            PlayerBackupNetwork.sendSync();

            MutableComponent playerDisplayName = getDisplayName(player);
            TranslatableComponent translationTextComponent = new TranslatableComponent("msg.successfully_saved");

            context.getSource().sendSuccess(new TextComponent("").append(playerDisplayName).append(translationTextComponent), false);

            return 0;

        } catch (Exception exception) {
            LOGGER.error("error executing command", exception);
            context.getSource().sendFailure(new TextComponent(exception.getMessage()));
            return -1;
        }
    }

    public List<String> getBackups() {
        return FileHandler.instance().getBackups();
    }

    private MutableComponent getDisplayName(Player player) {
        MutableComponent playerDisplayName = player.getDisplayName().copy();
        Style style = playerDisplayName.getStyle().withColor(DefaultStyle.COLOR);
        playerDisplayName.setStyle(style);
        return playerDisplayName;
    }
}
