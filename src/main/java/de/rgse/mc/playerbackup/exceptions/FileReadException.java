package de.rgse.mc.playerbackup.exceptions;

public class FileReadException extends RuntimeException {

    public FileReadException(Exception cause) {
        super(cause);
    }
}
