package natjom.neopunk.network;

import natjom.neopunk.chat.ChatManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class PacketSendChatMessage {
    private final String message;

    public PacketSendChatMessage(String message) {
        this.message = message;
    }

    public PacketSendChatMessage(FriendlyByteBuf buf) {
        this.message = buf.readUtf(256);
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(message);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ChatManager.handleMessage(ctx.get().getSender(), message);
        });
        ctx.get().setPacketHandled(true);
    }

}
