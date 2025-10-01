package natjom.neopunk.capabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class PlayerPocket {
    public static final Capability<IItemHandlerModifiable> POCKET =
            CapabilityManager.get(new CapabilityToken<>() {});

    public static class Provider implements ICapabilityProvider {
        private final ItemStackHandler handler = new ItemStackHandler(12);

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == POCKET ? LazyOptional.of(() -> handler).cast() : LazyOptional.empty();
        }

        public CompoundTag serializeNBT() { return handler.serializeNBT(); }
        public void deserializeNBT(CompoundTag nbt) { handler.deserializeNBT(nbt); }
    }
}
