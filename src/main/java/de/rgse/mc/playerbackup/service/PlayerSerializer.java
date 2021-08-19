package de.rgse.mc.playerbackup.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.rgse.mc.playerbackup.PlayerBackupMod;
import de.rgse.mc.playerbackup.exceptions.FileWriteException;
import de.rgse.mc.playerbackup.model.PersistentPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class PlayerSerializer {

    private static Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger();

    public static void serialize(List<PersistentPlayer> players) throws FileWriteException {
        for (PersistentPlayer player : players) {
            String s = GSON.toJson(player);
            byte[] encode = Base64.getEncoder().encode(s.getBytes(StandardCharsets.UTF_8));

            FileHandler.writeFile(player.getUuid(), encode);
        }
    }
}
