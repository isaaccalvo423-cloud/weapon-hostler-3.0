package com.gunholsters;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

public class DrawHandler {

    private static IDynamicStackHandler stacks(ServerPlayer p, String id) {
        return CuriosApi.getCuriosInventory(p).resolve()
                .flatMap(h -> h.getStacksHandler(id)).map(ICurioStacksHandler::getStacks).orElse(null);
    }

    private static void sound(ServerPlayer p, float pitch) {
        p.level().playSound(null, p.blockPosition(), SoundEvents.ARMOR_EQUIP_IRON, SoundSource.PLAYERS, 0.8f, pitch);
    }

    /** Instantaneo: guarda el arma de la mano si la funda esta libre; si no, saca el arma de la funda. */
    public static void request(ServerPlayer p, int slotType) {
        GunHelper.Kind kind = slotType == 0 ? GunHelper.Kind.SIDEARM : GunHelper.Kind.LONG;
        IDynamicStackHandler st = stacks(p, GunHelper.slotFor(kind));
        if (st == null) return;
        ItemStack held = p.getMainHandItem();
        ItemStack slot = st.getStackInSlot(0);
        boolean heldFits = GunHelper.isGun(held.getItem()) && GunHelper.kind(held) == kind;

        if (heldFits && slot.isEmpty()) {
            st.setStackInSlot(0, held.copy());
            p.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
            sound(p, 1.0f);
            return;
        }
        if (slot.isEmpty()) return;

        ItemStack toSlot = ItemStack.EMPTY;
        if (!held.isEmpty()) {
            if (heldFits) toSlot = held.copy();
            else if (!p.getInventory().add(held.copy())) p.drop(held.copy(), false);
        }
        st.setStackInSlot(0, toSlot);
        p.setItemInHand(InteractionHand.MAIN_HAND, slot.copy());
        sound(p, 1.3f);
    }
}
