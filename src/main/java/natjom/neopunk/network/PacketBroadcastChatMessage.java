package natjom.neopunk.network;

import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class PacketBroadcastChatMessage {
    private final Component formattedMessage;

    public PacketBroadcastChatMessage(Component message) {
        this.formattedMessage = message;
    }

    public PacketBroadcastChatMessage(FriendlyByteBuf buf) {
        this.formattedMessage = Component.Serializer.fromJson(buf.readUtf(32767));
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(Component.Serializer.toJson(formattedMessage));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            net.minecraft.client.Minecraft.getInstance().gui.getChat().addMessage(formattedMessage);
        });
        ctx.get().setPacketHandled(true);
    }
}
