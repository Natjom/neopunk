package natjom.neopunk.chat;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import natjom.neopunk.network.MyNetwork;
import natjom.neopunk.network.PacketBroadcastChatMessage;
import natjom.neopunk.security.perm.Perms;
import natjom.neopunk.security.perm.Role;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

public class ChatManager {
    public static class NickData {
        public String nickname;
        public String color;

        public NickData(String nickname, String color) {
            this.nickname = nickname;
            this.color = color;
        }
    }

    private static final Map<UUID, NickData> nickData = new HashMap<>();
    private static final Gson GSON = new Gson();
    private static final File FILE = new File("nicknames.json");

    public static void setNickname(UUID uuid, String nickname) {
        NickData data = nickData.getOrDefault(uuid, new NickData(nickname, "white"));
        data.nickname = nickname;
        nickData.put(uuid, data);
        saveNicknames();
    }

    public static void setColor(UUID uuid, String colorName) {
        NickData data = nickData.getOrDefault(uuid, new NickData(null, colorName));
        data.color = colorName;
        nickData.put(uuid, data);
        saveNicknames();
    }

    public static void removeNickname(UUID uuid) {
        nickData.remove(uuid);
        saveNicknames();
    }

    public static Optional<String> getNickname(UUID uuid) {
        NickData data = nickData.get(uuid);
        return data != null ? Optional.ofNullable(data.nickname) : Optional.empty();
    }

    public static Optional<String> getColorName(UUID uuid) {
        NickData data = nickData.get(uuid);
        return data != null ? Optional.ofNullable(data.color) : Optional.empty();
    }

    public static String resolveName(ServerPlayer player) {
        return getNickname(player.getUUID()).orElse(player.getName().getString());
    }

    public static ChatFormatting resolveColor(ServerPlayer player) {
        return getColor(player.getUUID());
    }

    public static ChatFormatting getColor(UUID uuid) {
        NickData data = nickData.get(uuid);
        if (data != null && data.color != null) {
            try {
                return ChatFormatting.valueOf(data.color.toUpperCase());
            } catch (IllegalArgumentException ignored) { }
        }
        return ChatFormatting.WHITE;
    }


    public static void handleMessage(ServerPlayer player, String rawMessage) {
        if (player.getServer() == null) return;

        player.getServer().execute(() -> {
            if (rawMessage.startsWith("!")) {
                String content = rawMessage.substring(1).trim();
                if (content.isEmpty()) {
                    player.sendSystemMessage(Component.literal("Message MJ vide. Écris après le !")
                            .withStyle(ChatFormatting.GRAY));
                    return;
                }

                String nick = resolveName(player);
                ChatFormatting nickColor = resolveColor(player);

                Component staffMsg = Component.literal("[MJ] ").withStyle(ChatFormatting.DARK_RED)
                        .append(Component.literal(nick).withStyle(nickColor))
                        .append(Component.literal(" » ").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(content).withStyle(ChatFormatting.RED));

                boolean senderIsStaff = Perms.has(player.createCommandSourceStack(), Role.MJ);
                List<ServerPlayer> staff = onlineStaff();

                if (senderIsStaff) {
                    for (ServerPlayer p : staff) {
                        MyNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> p),
                                new PacketBroadcastChatMessage(staffMsg));
                    }
                } else {
                    for (ServerPlayer p : staff) {
                        MyNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> p),
                                new PacketBroadcastChatMessage(staffMsg));
                    }

                    int count = staff.size();
                    if (count == 0) {
                        player.sendSystemMessage(Component.literal("Aucun MJ connecté. Ton message n’a pas pu être remis.")
                                .withStyle(ChatFormatting.YELLOW));
                    } else {
                        player.sendSystemMessage(Component.literal("Message envoyé aux MJ en ligne (" + count + ").")
                                .withStyle(ChatFormatting.GREEN));
                    }
                }
                return;
            }


            if (rawMessage.startsWith("?")) {
                String content = rawMessage.substring(1).trim();
                if (content.isEmpty()) {
                    player.sendSystemMessage(Component.literal("Message Support vide. Écris après le ?")
                            .withStyle(ChatFormatting.GRAY));
                    return;
                }

                String nick = resolveName(player);
                ChatFormatting nickColor = resolveColor(player);

                // Prefix différent et couleurs distinctes du canal MJ
                Component supportMsg = Component.literal("[S] ").withStyle(ChatFormatting.DARK_AQUA)
                        .append(Component.literal(nick).withStyle(nickColor))
                        .append(Component.literal(" » ").withStyle(ChatFormatting.GRAY))
                        .append(Component.literal(content).withStyle(ChatFormatting.AQUA));

                boolean senderIsSupport = Perms.has(player.createCommandSourceStack(), Role.SUPPORT);
                List<ServerPlayer> supports = onlineSupport();

                for (ServerPlayer p : supports) {
                    MyNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> p),
                            new PacketBroadcastChatMessage(supportMsg));
                }

                if (!senderIsSupport) {
                    int count = supports.size();
                    if (count == 0) {
                        player.sendSystemMessage(Component.literal("Aucun Support connecté. Ton message n’a pas pu être remis.")
                                .withStyle(ChatFormatting.YELLOW));
                    } else {
                        player.sendSystemMessage(Component.literal("Message envoyé aux Supports en ligne (" + count + ").")
                                .withStyle(ChatFormatting.GREEN));
                    }
                }
                return;
            }

            String name = resolveName(player);
            ChatFormatting nameColor = resolveColor(player);

            String message = rawMessage;
            boolean isEmote = message.startsWith("*");
            int range = 15;
            ChatFormatting messageColor = ChatFormatting.WHITE;

            if (message.startsWith("*&")) {
                isEmote = true;
                range = 35;
                messageColor = ChatFormatting.GOLD;
                message = message.substring(2);
            } else if (message.startsWith("*\"")) {
                isEmote = true;
                range = 3;
                messageColor = ChatFormatting.GREEN;
                message = message.substring(2);
            } else if (message.startsWith("*")) {
                isEmote = true;
                messageColor = ChatFormatting.DARK_GREEN;
                message = message.substring(1);
            } else if (message.startsWith("&")) {
                isEmote = false;
                range = 35;
                messageColor = ChatFormatting.YELLOW;
                message = message.substring(1);
            } else if (message.startsWith("\"")) {
                isEmote = false;
                range = 3;
                messageColor = ChatFormatting.LIGHT_PURPLE;
                message = message.substring(1);
            }

            Component formattedMessage;
            if (isEmote) {
                formattedMessage = Component.literal(name).withStyle(nameColor)
                        .append(" ")
                        .append(Component.literal(message).withStyle(messageColor));
            } else {
                formattedMessage = Component.literal(name).withStyle(nameColor)
                        .append(": ")
                        .append(Component.literal(message).withStyle(messageColor));
            }

            int finalRange = range;
            Component finalMessage = formattedMessage;

            player.level().players().stream()
                    .filter(p -> p instanceof ServerPlayer)
                    .map(p -> (ServerPlayer) p)
                    .filter(p -> finalRange <= 0 || p.distanceToSqr(player) <= finalRange * finalRange)
                    .forEach(p -> MyNetwork.CHANNEL.send(
                            PacketDistributor.PLAYER.with(() -> p),
                            new PacketBroadcastChatMessage(finalMessage)
                    ));
        });
    }

    public static void loadNicknames() {
        try {
            if (FILE.exists()) {
                Map<String, NickData> data = GSON.fromJson(new FileReader(FILE), new TypeToken<Map<String, NickData>>(){}.getType());
                nickData.clear();
                data.forEach((uuid, value) -> nickData.put(UUID.fromString(uuid), value));
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des nicknames : " + e.getMessage());
        }
    }

    public static void saveNicknames() {
        try (FileWriter writer = new FileWriter(FILE)) {
            Map<String, NickData> data = new HashMap<>();
            nickData.forEach((uuid, val) -> data.put(uuid.toString(), val));
            GSON.toJson(data, writer);
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde des nicknames : " + e.getMessage());
        }
    }

    public static Optional<ServerPlayer> getPlayerByName(String name) {
        return ServerLifecycleHooks.getCurrentServer()
                .getPlayerList()
                .getPlayers()
                .stream()
                .filter(player -> player.getName().getString().equalsIgnoreCase(name))
                .findFirst();
    }

    private static List<ServerPlayer> onlineStaff() {
        return ServerLifecycleHooks.getCurrentServer()
                .getPlayerList()
                .getPlayers()
                .stream()
                .filter(p -> Perms.has(p.createCommandSourceStack(), Role.MJ))
                .toList();
    }

    private static List<ServerPlayer> onlineSupport() {
        return ServerLifecycleHooks.getCurrentServer()
                .getPlayerList()
                .getPlayers()
                .stream()
                .filter(p -> Perms.has(p.createCommandSourceStack(), Role.SUPPORT))
                .toList();
    }

    private static void sendToStaff(Component msg) {
        for (ServerPlayer p : onlineStaff()) {
            MyNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> p),
                    new PacketBroadcastChatMessage(msg));
        }
    }

    private static void sendToSupport(Component msg) {
        for (ServerPlayer p : onlineSupport()) {
            MyNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> p),
                    new PacketBroadcastChatMessage(msg));
        }
    }

}
