package de.rgse.mc.playerbackup.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.rgse.mc.playerbackup.PlayerBackupMod;
import de.rgse.mc.playerbackup.exceptions.NoPlayerSelectedException;
import de.rgse.mc.playerbackup.model.PersistentPlayer;
import de.rgse.mc.playerbackup.service.PlayerArgumentService;
import de.rgse.mc.playerbackup.service.PlayerSerializer;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static de.rgse.mc.playerbackup.service.PlayerArgumentService.ARGUMENT_NAME;

public class BackupPlayerCommand {

    private static final Logger LOGGER = LogManager.getLogger();

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("backup")
                .requires(commandSource -> commandSource.hasPermission(0))
                .then(registerSubCommand(dispatcher))
                .executes(new ReflectiveBackupPlayerCommand());
    }

    private static ArgumentBuilder<CommandSource, ?> registerSubCommand(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.argument(ARGUMENT_NAME, EntityArgument.player())
                .executes(new ArgumentedBackupPlayerCommand());
    }

    private static abstract class AbstractBackupPlayerCommand implements Command<CommandSource> {

        abstract List<ServerPlayerEntity> getPlayers(CommandContext<CommandSource> context) throws NoPlayerSelectedException, CommandSyntaxException;

        @Override
        public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {

            try {
                List<ServerPlayerEntity> players = getPlayers(context);
                context.getSource().sendSuccess(new StringTextComponent("Backup started"), false);

                List<PersistentPlayer> persistentPlayers = players.stream().map(PersistentPlayer::new).collect(Collectors.toList());
                PlayerSerializer.serialize(persistentPlayers);
                context.getSource().sendSuccess(new StringTextComponent("Backup successful"), true);

                return 0;

            } catch (Exception exception) {
                LOGGER.error("error executing command", exception);
                context.getSource().sendFailure(new StringTextComponent(exception.getMessage()));
                return -1;
            }
        }

    }

    private static class ArgumentedBackupPlayerCommand extends AbstractBackupPlayerCommand {
        List<ServerPlayerEntity> getPlayers(CommandContext<CommandSource> context) throws NoPlayerSelectedException, CommandSyntaxException {
            return PlayerArgumentService.getPlayers(context);
        }
    }

    private static class ReflectiveBackupPlayerCommand extends AbstractBackupPlayerCommand {
        List<ServerPlayerEntity> getPlayers(CommandContext<CommandSource> context) throws NoPlayerSelectedException, CommandSyntaxException {
            if (context.getSource().getEntity() instanceof ServerPlayerEntity) {
                return Collections.singletonList((ServerPlayerEntity) context.getSource().getEntity());
            }
            return Collections.emptyList();
        }
    }
}
