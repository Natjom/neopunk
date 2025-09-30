package natjom.neopunk.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class MyNetwork {
    private static final String PROTOCOL = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("neopunk", "main"),
            () -> PROTOCOL, PROTOCOL::equals, PROTOCOL::equals
    );

    public static void register() {
        int id = 0;
        CHANNEL.registerMessage(id++, OpenCustomInventoryPacket.class,
                OpenCustomInventoryPacket::encode,
                OpenCustomInventoryPacket::decode,
                OpenCustomInventoryPacket::handle);
    }
}
