package de.rgse.mc.playerbackup.permissions;

import com.google.gson.annotations.Expose;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Permissions {

    @Expose
    private Permission selfRestore;
    @Expose
    private Permission restore;
    @Expose
    private Permission selfBackup;
    @Expose
    private Permission backup;

    Permissions() {
    }

    public static Permissions createDefaultPermissions() {
        return new Permissions(
                new Permission("self_restore", PermissionLevel.OWNER, "Allows the player to restore the own backup"),
                new Permission("restore", PermissionLevel.OWNER, "Allows the player to restore the backup of any other player"),
                new Permission("self_backup", PermissionLevel.DEFAULT_PLAYER, "Allows the player to create a backup of it's own"),
                new Permission("backup", PermissionLevel.OWNER, "Allows the player to create a backup of any other player")
        );
    }


    private Permissions(Permission selfRestore, Permission restore, Permission selfBackup, Permission backup) {
        this.selfRestore = selfRestore;
        this.restore = restore;
        this.selfBackup = selfBackup;
        this.backup = backup;
    }

    public Permission getSelfRestore() {
        return selfRestore;
    }

    public Permission getRestore() {
        return restore;
    }

    public Permission getSelfBackup() {
        return selfBackup;
    }

    public Permission getBackup() {
        return backup;
    }

    public static class Permission {
        @Expose
        private String permissionId;
        @Expose
        private int permissionLevel;
        @Expose
        private String description;

        private Permission(String permissionId, PermissionLevel permissionLevel, String description) {
            this.permissionId = permissionId;
            this.permissionLevel = permissionLevel.getLevel();
            this.description = description;
        }

        Permission() {
        }

        public String getPermissionId() {
            return permissionId;
        }

        public int getPermissionLevel() {
            return permissionLevel;
        }

        public String getDescription() {
            return description;
        }

        void setPermissionId(String permissionId) {
            this.permissionId = permissionId;
        }

        void setPermissionLevel(int permissionLevel) {
            this.permissionLevel = permissionLevel;
        }

        void setDescription(String description) {
            this.description = description;
        }

        public boolean permitted(CommandSourceStack commandSource) {
            return commandSource.getServer().isSingleplayer() || !(commandSource.getEntity() instanceof ServerPlayer) || commandSource.hasPermission(this.permissionLevel);
        }
    }
}
