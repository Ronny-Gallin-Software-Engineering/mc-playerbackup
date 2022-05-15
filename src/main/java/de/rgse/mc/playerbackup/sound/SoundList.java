package de.rgse.mc.playerbackup.sound;

import de.rgse.mc.playerbackup.PlayerBackupMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundList {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PlayerBackupMod.MOD_ID);

    public static final RegistryObject<SoundEvent> RESTORE_SOUND = SOUNDS.register("restored", () -> new SoundEvent(new ResourceLocation(PlayerBackupMod.MOD_ID, "restored")));

    private SoundList() {
    }
}

