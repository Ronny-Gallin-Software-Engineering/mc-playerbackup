package de.rgse.mc.playerbackup.exceptions;

public class FileWriteException extends RuntimeException {

    public FileWriteException(Exception cause) {
        super(cause);
    }
}
