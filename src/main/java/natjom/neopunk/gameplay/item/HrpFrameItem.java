package natjom.neopunk.gameplay.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class HrpFrameItem extends Item {

    public HrpFrameItem(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        Direction face = ctx.getClickedFace();
        BlockPos placePos = ctx.getClickedPos().relative(face);
        ItemStack stack = ctx.getItemInHand();

        if (!ctx.getPlayer().mayUseItemAt(placePos, face, stack)) {
            return InteractionResult.FAIL;
        }

        ItemFrame frame = new ItemFrame(level, placePos, face);
        frame.setInvisible(true);

        if (!frame.survives()) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide) {
            level.addFreshEntity(frame);
            stack.shrink(1);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
