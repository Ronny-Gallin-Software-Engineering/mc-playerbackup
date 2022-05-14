package de.rgse.mc.playerbackup.network.message;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import de.rgse.mc.playerbackup.network.client.ClientPlayerBackupPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Supplier;

public class SyncAvailableBackupsMessage {

    private static final Type LIST_TYPE = new TypeToken<List<String>>() {
    }.getType();

    private static final Gson GSON = new Gson();
    private final List<String> backups;

    public SyncAvailableBackupsMessage(List<String> backups) {
        this.backups = backups;
    }

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

    public static void handle(SyncAvailableBackupsMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> ClientPlayerBackupPacketHandler.handleSyncBackupsMessage(message)));
        context.setPacketHandled(true);
    }
}
