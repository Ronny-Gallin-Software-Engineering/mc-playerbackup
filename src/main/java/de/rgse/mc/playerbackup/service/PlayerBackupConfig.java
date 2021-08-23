package de.rgse.mc.playerbackup.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.rgse.mc.playerbackup.exceptions.FileWriteException;
import de.rgse.mc.playerbackup.model.Permissions;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class PlayerBackupConfig {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

    private static PlayerBackupConfig instance;
    protected static String root = "config/playerbackup.json";

    @Expose
    int backupCountPerPlayer;

    @Expose
    Permissions permissions;

    private PlayerBackupConfig() {
    }

    public Permissions getPermissions() {
        return permissions;
    }

    public static PlayerBackupConfig instance() {
        if (null == instance) {
            instance = readConfig();
        }

        return instance;
    }

    private static PlayerBackupConfig readConfig() {
        try {
            PlayerBackupConfig playerBackupConfig = GSON.fromJson(new FileReader(root), PlayerBackupConfig.class);
            playerBackupConfig.permissions.register();
            return playerBackupConfig;

        } catch (FileNotFoundException e) {
            try {
                LOGGER.info("unable to find config file. Writing new one");
                writeConfig();

                PlayerBackupConfig playerBackupConfig = GSON.fromJson(new FileReader(root), PlayerBackupConfig.class);
                playerBackupConfig.permissions.register();
                return playerBackupConfig;

            } catch (IOException e1) {
                LOGGER.error("unable to write config file", e1);
                throw new FileWriteException(e1);
            }
        }
    }

    private static void writeConfig() throws IOException {
        PlayerBackupConfig playerBackupConfig = new PlayerBackupConfig();
        playerBackupConfig.backupCountPerPlayer = 5;
        playerBackupConfig.permissions = Permissions.createDefaultPermissions();

        String s = GSON.toJson(playerBackupConfig);
        FileUtils.write(new File(root), s, StandardCharsets.UTF_8);
    }
}
