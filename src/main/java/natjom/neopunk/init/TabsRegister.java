package natjom.neopunk.init;

import natjom.neopunk.core.Neopunk;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public final class TabsRegister {
    private TabsRegister() {}

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Neopunk.MODID);

    public static final RegistryObject<CreativeModeTab> NEOPUNK =
            TABS.register("neopunk", () ->
                    CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.neopunk.neopunk"))
                            .icon(() -> ItemsRegister.ICON.get().getDefaultInstance())
                            .displayItems((params, output) -> {
                                output.accept(ItemsRegister.ICON.get()); // Mandatory
                                output.accept(ItemsRegister.GLASSES.get());
                                output.accept(ItemsRegister.HAT.get());
                                output.accept(ItemsRegister.MASK.get());
                                output.accept(ItemsRegister.JACKET.get());
                                output.accept(ItemsRegister.SHIRT.get());
                                output.accept(ItemsRegister.KEVLAR.get());
                                output.accept(ItemsRegister.PANTS.get());
                                output.accept(ItemsRegister.UNDERWEAR.get());
                                output.accept(ItemsRegister.LEG_ARMOR.get());
                                output.accept(ItemsRegister.BOOTS.get());
                                output.accept(ItemsRegister.CHARM.get());
                            }).build()
            );

    public static void register(IEventBus modBus) {
        TABS.register(modBus);
    }
}
