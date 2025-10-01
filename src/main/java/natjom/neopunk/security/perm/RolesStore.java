package natjom.neopunk.security.perm;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.*;

public final class RolesStore {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type TYPE = new TypeToken<Map<String, List<String>>>(){}.getType();
    private static Map<UUID, EnumSet<Role>> CACHE = new HashMap<>();
    private static File file() {
        var mcDir = ServerLifecycleHooks.getCurrentServer().getServerDirectory();
        var cfgDir = new File(mcDir, "config/neopunk");
        if (!cfgDir.exists()) cfgDir.mkdirs();
        return new File(cfgDir, "neopunk-perms.json");
    }

    public static synchronized void load() {
        CACHE.clear();
        var f = file();
        if (!f.exists()) { save(); return; }
        try (var r = new FileReader(f)) {
            Map<String, List<String>> raw = GSON.fromJson(r, TYPE);
            if (raw != null) {
                for (var e : raw.entrySet()) {
                    var uuid = UUID.fromString(e.getKey());
                    var set = EnumSet.noneOf(Role.class);
                    for (var s : e.getValue()) {
                        var role = Role.fromString(s);
                        if (role != null) set.add(role);
                    }
                    CACHE.put(uuid, set);
                }
            }
        } catch (Exception ignored) {}
    }

    public static synchronized void save() {
        Map<String, List<String>> out = new LinkedHashMap<>();
        for (var e : CACHE.entrySet()) {
            var list = new ArrayList<String>();
            for (var r : e.getValue()) list.add(r.name().toLowerCase());
            out.put(e.getKey().toString(), list);
        }
        try (var w = new FileWriter(file())) {
            GSON.toJson(out, TYPE, w);
        } catch (Exception ignored) {}
    }

    public static synchronized EnumSet<Role> get(UUID uuid) {
        return CACHE.computeIfAbsent(uuid, k -> EnumSet.noneOf(Role.class));
    }

    public static synchronized boolean add(UUID uuid, Role role) {
        var set = get(uuid);
        boolean added = set.add(role);
        if (added) save();
        return added;
    }

    public static synchronized boolean remove(UUID uuid, Role role) {
        var set = get(uuid);
        boolean removed = set.remove(role);
        if (removed) save();
        return removed;
    }

    public static synchronized void wipeAndSet(UUID uuid, EnumSet<Role> roles) {
        CACHE.put(uuid, EnumSet.copyOf(roles));
        save();
    }

    public static synchronized Map<UUID, EnumSet<Role>> snapshot() {
        Map<UUID, EnumSet<Role>> copy = new LinkedHashMap<>();
        for (var e : CACHE.entrySet()) copy.put(e.getKey(), EnumSet.copyOf(e.getValue()));
        return copy;
    }
}
