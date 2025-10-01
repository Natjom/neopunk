package natjom.neopunk.ui.tab;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static natjom.neopunk.core.Neopunk.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class TabEvents {

    @SubscribeEvent
    public static void onLogin(PlayerLoggedInEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer sp)) return;
        TabDisplay.updateFor(sp);
        TabDisplay.setHeaderFooter(sp);
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer sp) || sp.level().isClientSide) return;
        TabDisplay.updateFor(sp);
        TabDisplay.setHeaderFooter(sp);
    }

    @SubscribeEvent
    public static void onDimChange(PlayerEvent.PlayerChangedDimensionEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer sp)) return;
        TabDisplay.updateFor(sp);
        TabDisplay.setHeaderFooter(sp);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        if (e.getEntity() instanceof ServerPlayer sp && !sp.level().isClientSide) {
            TabDisplay.updateFor(sp);
            TabDisplay.setHeaderFooter(sp);
        }
    }
}
