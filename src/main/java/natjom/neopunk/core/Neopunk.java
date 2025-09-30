package natjom.neopunk.core;

import com.mojang.logging.LogUtils;
import natjom.neopunk.init.NeopunkMenus;
import natjom.neopunk.network.MyNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import natjom.neopunk.init.ItemsRegister;
import natjom.neopunk.init.TabsRegister;

@Mod(Neopunk.MODID)
public class Neopunk {

    public static final String MODID = "neopunk";

    private static final Logger LOGGER = LogUtils.getLogger();


    public Neopunk() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();



        modEventBus.addListener(this::commonSetup);

        ItemsRegister.register(modEventBus);
        TabsRegister.register(modEventBus);

        NeopunkMenus.MENUS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        MyNetwork.register();

    }

    private void commonSetup(final FMLCommonSetupEvent event) {}

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

}
