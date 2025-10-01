package natjom.neopunk.commands.admin;

import com.mojang.brigadier.CommandDispatcher;
import natjom.neopunk.security.perm.Perms;
import natjom.neopunk.security.perm.Role;
import natjom.neopunk.ui.tab.TabDisplay;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public final class TabAdminCommand {
    private TabAdminCommand() {}

    public static void register(CommandDispatcher<CommandSourceStack> d) {
        d.register(Commands.literal("tabrefresh")
                .requires(Perms.require(Role.MJ, Role.SUPPORT))
                .executes(ctx -> {
                    var srv = ctx.getSource().getServer();
                    TabDisplay.updateAll(srv.getPlayerList().getPlayers());
                    ctx.getSource().sendSuccess(
                            () -> Component.literal("TAB rafraîchi.").withStyle(ChatFormatting.GRAY),
                            false
                    );
                    return 1;
                })
        );
    }
}
