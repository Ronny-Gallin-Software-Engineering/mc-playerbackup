package de.rgse.mc.playerbackup.argumenttypes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.rgse.mc.playerbackup.service.ServerPlayerSerializer;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BackupArgumentType implements ArgumentType<String> {

    private final StringArgumentType delegate = StringArgumentType.word();

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return delegate.parse(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        List<String> suggestions = new LinkedList<>();

        String input = context.getInput();
        String[] s = input.split(" ");
        String userName = s[s.length - 1];

        ServerPlayerEntity playerByName = ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayerByName(userName);
        if (playerByName != null) {
            suggestions = ServerPlayerSerializer.instance().getBackupsForPlayer(playerByName);
        }

        return ISuggestionProvider.suggest(suggestions, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return delegate.getExamples();
    }
}
