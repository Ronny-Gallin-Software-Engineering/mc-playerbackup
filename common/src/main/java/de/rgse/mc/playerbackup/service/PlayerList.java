package de.rgse.mc.playerbackup.service;

import de.rgse.mc.playerbackup.network.PlayerBackupNetwork;
import me.shedaniel.architectury.event.events.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PlayerList implements PlayerEvent.PlayerJoin, PlayerEvent.PlayerQuit {

    private static PlayerList instance;

    private final Map<String, ServerPlayer> players = new HashMap<>();

    public static void init() {
        instance = new PlayerList();
        PlayerEvent.PLAYER_JOIN.register(instance);
        PlayerEvent.PLAYER_QUIT.register(instance);
    }

    @Override
    public void join(ServerPlayer player) {
        players.put(player.getStringUUID(), player);
        PlayerBackupNetwork.sendSync(player);
    }

    @Override
    public void quit(ServerPlayer player) {
        players.remove(player.getStringUUID());
    }

    public Collection<ServerPlayer> getPlayers() {
        return Collections.unmodifiableCollection(players.values());
    }

    public static PlayerList getInstance() {
        return instance;
    }
}
