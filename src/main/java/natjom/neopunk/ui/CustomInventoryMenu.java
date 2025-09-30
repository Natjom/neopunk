package natjom.neopunk.ui;

import natjom.neopunk.init.NeopunkMenus;
import natjom.neopunk.capabilities.PocketInventory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class CustomInventoryMenu extends AbstractContainerMenu {
    private final Player player;
    private final PocketInventory pocket;

    public CustomInventoryMenu(int id, Inventory playerInv) {
        super(NeopunkMenus.CUSTOM_INVENTORY.get(), id);
        this.player = playerInv.player;
        this.pocket = new PocketInventory();

        this.addSlot(new Slot(playerInv, 0, 44, 35));

        final SimpleContainer pocket = new SimpleContainer(1);

        this.addSlot(new Slot(pocket, 0, 98, 35));
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
