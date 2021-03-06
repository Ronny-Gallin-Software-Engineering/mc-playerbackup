package de.rgse.mc.playerbackup.network.message;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import de.rgse.mc.playerbackup.network.client.ClientPlayerBackupPacketHandler;
import dev.architectury.networking.NetworkManager;
import net.minecraft.network.FriendlyByteBuf;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

public record SyncAvailableBackupsMessage(List<String> backups) {

    private static final Type LIST_TYPE = new TypeToken<List<String>>() {
    }.getType();

    private static final Gson GSON = new Gson();

    public List<String> getBackups() {
        return backups;
    }

    public static void encode(SyncAvailableBackupsMessage message, FriendlyByteBuf buffer) {
        buffer.writeUtf(GSON.toJson(message.backups));
    }

    public static SyncAvailableBackupsMessage decode(FriendlyByteBuf buffer) {
        String json = buffer.readUtf();
        return new SyncAvailableBackupsMessage(GSON.fromJson(json, LIST_TYPE));
    }

    public void handle(Supplier<NetworkManager.PacketContext> contextSupplier) {
        NetworkManager.PacketContext context = contextSupplier.get();
        context.queue(() -> ClientPlayerBackupPacketHandler.handleSyncBackupsMessage(this));
    }
}
