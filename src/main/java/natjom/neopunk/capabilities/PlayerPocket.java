package natjom.neopunk.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;

@Mod.EventBusSubscriber(modid = "neopunk")
public class PlayerPocket {
    public static final Capability<ItemStackHandler> POCKET =
            CapabilityManager.get(new CapabilityToken<>() {});

    public static class Provider implements ICapabilitySerializable<CompoundTag> {
        private final ItemStackHandler handler = new ItemStackHandler(12);
        private final LazyOptional<ItemStackHandler> optional = LazyOptional.of(() -> handler);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == POCKET ? optional.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return handler.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            handler.deserializeNBT(nbt);
        }
    }

    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Object> event) {
        if (event.getObject() instanceof Player) {
            event.addCapability(new ResourceLocation("neopunk", "pocket"), new Provider());
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        event.getOriginal().reviveCaps();

        event.getOriginal().getCapability(POCKET).ifPresent(oldInv -> {
            event.getEntity().getCapability(POCKET).ifPresent(newInv -> {
                newInv.deserializeNBT(oldInv.serializeNBT());
            });
        });

        event.getOriginal().invalidateCaps();
    }
}
