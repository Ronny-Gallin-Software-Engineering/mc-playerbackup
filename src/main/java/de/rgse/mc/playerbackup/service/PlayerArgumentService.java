package de.rgse.mc.playerbackup.service;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.rgse.mc.playerbackup.exceptions.NoPlayerSelectedException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.List;

public class PlayerArgumentService {

    public static final String ARGUMENT_NAME = "player";

    public static List<ServerPlayerEntity> getPlayers(CommandContext<CommandSource> context) throws CommandSyntaxException, NoPlayerSelectedException {
        EntitySelector argument = context.getArgument(ARGUMENT_NAME, EntitySelector.class);
        List<ServerPlayerEntity> players = argument.findPlayers(context.getSource());

        if (players == null || players.isEmpty()) {
            throw new NoPlayerSelectedException();

        } else {
            return players;
        }
    }
}
