package natjom.neopunk.init;
import natjom.neopunk.core.Neopunk;
import natjom.neopunk.gameplay.item.RpPropItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class ItemsRegister {
    private ItemsRegister() {}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Neopunk.MODID);

    public static final RegistryObject<Item> ICON =
            ITEMS.register("icon", () -> new RpPropItem(new Item.Properties().stacksTo(1)));


    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}

