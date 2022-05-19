package de.rgse.mc.playerbackup.exceptions;

public class NoPlayerSelectedException extends Exception {

    public NoPlayerSelectedException() {
        super("players not found");
    }
}
