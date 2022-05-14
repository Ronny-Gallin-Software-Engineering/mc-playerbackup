package de.rgse.mc.playerbackup.commands.model;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.BiFunction;

public class ReflectivePlayerCommand extends AbstractPlayerCommand {

    public ReflectivePlayerCommand(BiFunction<CommandContext<CommandSourceStack>, ServerPlayer, Integer> onRun) {
        super(onRun);
    }

    public ServerPlayer getPlayer(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getEntity() instanceof ServerPlayer) {
            return (ServerPlayer) context.getSource().getEntity();
        }
        return null;
    }
}
