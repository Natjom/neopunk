package natjom.neopunk.commands.social;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import natjom.neopunk.chat.ChatManager;
import natjom.neopunk.security.perm.Perms;
import natjom.neopunk.security.perm.Role;
import natjom.neopunk.ui.tab.TabDisplay;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;

import static natjom.neopunk.core.Neopunk.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class NickCommand {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> d = event.getDispatcher();

        d.register(Commands.literal("nick")
                .then(Commands.literal("reset")
                        .executes(ctx -> {
                            ServerPlayer self = ctx.getSource().getPlayerOrException();
                            ChatManager.removeNickname(self.getUUID());
                            ctx.getSource().sendSuccess(() -> Component.literal("Ton pseudo a été réinitialisé."), false);
                            refreshTab(ctx.getSource());
                            return 1;
                        })
                )

                .then(Commands.literal("set")
                        .then(Commands.argument("pseudo", StringArgumentType.greedyString())
                                .executes(ctx -> {
                                    ServerPlayer self = ctx.getSource().getPlayerOrException();
                                    String nick = StringArgumentType.getString(ctx, "pseudo");
                                    ChatManager.setNickname(self.getUUID(), nick);
                                    ctx.getSource().sendSuccess(() -> Component.literal("Ton pseudo est maintenant : " + nick), false);
                                    refreshTab(ctx.getSource());
                                    return 1;
                                })
                        )
                )

                .then(Commands.literal("for")
                        .requires(Perms.require(Role.MJ))
                        .then(Commands.argument("player", GameProfileArgument.gameProfile())
                                .then(Commands.argument("pseudo", StringArgumentType.greedyString())
                                        .executes(ctx -> {
                                            var profiles = GameProfileArgument.getGameProfiles(ctx, "player");
                                            var gp = profiles.iterator().next();
                                            String nick = StringArgumentType.getString(ctx, "pseudo");

                                            var srv = ctx.getSource().getServer();
                                            ServerPlayer target = srv.getPlayerList().getPlayer(gp.getId());
                                            if (target == null) {
                                                ctx.getSource().sendFailure(Component.literal("Joueur hors-ligne ou introuvable."));
                                                return 0;
                                            }

                                            ChatManager.setNickname(target.getUUID(), nick);
                                            ctx.getSource().sendSuccess(() -> Component.literal(
                                                    "Pseudo de " + gp.getName() + " changé en : " + nick), false);
                                            refreshTab(ctx.getSource());
                                            return 1;
                                        })
                                )
                        )
                )

                .then(Commands.literal("color")
                        .then(Commands.argument("color", StringArgumentType.word())
                                .suggests((ctx, b) -> {
                                    List<String> names = Arrays.stream(ChatFormatting.values())
                                            .map(cf -> cf.name().toLowerCase())
                                            .toList();
                                    return SharedSuggestionProvider.suggest(names, b);
                                })
                                .executes(ctx -> {
                                    ServerPlayer self = ctx.getSource().getPlayerOrException();
                                    String colorInput = StringArgumentType.getString(ctx, "color").toUpperCase();
                                    try {
                                        ChatFormatting.valueOf(colorInput);
                                        ChatManager.setColor(self.getUUID(), colorInput);
                                        ctx.getSource().sendSuccess(() -> Component.literal(
                                                "🎨 Couleur du pseudo mise à jour : " + colorInput.toLowerCase()), false);
                                        refreshTab(ctx.getSource());
                                        return 1;
                                    } catch (IllegalArgumentException e) {
                                        ctx.getSource().sendFailure(Component.literal(
                                                "Couleur invalide. Essaie /nick color et regarde l’autocomplétion."));
                                        return 0;
                                    }
                                })
                                // /nick color <couleur> <player>  -> MJ only (other)
                                .then(Commands.argument("player", GameProfileArgument.gameProfile())
                                        .requires(Perms.require(Role.MJ))
                                        .executes(ctx -> {
                                            String colorInput = StringArgumentType.getString(ctx, "color").toUpperCase();
                                            try {
                                                ChatFormatting.valueOf(colorInput);
                                            } catch (IllegalArgumentException e) {
                                                ctx.getSource().sendFailure(Component.literal(
                                                        "Couleur invalide. Essaie /nick color et regarde l’autocomplétion."));
                                                return 0;
                                            }

                                            var profiles = GameProfileArgument.getGameProfiles(ctx, "player");
                                            var gp = profiles.iterator().next();

                                            var srv = ctx.getSource().getServer();
                                            ServerPlayer target = srv.getPlayerList().getPlayer(gp.getId());
                                            if (target == null) {
                                                ctx.getSource().sendFailure(Component.literal("Joueur hors-ligne ou introuvable."));
                                                return 0;
                                            }

                                            ChatManager.setColor(target.getUUID(), colorInput);
                                            ctx.getSource().sendSuccess(() -> Component.literal(
                                                    "Couleur du pseudo de " + gp.getName() + " mise à jour : " + colorInput.toLowerCase()), false);
                                            refreshTab(ctx.getSource());
                                            return 1;
                                        })
                                )
                        )
                )
        );
    }

    private static void refreshTab(CommandSourceStack src) {
        var srv = src.getServer();
        TabDisplay.updateAll(srv.getPlayerList().getPlayers());
    }
}
