package natjom.neopunk.security.perm;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

import java.util.EnumSet;
import java.util.UUID;
import java.util.function.Predicate;

public final class Perms {
    private Perms(){}

    public static boolean has(CommandSourceStack src, Role role) {
        ServerPlayer p = src.getPlayer();
        if (p == null) return false;
        var set = RolesStore.get(p.getUUID());
        return set.contains(role);
    }

    public static boolean any(CommandSourceStack src, Role... roles) {
        ServerPlayer p = src.getPlayer();
        if (p == null) return false;
        var set = RolesStore.get(p.getUUID());
        for (var r : roles) if (set.contains(r)) return true;
        return false;
    }

    public static Predicate<CommandSourceStack> require(Role... roles) {
        return src -> any(src, roles);
    }

    public static EnumSet<Role> rolesOf(UUID uuid) {
        return RolesStore.get(uuid);
    }
}
