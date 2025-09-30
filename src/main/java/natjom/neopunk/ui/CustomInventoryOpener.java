package natjom.neopunk.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraftforge.network.NetworkHooks;

@Mod.EventBusSubscriber(modid = "neopunk", value = Dist.CLIENT)
public class CustomInventoryOpener {

    @SubscribeEvent
    public static void onInventoryOpen(ScreenEvent.Opening event) {
        if (event.getScreen() instanceof InventoryScreen) {
            Player player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative()) {
                event.setCanceled(true);

                if (player instanceof ServerPlayer serverPlayer) {
                    NetworkHooks.openScreen(serverPlayer, new SimpleMenuProvider(
                            (id, inv, p) -> new CustomInventoryMenu(id, inv),
                            Component.literal("Inventaire Custom")
                    ));
                }
            }
        }
    }
}
