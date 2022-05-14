package de.rgse.mc.playerbackup.service;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.rgse.mc.playerbackup.exceptions.NoPlayerSelectedException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Optional;

public class ArgumentService {

    public static final String PLAYER_ARGUMENT_NAME = "player";
    public static final String DATE_ARGUMENT_NAME = "date";

    private static ArgumentService instance;

    public static ArgumentService instance() {
        if (null == instance) {
            instance = new ArgumentService();
        }

        return instance;
    }

    private ArgumentService() {
    }

    public ServerPlayer getPlayer(CommandContext<CommandSourceStack> context) throws CommandSyntaxException, NoPlayerSelectedException {
        EntitySelector argument = context.getArgument(PLAYER_ARGUMENT_NAME, EntitySelector.class);
        List<ServerPlayer> players = argument.findPlayers(context.getSource());

        if (players.isEmpty()) {
            throw new NoPlayerSelectedException();

        } else {
            return players.get(0);
        }
    }

    public Optional<String> getBackup(CommandContext<CommandSourceStack> context) {
        try {
            String argument = context.getArgument(DATE_ARGUMENT_NAME, String.class);
            return Optional.of(argument);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
