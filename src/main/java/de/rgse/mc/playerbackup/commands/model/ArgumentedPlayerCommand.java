package de.rgse.mc.playerbackup.commands.model;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.rgse.mc.playerbackup.exceptions.NoPlayerSelectedException;
import de.rgse.mc.playerbackup.service.ArgumentService;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.function.BiFunction;

public class ArgumentedPlayerCommand extends AbstractPlayerCommand {

    public ArgumentedPlayerCommand(BiFunction<CommandContext<CommandSource>, ServerPlayerEntity, Integer> onRun) {
        super(onRun);
    }

    public ServerPlayerEntity getPlayer(CommandContext<CommandSource> context) throws NoPlayerSelectedException, CommandSyntaxException {
        return ArgumentService.instance().getPlayer(context);
    }
}
