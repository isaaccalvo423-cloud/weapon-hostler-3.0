package com.gunholsters;

import java.util.List;
import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    public static final ForgeConfigSpec COMMON;
    public static final ForgeConfigSpec CLIENT;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_SIDEARM;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> EXTRA_LONG;
    /** x, y, z, pitch, roll, tilt, scale */
    public static final ForgeConfigSpec.DoubleValue[] WAIST;
    public static final ForgeConfigSpec.DoubleValue[] BACK;

    static {
        ForgeConfigSpec.Builder c = new ForgeConfigSpec.Builder();
        EXTRA_SIDEARM = c.comment("Palabras (parte del id del item) que fuerzan un arma a ser ARMA CORTA (cintura). Ej: [\"m9\"]")
                .defineList("extraSidearmKeywords", List.of("placeholder_none"), o -> o instanceof String);
        EXTRA_LONG = c.comment("Palabras (parte del id del item) que fuerzan un arma a ser ARMA LARGA (espalda). Tiene prioridad.")
                .defineList("extraLongKeywords", List.of("placeholder_none"), o -> o instanceof String);
        COMMON = c.build();

        ForgeConfigSpec.Builder k = new ForgeConfigSpec.Builder();
        k.comment("hip_holster y back_holster: posicion relativa al torso (bloques). pitch/roll/tilt en grados.");
        WAIST = pose(k, "hip_holster", -0.20, 0.60, -0.19, 90, 0, 0, 0.85);
        BACK = pose(k, "back_holster", 0.0, 0.25, 0.21, 90, 90, 18, 0.8);
        CLIENT = k.build();
    }

    private static ForgeConfigSpec.DoubleValue[] pose(ForgeConfigSpec.Builder b, String name, double... d) {
        String[] n = {"x", "y", "z", "pitch", "roll", "tilt", "scale"};
        ForgeConfigSpec.DoubleValue[] out = new ForgeConfigSpec.DoubleValue[7];
        b.push(name);
        for (int i = 0; i < 7; i++) out[i] = b.defineInRange(n[i], d[i], -360.0, 360.0);
        b.pop();
        return out;
    }
}
