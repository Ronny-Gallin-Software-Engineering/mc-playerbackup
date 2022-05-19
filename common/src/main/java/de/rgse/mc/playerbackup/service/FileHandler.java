package de.rgse.mc.playerbackup.service;

import de.rgse.mc.playerbackup.PlayerbackupMod;
import de.rgse.mc.playerbackup.config.PlayerBackupConfig;
import de.rgse.mc.playerbackup.exceptions.FileReadException;
import de.rgse.mc.playerbackup.exceptions.FileWriteException;
import de.rgse.mc.playerbackup.exceptions.NoBackupFoundException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.server.MinecraftServer;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileHandler {

    private static final Logger LOGGER = LogManager.getLogger();

    private static FileHandler instance;
    private static FileNameService fileNameWrapper;

    static FileHandler instance() {
        if (instance == null) {
            instance = new FileHandler();
        }

        return instance;
    }

    private FileHandler() {
    }

    public static void init(MinecraftServer server) {

        String levelName = server.getWorldData().getLevelName();

        String root = String.format("%s/%s/", levelName, PlayerbackupMod.MOD_ID);
        if (server.isSingleplayer()) {
            root = "saves/" + root;
        }

        fileNameWrapper = new FileNameService(root, ".dat");

        LOGGER.info("playerbackup directory set to {}", root);
        new File(root).mkdirs();
    }

    void writeFile(String uuid, CompoundTag data) throws FileWriteException {

        String name = fileNameWrapper.get(uuid);

        try {
            clear(uuid);
            LOGGER.debug("creating file {}", name);
            File file = new File(name);
            writeTag(data, file);
        } catch (Exception exception) {
            throw new FileWriteException(exception);

        }
    }

    List<String> getBackups() {
        String[] list = new File(fileNameWrapper.getRoot()).list();
        return list != null ? Arrays.asList(list) : Collections.emptyList();
    }

    CompoundTag readFile(String uuid, String backupDate) throws FileReadException {
        try {
            String fileName = fileNameWrapper.get(uuid, backupDate);
            return readTag(new File(fileName));

        } catch (Exception exception) {
            throw new FileReadException(exception);
        }
    }

    CompoundTag readLatestFile(String uuid) throws NoBackupFoundException, FileReadException {
        List<String> backupFilesForUuid = getBackupTimeStamps(uuid);
        if (backupFilesForUuid.isEmpty()) {
            throw new NoBackupFoundException();

        } else {
            String timestamp = backupFilesForUuid.stream().sorted().reduce((first, second) -> second).orElse(null);
            return readFile(uuid, timestamp);
        }
    }

    void delete(String uuid, String timestamp) throws IOException {
        String fileName = fileNameWrapper.get(uuid, timestamp);
        FileUtils.forceDelete(new File(fileName));
    }

    void deleteLatest(String uuid) throws IOException {
        List<String> backupFilesForUuid = getBackupTimeStamps(uuid);

        if (!backupFilesForUuid.isEmpty()) {
            String timestamp = backupFilesForUuid.get(backupFilesForUuid.size() - 1);
            delete(uuid, timestamp);
        }
    }

    private List<String> getBackupTimeStamps(String uuid) {
        String[] list = new File(fileNameWrapper.getRoot()).list((dist, name) -> name.startsWith(uuid));
        return list != null ? Arrays.stream(list).map(fileNameWrapper::getTimestamp).collect(Collectors.toList()) : Collections.emptyList();
    }

    private void clear(String uuid) throws IOException {
        int backupCountPerPlayer = PlayerBackupConfig.instance().getBackupCountPerPlayer();

        String[] list = new File(fileNameWrapper.getRoot()).list();
        List<String> files = list != null ? Arrays.stream(list).filter(name -> name.startsWith(uuid)).sorted().toList() : Collections.emptyList();

        if (backupCountPerPlayer > 0) {
            while (files.size() >= backupCountPerPlayer) {
                LOGGER.info("maximum of {} backups for player {} reached. Deleting oldest", backupCountPerPlayer, uuid);
                deleteOldest(files);
            }
        }
    }

    private void deleteOldest(List<String> list) throws IOException {
        File file = new File(fileNameWrapper.getRoot() + list.get(0));
        LOGGER.debug("Deleting file {}", file.getAbsolutePath());
        FileUtils.forceDelete(file);
        list.remove(0);
    }

    public void writeTag(CompoundTag tag, File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            DataOutput dataOutput = new DataOutputStream(fileOutputStream);
            tag.write(dataOutput);

        } catch (IOException e) {
            LOGGER.error("unable to persist compound tag to {}", file.getAbsolutePath());
            LOGGER.error(e);
        }
    }

    public CompoundTag readTag(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            DataInput dataInput = new DataInputStream(fileInputStream);
            return CompoundTag.TYPE.load(dataInput, 0, NbtAccounter.UNLIMITED);

        }
    }
}
