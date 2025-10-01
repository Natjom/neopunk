package natjom.neopunk.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

import static natjom.neopunk.core.Neopunk.MODID;

public class MyNetwork {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, PacketOpenCustomInventory.class,
                PacketOpenCustomInventory::encode,
                PacketOpenCustomInventory::decode,
                PacketOpenCustomInventory::handle);
        CHANNEL.registerMessage(id++, PacketSendChatMessage.class,
                PacketSendChatMessage::toBytes,
                PacketSendChatMessage::new,
                PacketSendChatMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(id++, PacketBroadcastChatMessage.class,
                PacketBroadcastChatMessage::toBytes,
                PacketBroadcastChatMessage::new,
                PacketBroadcastChatMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendTypingUpdateToAll() {

    }
}
