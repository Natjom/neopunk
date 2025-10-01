package natjom.neopunk.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "neopunk")
public class GenericHandler {
    @SubscribeEvent
    public static void onRenderHotbar(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay().id().toString().equals("minecraft:hotbar")) {
            Player player = Minecraft.getInstance().player;
            if (player != null && !player.isCreative()) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGuiOverlayEvent.Pre event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && !player.isCreative()) {
            String id = event.getOverlay().id().toString();
            if (id.equals("minecraft:hotbar")
                    || id.equals("minecraft:player_health")
                    || id.equals("minecraft:food_level")
                    || id.equals("minecraft:experience_bar")
                    || id.equals("minecraft:air_level")
                    || id.equals("minecraft:crosshair")) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (!event.player.isCreative()) {
                event.player.getInventory().selected = 0;
            }
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(InputEvent.MouseScrollingEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && !player.isCreative()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && !player.isCreative()) {
            for (int i = 0; i < 9; i++) {
                if (Minecraft.getInstance().options.keyHotbarSlots[i].matches(event.getKey(), event.getScanCode())) {
                    player.getInventory().selected = 0;
                }
            }
        }
    }
}
