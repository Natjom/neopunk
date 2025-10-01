// OpenCustomInventoryPacket.java
package natjom.neopunk.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class PacketOpenCustomInventory {
    public PacketOpenCustomInventory() {}

    public static PacketOpenCustomInventory decode(FriendlyByteBuf buf) {
        return new PacketOpenCustomInventory();
    }

    public void encode(FriendlyByteBuf buf) {}

    public static void handle(PacketOpenCustomInventory msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = ctx.get().getSender();
            if (player != null && !player.isCreative()) {
                net.minecraftforge.network.NetworkHooks.openScreen(
                        player,
                        new net.minecraft.world.SimpleMenuProvider(
                                (id, inv, p) -> new natjom.neopunk.ui.CustomInventoryMenu(id, inv),
                                net.minecraft.network.chat.Component.literal("Agent")
                        )
                );
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
