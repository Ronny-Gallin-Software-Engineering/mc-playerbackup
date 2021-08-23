package de.rgse.mc.playerbackup.commands.model;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.ServerPlayerEntity;

import java.util.function.BiFunction;

public class ReflectivePlayerCommand extends AbstractPlayerCommand {

    public ReflectivePlayerCommand(BiFunction<CommandContext<CommandSource>, ServerPlayerEntity, Integer> onRun) {
        super(onRun);
    }

    public ServerPlayerEntity getPlayer(CommandContext<CommandSource> context) {
        if (context.getSource().getEntity() instanceof ServerPlayerEntity) {
            return (ServerPlayerEntity) context.getSource().getEntity();
        }
        return null;
    }
}
