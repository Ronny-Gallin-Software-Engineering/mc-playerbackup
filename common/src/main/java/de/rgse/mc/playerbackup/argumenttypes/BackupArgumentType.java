package de.rgse.mc.playerbackup.argumenttypes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.rgse.mc.playerbackup.service.client.Backups;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BackupArgumentType implements ArgumentType<String> {

    private static final Pattern PATTERN = Pattern.compile("^.*(\\d{2}_\\d{2}_\\d{2}_\\d{6}).dat");

    private final StringArgumentType delegate = StringArgumentType.word();

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return delegate.parse(reader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = context.getInput();
        String[] s = input.split(" ");
        String userName = s[s.length - 1];

        PlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(userName);

        List<String> suggestions = Collections.emptyList();
        if (playerInfo != null) {
            suggestions = Backups.instance().getAvailableBackups().stream()
                    .filter(f -> f.startsWith(playerInfo.getProfile().getId().toString()))
                    .map(this::getTimestamp)
                    .collect(Collectors.toList());
        }

        return SharedSuggestionProvider.suggest(suggestions, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return delegate.getExamples();
    }

    private String getTimestamp(String filename) {
        Matcher matcher = PATTERN.matcher(filename);
        return matcher.find() ? matcher.group(1) : null;
    }
}
