package natjom.neopunk.ui;

import natjom.neopunk.capabilities.PlayerPocket;
import natjom.neopunk.init.NeopunkMenus;
import natjom.neopunk.item.WearableType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

public class CustomInventoryMenu extends AbstractContainerMenu {
    private final Player player;

    public CustomInventoryMenu(int id, Inventory playerInv) {
        super(NeopunkMenus.CUSTOM_INVENTORY.get(), id);
        this.player = playerInv.player;

        int columns = 3;
        int clothingRows = 4;
        int slotSize = 18;
        int spacing = 4;

        int gridWidth = columns * slotSize + (columns - 1) * spacing;
        int gridHeight = (clothingRows + 2) * slotSize + (clothingRows + 1) * spacing;

        int startX = (176 - gridWidth) / 2;
        int startY = (166 - gridHeight) / 2;

        IItemHandlerModifiable handler = player.getCapability(PlayerPocket.POCKET)
                .orElseThrow(() -> new IllegalStateException("Pocket capability missing"));

        int slotIndex = 0;

        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX, startY, WearableType.EYES));
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX + (slotSize + spacing), startY, WearableType.HAT));
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX + 2 * (slotSize + spacing), startY, WearableType.MASK));

        int row2 = startY + (slotSize + spacing);
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX, row2, WearableType.OVER_TOP));
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX + (slotSize + spacing), row2, WearableType.TOP));
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX + 2 * (slotSize + spacing), row2, WearableType.SPECIAL_TORSO));

        int row3 = startY + 2 * (slotSize + spacing);
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX, row3, WearableType.LEGS));
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX + (slotSize + spacing), row3, WearableType.UNDERWEAR));
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX + 2 * (slotSize + spacing), row3, WearableType.SPECIAL_LEGS));

        int row4 = startY + 3 * (slotSize + spacing);
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX, row4, WearableType.ACCESSORY));
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX + (slotSize + spacing), row4, WearableType.FEET));
        this.addSlot(new FilteredWearableSlot(handler, slotIndex++, startX + 2 * (slotSize + spacing), row4, WearableType.ACCESSORY));

        int mainCol = 1;
        int mainX = startX + mainCol * (slotSize + spacing);
        int mainY = startY + 5 * (slotSize + spacing); // saute 1 ligne vide
        this.addSlot(new Slot(playerInv, 0, mainX, mainY));
    }

    public CustomInventoryMenu(int id, Inventory playerInv, FriendlyByteBuf buf) {
        this(id, playerInv);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }
}
