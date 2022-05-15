package de.rgse.mc.playerbackup.permissions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum PermissionLevel {

    DEFAULT_PLAYER(0, "ALL"), MODERATOR(1), GAME_MASTER(2), ADMINISTRATOR(3), OWNER(4, "OP");

    private final int level;
    private final List<String> synonyms;

    PermissionLevel(int level, String... synonyms) {
        this.level = level;
        this.synonyms = Arrays.asList(synonyms);
    }

    public int getLevel() {
        return level;
    }

    static Optional<PermissionLevel> of(String value) {
        return Arrays.stream(values()).filter(pl -> pl.name().equals(value) || pl.synonyms.contains(value)).findFirst();
    }
}
