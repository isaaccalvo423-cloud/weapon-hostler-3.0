package com.gunholsters.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * Pose = {x, y, z, pitch, roll, tilt, scale}. (x,y,z) = donde queda el AGARRE del arma (espacio del torso).
 * Se reutiliza la transformacion con la que el juego pone un item en la mano derecha (canon hacia delante, mira arriba)
 * y despues se gira:  pitch 90 = canon hacia abajo | roll = giro sobre el eje del arma | tilt = diagonal.
 */
public class HolsterPose {
    private static final float HAND_LEN = 0.625f;

    public static double[] values(ForgeConfigSpec.DoubleValue[] c) {
        double[] d = new double[7];
        for (int i = 0; i < 7; i++) d[i] = c[i].get();
        return d;
    }

    public static void apply(PoseStack ps, double[] c) {
        ps.translate(c[0], c[1], c[2]);
        float s = (float) c[6];
        ps.scale(s, s, s);
        ps.mulPose(Axis.ZP.rotationDegrees((float) c[5]));
        ps.mulPose(Axis.YP.rotationDegrees((float) c[4]));
        ps.mulPose(Axis.XP.rotationDegrees((float) c[3]));
        ps.translate(0, -HAND_LEN, 0);
        ps.mulPose(Axis.XP.rotationDegrees(-90));
        ps.mulPose(Axis.YP.rotationDegrees(180));
        ps.translate(1.0f / 16.0f, 0.125f, -0.625f);
    }
}
