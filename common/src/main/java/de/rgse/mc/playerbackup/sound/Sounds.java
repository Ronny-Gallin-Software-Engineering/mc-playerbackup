package de.rgse.mc.playerbackup.sound;

import de.rgse.mc.playerbackup.PlayerbackupMod;
import me.shedaniel.architectury.registry.DeferredRegister;
import me.shedaniel.architectury.registry.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class Sounds {
    private static final ResourceLocation LOCATION = new ResourceLocation(PlayerbackupMod.MOD_ID, "restored");
    private static final DeferredRegister<SoundEvent> SOUNDS_REGISTRY = DeferredRegister.create(PlayerbackupMod.MOD_ID, Registry.SOUND_EVENT_REGISTRY);

    public static final RegistrySupplier<SoundEvent> RESTORE_SOUND = SOUNDS_REGISTRY.register(LOCATION, () -> new SoundEvent(LOCATION));

    private Sounds() {
    }

    public static void init() {
        SOUNDS_REGISTRY.register();
    }
}

