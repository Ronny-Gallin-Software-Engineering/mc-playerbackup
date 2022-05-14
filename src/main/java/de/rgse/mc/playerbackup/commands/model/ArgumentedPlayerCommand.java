package de.rgse.mc.playerbackup.commands.model;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.rgse.mc.playerbackup.exceptions.NoPlayerSelectedException;
import de.rgse.mc.playerbackup.service.ArgumentService;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiFunction;

public class ArgumentedPlayerCommand extends AbstractPlayerCommand {

    public ArgumentedPlayerCommand(BiFunction<CommandContext<CommandSourceStack>, ServerPlayer, Integer> onRun) {
        super(onRun);
    }

    public ServerPlayer getPlayer(CommandContext<CommandSourceStack> context) throws NoPlayerSelectedException, CommandSyntaxException {
        return ArgumentService.instance().getPlayer(context);
    }
}
