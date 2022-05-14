package de.rgse.mc.playerbackup.commands.model;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.rgse.mc.playerbackup.exceptions.NoPlayerSelectedException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.BiFunction;

public abstract class AbstractPlayerCommand implements Command<CommandSourceStack> {

    private static final Logger LOGGER = LogManager.getLogger();

    protected abstract ServerPlayer getPlayer(CommandContext<CommandSourceStack> context) throws NoPlayerSelectedException, CommandSyntaxException;

    private final BiFunction<CommandContext<CommandSourceStack>, ServerPlayer, Integer> onRun;

    protected AbstractPlayerCommand(BiFunction<CommandContext<CommandSourceStack>, ServerPlayer, Integer> onRun) {
        this.onRun = onRun;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) {

        try {
            ServerPlayer player = getPlayer(context);
            return onRun.apply(context, player);

        } catch (Exception exception) {
            LOGGER.error("error executing command", exception);
            context.getSource().sendFailure(new TextComponent(exception.getMessage()));
            return -1;
        }
    }
}
