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
                                // output.accept(ItemsRegister.ICON.get()); // Mandatory

                            })
                            .build()
            );

    public static void register(IEventBus modBus) {
        TABS.register(modBus);
    }
}
