package natjom.neopunk.item;

import net.minecraft.world.item.Item;

public class WearableItem extends Item {
    private final WearableType type;

    public WearableItem(Properties props, WearableType type) {
        super(props);
        this.type = type;
    }

    public WearableType getType() {
        return type;
    }
}
