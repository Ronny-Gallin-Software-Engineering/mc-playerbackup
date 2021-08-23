package de.rgse.mc.playerbackup.permissions;

import com.google.gson.annotations.Expose;
import de.rgse.mc.playerbackup.PlayerBackupMod;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Permissions {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PERMISSION_NODE_PREFIX = PlayerBackupMod.MOD_ID + ".";

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
                new Permission("self_restore", DefaultPermissionLevel.OP, "Allows the player to restore the own backup"),
                new Permission("restore", DefaultPermissionLevel.OP, "Allows the player to restore the backup of any other player"),
                new Permission("self_backup", DefaultPermissionLevel.ALL, "Allows the player to create a backup of it's own"),
                new Permission("backup", DefaultPermissionLevel.OP, "Allows the player to create a backup of any other player")
        );
    }

    private Permissions(Permission selfRestore, Permission restore, Permission selfBackup, Permission backup) {
        this.selfRestore = selfRestore;
        this.restore = restore;
        this.selfBackup = selfBackup;
        this.backup = backup;
    }

    public void register() {
        PermissionAPI.registerNode(PERMISSION_NODE_PREFIX +selfRestore.permissionId, selfRestore.permissionLevel, selfRestore.description);
        PermissionAPI.registerNode(PERMISSION_NODE_PREFIX +restore.permissionId, restore.permissionLevel, restore.description);
        PermissionAPI.registerNode(PERMISSION_NODE_PREFIX +selfBackup.permissionId, selfBackup.permissionLevel, selfBackup.description);
        PermissionAPI.registerNode(PERMISSION_NODE_PREFIX +backup.permissionId, backup.permissionLevel, backup.description);

        LOGGER.info("permissions registered");
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
        private DefaultPermissionLevel permissionLevel;
        @Expose
        private String description;

        private Permission(String permissionId, DefaultPermissionLevel permissionLevel, String description) {
            this.permissionId = permissionId;
            this.permissionLevel = permissionLevel;
            this.description = description;
        }

        Permission() {
        }

        public boolean permitted(CommandSource commandSource) {
            return commandSource.getServer().isSingleplayer() || !(commandSource.getEntity() instanceof PlayerEntity) || PermissionAPI.hasPermission((ServerPlayerEntity) commandSource.getEntity(), PERMISSION_NODE_PREFIX + this.permissionId);
        }
    }
}
