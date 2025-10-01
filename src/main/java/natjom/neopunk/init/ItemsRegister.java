package natjom.neopunk.init;
import natjom.neopunk.core.Neopunk;
import natjom.neopunk.gameplay.item.RpPropItem;
import natjom.neopunk.item.WearableItem;
import natjom.neopunk.item.WearableType;
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

    public static final RegistryObject<Item> GLASSES = ITEMS.register("glasses", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.EYES));

    public static final RegistryObject<Item> HAT = ITEMS.register("hat", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.HAT));

    public static final RegistryObject<Item> MASK = ITEMS.register("mask", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.MASK));

    public static final RegistryObject<Item> SHIRT = ITEMS.register("shirt", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.TOP));

    public static final RegistryObject<Item> JACKET = ITEMS.register("jacket", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.OVER_TOP));

    public static final RegistryObject<Item> KEVLAR = ITEMS.register("kevlar", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.SPECIAL_TORSO));

    public static final RegistryObject<Item> PANTS = ITEMS.register("pants", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.LEGS));

    public static final RegistryObject<Item> UNDERWEAR = ITEMS.register("underwearm", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.UNDERWEAR));

    public static final RegistryObject<Item> LEG_ARMOR = ITEMS.register("leg_armor", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.SPECIAL_LEGS));

    public static final RegistryObject<Item> BOOTS = ITEMS.register("boots", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.FEET));

    public static final RegistryObject<Item> CHARM = ITEMS.register("charm", () ->
            new WearableItem(new Item.Properties().stacksTo(1), WearableType.ACCESSORY));

    public static void register(IEventBus modBus) {
        ITEMS.register(modBus);
    }
}

