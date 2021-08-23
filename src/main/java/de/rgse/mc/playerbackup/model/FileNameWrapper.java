package de.rgse.mc.playerbackup.model;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileNameWrapper {

    public static final String TIMESTAMP_PATTERN = "yy_MM_dd_kkmmss";

    private static final Pattern PATTERN = Pattern.compile("^.*(\\d{2}_\\d{2}_\\d{2}_\\d{6}).dat");
    private static final String FILE_NAME_FORMAT = "%s%s-%s.%s";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_PATTERN);

    private final String root;
    private final String extension;

    public FileNameWrapper(String root, String extension) {
        this.root = root;
        this.extension = extension.charAt(0) == '.' ? extension.substring(1) : extension;
    }

    public String get(String uuid) {
        String timestamp = ZonedDateTime.now().format(FORMATTER);
        return get(uuid, timestamp);
    }

    public String get(String uuid, String timestamp) {
        return String.format(FILE_NAME_FORMAT, root, uuid, timestamp, extension);
    }

    public String getRoot() {
        return root;
    }

    public String getTimestamp(String filename) {
        Matcher matcher = PATTERN.matcher(filename);
        return matcher.find() ? matcher.group(1) : null;
    }
}
