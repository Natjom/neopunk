package natjom.neopunk.ui.tab;

import natjom.neopunk.chat.ChatManager;
import natjom.neopunk.security.perm.Perms;
import natjom.neopunk.security.perm.Role;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.Optional;

public class TabDisplay {

    private static final Logger LOGGER = LoggerFactory.getLogger("NeoPunk-Tab");

    public static void updateFor(ServerPlayer player) {
        if (player == null || player.level().isClientSide) return;

        Component display = buildTabDisplayName(player);

        try {
            Field f = ServerPlayer.class.getDeclaredField("tabListDisplayName");
            f.setAccessible(true);
            f.set(player, display);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.warn("Impossible de setter tabListDisplayName", e);
            return;
        }

        ClientboundPlayerInfoUpdatePacket pkt = new ClientboundPlayerInfoUpdatePacket(
                ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME,
                player
        );
        for (ServerPlayer viewer : player.server.getPlayerList().getPlayers()) {
            viewer.connection.send(pkt);
        }

        refreshHeaderFooterForAll(player);
    }

    public static void updateAll(Iterable<ServerPlayer> players) {
        for (ServerPlayer p : players) updateFor(p);
    }

    private static Component buildTabDisplayName(ServerPlayer p) {
        MutableComponent badges = buildBadges(p);

        String baseName = ChatManager.getNickname(p.getUUID())
                .orElse(p.getGameProfile().getName());

        ChatFormatting color = ChatFormatting.WHITE;
        Optional<String> optColorStr = ChatManager.getColorName(p.getUUID());
        if (optColorStr.isPresent()) {
            try { color = ChatFormatting.valueOf(optColorStr.get().toUpperCase()); }
            catch (IllegalArgumentException ignored) { color = ChatFormatting.WHITE; }
        }

        MutableComponent nickComp = Component.literal(baseName)
                .setStyle(Style.EMPTY.withColor(color));

        return Component.empty().append(badges).append(nickComp);
    }

    private static MutableComponent buildBadges(ServerPlayer p) {
        EnumSet<Role> roles = Perms.rolesOf(p.getUUID());
        MutableComponent out = Component.empty();

        if (roles.contains(Role.MJ)) {
            out = out.append(Component.literal("[MJ]"))
                    .append(Component.literal(" "));
        }
        else if (roles.contains(Role.SUPPORT)) {
            out = out.append(Component.literal("[S]").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(" "));
        }
        else if (roles.contains(Role.BUILDER)) {
            out = out.append(Component.literal("[B]").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(" "));
        }

        return out;
    }


    public static void setHeaderFooter(ServerPlayer player) {
        Component header = Component.literal("§6NeoPunk RP\n");

        Component footer = Component.literal("");

        player.connection.send(new net.minecraft.network.protocol.game.ClientboundTabListPacket(header, footer));
    }

    public static void refreshHeaderFooterForAll(ServerPlayer anyOnline) {
        for (ServerPlayer v : anyOnline.server.getPlayerList().getPlayers()) {
            setHeaderFooter(v);
        }
    }
}
