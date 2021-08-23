package de.rgse.mc.playerbackup.commands.model;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.rgse.mc.playerbackup.exceptions.NoPlayerSelectedException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;

public abstract class AbstractPlayerCommand implements Command<CommandSource> {

    private static final Logger LOGGER = LogManager.getLogger();

    protected abstract ServerPlayerEntity getPlayer(CommandContext<CommandSource> context) throws NoPlayerSelectedException, CommandSyntaxException;

    private final BiFunction<CommandContext<CommandSource>, ServerPlayerEntity, Integer> onRun;

    protected AbstractPlayerCommand(BiFunction<CommandContext<CommandSource>, ServerPlayerEntity, Integer> onRun) {
        this.onRun = onRun;
    }

    @Override
    public int run(CommandContext<CommandSource> context) {

        try {
            ServerPlayerEntity player = getPlayer(context);
            return onRun.apply(context, player);

        } catch (Exception exception) {
            LOGGER.error("error executing command", exception);
            context.getSource().sendFailure(new StringTextComponent(exception.getMessage()));
            return -1;
        }
    }
}
