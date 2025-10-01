package natjom.neopunk.chat;

import natjom.neopunk.network.MyNetwork;
import natjom.neopunk.network.PacketSendChatMessage;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static natjom.neopunk.core.Neopunk.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ChatEventHandler {

    @SubscribeEvent
    public static void onClientChat(ClientChatEvent event) {
        String message = event.getMessage();
        MyNetwork.CHANNEL.sendToServer(new PacketSendChatMessage(message));
        event.setCanceled(true);
    }
}
