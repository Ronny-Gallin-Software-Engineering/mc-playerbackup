package de.rgse.mc.playerbackup.service;

import de.rgse.mc.playerbackup.PlayerBackupMod;
import de.rgse.mc.playerbackup.exceptions.FileReadException;
import de.rgse.mc.playerbackup.exceptions.FileWriteException;
import de.rgse.mc.playerbackup.exceptions.NoBackupFoundException;
import de.rgse.mc.playerbackup.model.FileNameWrapper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static FileHandler instance;
    private static FileNameWrapper fileNameWrapper;

    public static FileHandler instance() {
        if (instance == null) {
            instance = new FileHandler();
        }

        return instance;
    }

    private FileHandler() {
    }

    public static void init(MinecraftServer server) {
        String levelName = server.getWorldData().getLevelSettings().levelName();

        String root = String.format("%s/%s/", levelName, PlayerBackupMod.MOD_ID);
        if (server.isSingleplayer()) {
            root = "saves/" + root;
        }

        fileNameWrapper = new FileNameWrapper(root, ".dat");

        LOGGER.info("playerbackup directory set to {}", root);
        boolean mkdirs = new File(root).mkdirs();

        if(mkdirs) {
            LOGGER.warn("{} culd not be created", new File(root).getAbsolutePath());
        }
    }

    public void writeFile(String uuid, CompoundNBT data) throws FileWriteException {

        String name = fileNameWrapper.get(uuid);

        try {
            clear(uuid);
            LOGGER.debug("creating file {}", name);
            File file = new File(name);
            CompressedStreamTools.writeCompressed(data, file);
        } catch (Exception exception) {
            throw new FileWriteException(exception);

        }
    }

    public List<String> getBackupTimeStamps(String uuid) {
        String[] list = new File(fileNameWrapper.getRoot()).list((dist, name) -> name.startsWith(uuid));
        return list != null ? Arrays.stream(list).map(fileNameWrapper::getTimestamp).collect(Collectors.toList()) : Collections.emptyList();
    }

    private void clear(String uuid) throws IOException {
        int backupCountPerPlayer = PlayerBackupConfig.instance().backupCountPerPlayer;

        String[] list = new File(fileNameWrapper.getRoot()).list();
        List<String> files = list != null ? Arrays.stream(list).filter(name -> name.startsWith(uuid)).sorted().collect(Collectors.toList()) : Collections.emptyList();

        while (files.size() >= backupCountPerPlayer) {
            LOGGER.info("maximum of {} backups for player {} reached. Deleting oldest", backupCountPerPlayer, uuid);
            deleteOldest(files);
        }
    }

    private void deleteOldest(List<String> list) throws IOException {
        File file = new File(fileNameWrapper.getRoot() + list.get(0));
        LOGGER.debug("Deleting file {}", file.getAbsolutePath());
        FileUtils.forceDelete(file);
        list.remove(0);
    }

    public CompoundNBT readFile(String uuid, String backupDate) throws FileReadException {
        try {
            String fileName = fileNameWrapper.get(uuid, backupDate);
            return CompressedStreamTools.readCompressed(new File(fileName));

        } catch (Exception exception) {
            throw new FileReadException(exception);
        }
    }

    public CompoundNBT readLatestFile(String uuid) throws NoBackupFoundException, FileReadException {
        List<String> backupFilesForUuid = getBackupTimeStamps(uuid);
        if (backupFilesForUuid.isEmpty()) {
            throw new NoBackupFoundException();

        } else {
            String timestamp = backupFilesForUuid.get(backupFilesForUuid.size() - 1);
            return readFile(uuid, timestamp);
        }
    }
}
