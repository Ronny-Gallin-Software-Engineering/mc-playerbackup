package de.rgse.mc.playerbackup.service;

import de.rgse.mc.playerbackup.PlayerBackupMod;
import de.rgse.mc.playerbackup.exceptions.FileWriteException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final String root = PlayerBackupMod.MOD_ID + "/";
    private static final String FILE_NAME_FORMAT = "%s%s-%s.b64";

    public static void initFielSystem() {
        new File(root).mkdirs();
    }

    public static void writeFile(String uuid, byte[] bytes) throws FileWriteException {
        try {
            String timeFormat = ZonedDateTime.now().format(DateTimeFormatter.ofPattern("yy_MM_dd_kkmmss"));
            String fileName = String.format(FILE_NAME_FORMAT, root, uuid, timeFormat);

            clear(uuid);

            LOGGER.debug("creating file {}", fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            fileOutputStream.write(bytes);
            fileOutputStream.flush();
            fileOutputStream.close();

        } catch (Exception exception) {
            throw new FileWriteException(exception);
        }
    }

    private static void clear(String uuid) throws IOException {
        int backupCountPerPlayer = PlayerBackupConfig.instance().backupCountPerPlayer;

        String[] list = new File(root).list();
        List<String> files = list != null ? Arrays.stream(list).filter(name -> name.startsWith(uuid)).sorted().collect(Collectors.toList()) : Collections.emptyList();

        while (files.size() >= backupCountPerPlayer) {
            LOGGER.info("maximum of {} backups for player {} reached. Deleting oldest", backupCountPerPlayer, uuid);
            deleteOldest(files);
        }
    }

    private static void deleteOldest(List<String> list) throws IOException {
        File file = new File(root + list.get(0));
        LOGGER.debug("Deleting file {}", file.getAbsolutePath());
        FileUtils.forceDelete(file);
        list.remove(0);
    }
}
