package de.rgse.mc.playerbackup.service.client;

import java.util.List;

public class Backups {

    private static Backups instance;

    public static Backups instance() {
        if (instance == null) {
            instance = new Backups();
        }

        return instance;
    }

    private List<String> availableBackups;

    private Backups() {
    }

    public List<String> getAvailableBackups() {
        return availableBackups;
    }

    public void setAvailableBackups(List<String> availableBackups) {
        this.availableBackups = availableBackups;
    }
}
