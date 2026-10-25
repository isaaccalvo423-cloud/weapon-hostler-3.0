package com.gunholsters;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

public final class GunHelper {
    public static final String SLOT_WAIST = "holster_waist";
    public static final String SLOT_BACK = "holster_back";
    public enum Kind { SIDEARM, LONG }

    private static final Map<Class<?>, Boolean> CACHE = new ConcurrentHashMap<>();

    /** Palabras largas: basta con que el id las contenga. */
    private static final String[] SIDE_CONTAINS = {
            "pistol", "revolver", "glock", "beretta", "python", "deagle", "desert_eagle", "magnum", "handgun",
            "makarov", "walther", "luger", "taurus", "fiveseven", "five_seven", "anaconda", "peacemaker", "mateba", "p2000"};
    /** Palabras cortas: deben coincidir con una "palabra" completa del id (separada por _ - /), p.ej. m9, m9a1 pero NO m95. */
    private static final String[] SIDE_TOKENS = {
            "m9", "m92", "m1911", "1911", "usp", "p226", "p229", "p320", "p250", "p99", "p22", "hk45", "mk23", "cz75", "tt33",
            "g17", "g18", "g19", "g22", "g23", "g26", "g43", "m17", "m18", "vp9", "fnx", "ppk", "saa", "m29", "pm", "r8"};

    /** Cualquier clase que herede de Point Blank "GunItem" (incluye armas del Civilian Gun Pack). */
    public static boolean isGun(Item item) {
        return CACHE.computeIfAbsent(item.getClass(), c -> {
            for (Class<?> k = c; k != null; k = k.getSuperclass())
                if (k.getSimpleName().equals("GunItem")) return true;
            return false;
        });
    }

    private static boolean matches(String path, String kw) {
        kw = kw.toLowerCase();
        if (kw.length() >= 5) return path.contains(kw);
        for (String tok : path.split("[^a-z0-9]+")) {
            if (tok.equals(kw)) return true;
            if (tok.startsWith(kw) && tok.length() > kw.length() && !Character.isDigit(tok.charAt(kw.length()))) return true;
        }
        return false;
    }

    public static Kind kind(Item item) {
        ResourceLocation id = ForgeRegistries.ITEMS.getKey(item);
        if (id == null) return Kind.LONG;
        String p = id.getPath().toLowerCase();
        try {
            for (String s : Config.EXTRA_LONG.get()) if (!s.equals("placeholder_none") && p.contains(s.toLowerCase())) return Kind.LONG;
            for (String s : Config.EXTRA_SIDEARM.get()) if (!s.equals("placeholder_none") && p.contains(s.toLowerCase())) return Kind.SIDEARM;
        } catch (Exception ignored) { /* config aun no cargada: se usan solo las reglas internas */ }
        for (String s : SIDE_CONTAINS) if (p.contains(s)) return Kind.SIDEARM;
        for (String s : SIDE_TOKENS) if (matches(p, s)) return Kind.SIDEARM;
        return Kind.LONG;
    }

    public static Kind kind(ItemStack s) { return kind(s.getItem()); }
    public static String slotFor(Kind k) { return k == Kind.SIDEARM ? SLOT_WAIST : SLOT_BACK; }
    public static int slotType(String slotId) { return SLOT_WAIST.equals(slotId) ? 0 : 1; }
}
