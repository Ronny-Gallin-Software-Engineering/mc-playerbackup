package de.rgse.mc.playerbackup.exceptions;

public class NoBackupFoundException extends Exception {

    public NoBackupFoundException() {
        super("no backup found for player");
    }
}
