package natjom.neopunk.init;

import natjom.neopunk.ui.CustomInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NeopunkMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, "neopunk");

    public static final RegistryObject<MenuType<CustomInventoryMenu>> CUSTOM_INVENTORY =
            MENUS.register("custom_inventory",
                    () -> IForgeMenuType.create(CustomInventoryMenu::new));
}
