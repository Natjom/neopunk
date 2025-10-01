package natjom.neopunk.ui;

import natjom.neopunk.capabilities.PlayerPocket;
import natjom.neopunk.init.NeopunkMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;

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

        for (int row = 0; row < clothingRows; row++) {
            for (int col = 0; col < columns; col++) {
                int x = startX + col * (slotSize + spacing);
                int y = startY + row * (slotSize + spacing);
                this.addSlot(new SlotItemHandler(handler, slotIndex++, x, y));
            }
        }

        int mainCol = 1;
        int mainX = startX + mainCol * (slotSize + spacing);
        int mainY = startY + clothingRows * (slotSize + spacing);
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
