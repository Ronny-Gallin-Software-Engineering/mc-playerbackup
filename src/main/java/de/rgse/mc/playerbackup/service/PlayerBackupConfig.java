package de.rgse.mc.playerbackup.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import de.rgse.mc.playerbackup.PlayerBackupMod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PlayerBackupConfig {

    private static final Logger LOGGER = LogManager.getLogger();

    private static Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    private static PlayerBackupConfig INSTANCE;
    protected static String root = "config/playerbackup.json";

    @Expose
    int backupCountPerPlayer = 5;

    private PlayerBackupConfig() {
    }

    public static PlayerBackupConfig instance() {
        if (null == INSTANCE) {
            INSTANCE = readConfig();

        }

        return INSTANCE;
    }

    private static PlayerBackupConfig readConfig() {
        try {
            return GSON.fromJson(new FileReader(root), PlayerBackupConfig.class);

        } catch (FileNotFoundException e) {
            try {
                LOGGER.info("unable to find config file. Writing new one");
                writeConfig();

                return GSON.fromJson(new FileReader(root), PlayerBackupConfig.class);

            } catch (IOException e1) {
                LOGGER.error("unable to write config file", e1);
                throw new RuntimeException(e1);
            }
        }
    }

    private static void writeConfig() throws IOException {
        String s = GSON.toJson(new PlayerBackupConfig());
        FileWriter fileWriter = new FileWriter(root);
        fileWriter.write(s);
        fileWriter.flush();
        fileWriter.close();
    }
}
