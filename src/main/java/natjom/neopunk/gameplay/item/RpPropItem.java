package natjom.neopunk.gameplay.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ToolAction;

public class RpPropItem extends Item {
    public RpPropItem(Properties props) { super(props); }

    @Override public boolean isEnchantable(ItemStack stack) { return false; }
    @Override public boolean isFoil(ItemStack stack) { return false; }

    @Override public boolean canPerformAction(ItemStack stack, ToolAction toolAction) { return false; }
}
