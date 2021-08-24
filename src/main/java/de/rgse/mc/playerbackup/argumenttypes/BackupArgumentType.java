package de.rgse.mc.playerbackup.argumenttypes;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import de.rgse.mc.playerbackup.service.client.Backups;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.command.ISuggestionProvider;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BackupArgumentType implements ArgumentType<String>, Serializable {

    private static final Pattern PATTERN = Pattern.compile("^.*(\\d{2}_\\d{2}_\\d{2}_\\d{6}).dat");

    @Override
    public String parse(StringReader reader) throws CommandSyntaxException {
        return reader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        String input = context.getInput();
        String[] s = input.split(" ");
        String userName = s[s.length - 1];

        NetworkPlayerInfo playerInfo = Minecraft.getInstance().getConnection().getPlayerInfo(userName);

        List<String> suggestions = Collections.emptyList();
        if (playerInfo != null) {
            suggestions = Backups.getBackups().stream()
                    .filter(f -> f.startsWith(playerInfo.getProfile().getId().toString()))
                    .map(this::getTimestamp)
                    .collect(Collectors.toList());
        }

        return ISuggestionProvider.suggest(suggestions, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.asList("word", "words_with_underscores");
    }

    private String getTimestamp(String filename) {
        Matcher matcher = PATTERN.matcher(filename);
        return matcher.find() ? matcher.group(1) : null;
    }
}
