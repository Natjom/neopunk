package natjom.neopunk.ui;

import natjom.neopunk.item.WearableItem;
import natjom.neopunk.item.WearableType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

public class FilteredWearableSlot extends SlotItemHandler {
    private final WearableType allowedType;

    public FilteredWearableSlot(IItemHandlerModifiable handler, int index, int x, int y, WearableType allowedType) {
        super(handler, index, x, y);
        this.allowedType = allowedType;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        if (stack.getItem() instanceof WearableItem wearable) {
            return wearable.getType() == allowedType;
        }
        return false;
    }
}
