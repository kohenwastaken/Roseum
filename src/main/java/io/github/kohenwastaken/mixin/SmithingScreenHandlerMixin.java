package io.github.kohenwastaken.mixin;

import io.github.kohenwastaken.RoseumConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin {

    @Unique private ItemStack roseum$templateBefore = ItemStack.EMPTY;

    // İmza ile hedefle: onTakeOutput(PlayerEntity, ItemStack)
    @Inject(
        method = "onTakeOutput(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V",
        at = @At("HEAD")
    )
    private void roseum$captureBefore(PlayerEntity player, ItemStack result, CallbackInfo ci) {
        ScreenHandler self = (ScreenHandler) (Object) this;
        this.roseum$templateBefore = self.getSlot(0).getStack().copy();
    }

    @Inject(
        method = "onTakeOutput(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V",
        at = @At("TAIL")
    )
    private void roseum$applyTemplateBehavior(PlayerEntity player, ItemStack result, CallbackInfo ci) {
        if (roseum$templateBefore.isEmpty()) return;

        // Sadece bizim tariflerin çıktılarında davran
        var id = Registries.ITEM.getId(result.getItem());
        if (!"roseum".equals(id.getNamespace())) return;

        String behavior;
        String path = id.getPath();
        if ("rosegold_ingot".equals(path)) {
            behavior = RoseumConfig.INSTANCE.smithingAlloy_templateBehavior;
        } else {
            behavior = RoseumConfig.INSTANCE.smithingTransform_templateBehavior;
        }
        if ("consume".equalsIgnoreCase(behavior)) return;

        ScreenHandler self = (ScreenHandler) (Object) this;
        Slot templateSlot = self.getSlot(0);

        if ("return".equalsIgnoreCase(behavior)) {
            returnStack(player, templateSlot, roseum$templateBefore.copy());
            return;
        }

        if ("damage".equalsIgnoreCase(behavior)) {
            ItemStack dmg = roseum$templateBefore.copy();
            dmg.setCount(1);
            if (dmg.isDamageable()) {
                dmg.setDamage(dmg.getDamage() + 1);
                if (dmg.getDamage() >= dmg.getMaxDamage()) return; // kırıldı → tüketildi
            }
            returnStack(player, templateSlot, dmg);
        }
    }

    @Unique
    private static void returnStack(PlayerEntity player, Slot slot, ItemStack give) {
        ItemStack inSlot = slot.getStack();
        if (inSlot.isEmpty()) {
            slot.setStack(give);
            slot.markDirty();
        } else if (ItemStack.canCombine(inSlot, give) && inSlot.getCount() < inSlot.getMaxCount()) {
            inSlot.increment(1);
            slot.setStack(inSlot);
            slot.markDirty();
        } else {
            player.getInventory().insertStack(give);
        }
    }
}
