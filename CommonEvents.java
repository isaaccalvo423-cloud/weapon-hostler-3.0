package com.gunholsters;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

public class CommonEvents {

    /** Convierte cada GunItem (Point Blank / Civilian Gun Pack) en un "curio" que solo entra en su funda correcta. */
    @SubscribeEvent
    public void attach(AttachCapabilitiesEvent<ItemStack> e) {
        ItemStack stack = e.getObject();
        if (stack.isEmpty() || !GunHelper.isGun(stack.getItem())) return;
        ICurio curio = new ICurio() {
            @Override public ItemStack getStack() { return stack; }
            @Override public boolean canEquip(SlotContext ctx) {
                return GunHelper.slotFor(GunHelper.kind(stack)).equals(ctx.identifier());
            }
            @Override public boolean canEquipFromUse(SlotContext ctx) { return true; }
        };
        e.addCapability(new ResourceLocation(GunHolsters.MODID, "holster"), CuriosApi.createCurioProvider(curio));
    }
}
