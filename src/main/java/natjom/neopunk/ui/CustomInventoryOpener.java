package natjom.neopunk.ui;

import natjom.neopunk.network.MyNetwork;
import natjom.neopunk.network.OpenCustomInventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = "neopunk", value = Dist.CLIENT)
public class CustomInventoryOpener {

    @SubscribeEvent
    public static void onInventoryOpen(ScreenEvent.Opening event) {
        if (event.getScreen() instanceof InventoryScreen) {
            var player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        var mc = Minecraft.getInstance();
        if (mc.player != null && !mc.player.isCreative()) {
            if (mc.screen == null) {
                if (mc.options.keyInventory.matches(event.getKey(), event.getScanCode())
                        && event.getAction() == GLFW.GLFW_PRESS) {
                    MyNetwork.CHANNEL.sendToServer(new OpenCustomInventoryPacket());
                }
            }
        }
    }

}
