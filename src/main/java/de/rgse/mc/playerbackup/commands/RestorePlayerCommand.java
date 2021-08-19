package de.rgse.mc.playerbackup.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.rgse.mc.playerbackup.PlayerBackupMod;
import de.rgse.mc.playerbackup.service.PlayerArgumentService;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static de.rgse.mc.playerbackup.service.PlayerArgumentService.ARGUMENT_NAME;

public class RestorePlayerCommand implements Command<CommandSource> {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        try {
            List<ServerPlayerEntity> players = PlayerArgumentService.getPlayers(context);

            StringTextComponent textComponent = new StringTextComponent("Restore player ");

            for (ServerPlayerEntity player : players) {
                context.getSource().sendSuccess(textComponent.append(player.getDisplayName()), false);
            }
            return 0;

        } catch (Exception exception) {
            LOGGER.error("error executing command", exception);
            context.getSource().sendFailure(new StringTextComponent(exception.getMessage()));
            return -1;
        }


    }

    public static ArgumentBuilder<CommandSource, ?> register(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.literal("restore")
                .requires(commandSource -> commandSource.hasPermission(0))
                .then(registerSubCommand(dispatcher));
    }

    private static ArgumentBuilder<CommandSource, ?> registerSubCommand(CommandDispatcher<CommandSource> dispatcher) {
        return Commands.argument(ARGUMENT_NAME, EntityArgument.player())
                .executes(new RestorePlayerCommand());
    }
}
