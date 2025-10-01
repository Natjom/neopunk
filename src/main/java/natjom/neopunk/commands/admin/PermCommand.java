package natjom.neopunk.commands.admin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import natjom.neopunk.security.perm.Role;
import natjom.neopunk.security.perm.RolesStore;
import natjom.neopunk.ui.tab.TabDisplay;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.network.chat.Component;

import java.util.List;

public final class PermCommand {
    private PermCommand(){}

    private static final SuggestionProvider<CommandSourceStack> ROLE_SUGGEST =
            (ctx, b) -> net.minecraft.commands.SharedSuggestionProvider.suggest(List.of("mj","support","builder"), b);

    public static void register(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("perm")
                .requires(src -> src.hasPermission(2))
                .then(Commands.literal("reload")
                        .executes(ctx -> {
                            RolesStore.load();
                            ctx.getSource().sendSuccess(() -> Component.literal("§aPermissions rechargées."), true);
                            refreshTab(ctx.getSource());
                            return 1;
                        }))
                .then(Commands.literal("list")
                        .then(Commands.argument("player", GameProfileArgument.gameProfile())
                                .executes(ctx -> {
                                    var profiles = GameProfileArgument.getGameProfiles(ctx, "player");
                                    var src = ctx.getSource();
                                    profiles.forEach(gp -> {
                                        var uuid = gp.getId();
                                        var roles = RolesStore.get(uuid);
                                        src.sendSuccess(() -> Component.literal("§7" + gp.getName() + " §f→ §b" + roles), false);
                                    });
                                    refreshTab(ctx.getSource());
                                    return 1;
                                })))
                .then(Commands.literal("role")
                        .then(Commands.argument("player", GameProfileArgument.gameProfile())
                                .then(Commands.literal("add")
                                        .then(Commands.argument("role", StringArgumentType.string()).suggests(ROLE_SUGGEST)
                                                .executes(ctx -> {
                                                    var gp = GameProfileArgument.getGameProfiles(ctx, "player").iterator().next();
                                                    var roleStr = StringArgumentType.getString(ctx, "role");
                                                    var role = Role.fromString(roleStr);
                                                    if (role == null) {
                                                        ctx.getSource().sendFailure(Component.literal("§cRôle inconnu."));
                                                        return 0;
                                                    }
                                                    boolean ok = RolesStore.add(gp.getId(), role);
                                                    ctx.getSource().sendSuccess(() -> Component.literal(
                                                            (ok?"§aAjouté":"§eDéjà présent") + " §7" + role + " §fà §7" + gp.getName()), true);
                                                    refreshTab(ctx.getSource());
                                                    return 1;
                                                })))
                                .then(Commands.literal("remove")
                                        .then(Commands.argument("role", StringArgumentType.string()).suggests(ROLE_SUGGEST)
                                                .executes(ctx -> {
                                                    var gp = GameProfileArgument.getGameProfiles(ctx, "player").iterator().next();
                                                    var roleStr = StringArgumentType.getString(ctx, "role");
                                                    var role = Role.fromString(roleStr);
                                                    if (role == null) {
                                                        ctx.getSource().sendFailure(Component.literal("§cRôle inconnu."));
                                                        return 0;
                                                    }
                                                    boolean ok = RolesStore.remove(gp.getId(), role);
                                                    ctx.getSource().sendSuccess(() -> Component.literal(
                                                            (ok?"§aRetiré":"§eN’avait pas") + " §7" + role + " §fsur §7" + gp.getName()), true);
                                                    refreshTab(ctx.getSource());
                                                    return 1;
                                                })))
                        ))
        );
    }

    private static void refreshTab(CommandSourceStack src) {
        var srv = src.getServer();
        TabDisplay.updateAll(srv.getPlayerList().getPlayers());
    }
}
