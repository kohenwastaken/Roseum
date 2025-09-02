package io.github.kohenwastaken.mixin;

import io.github.kohenwastaken.RoseumConfig;
import io.github.kohenwastaken.RoseumConfig.TemplatePolicy;
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

    // catching template before output
    @Inject(method = "onTakeOutput(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V",
            at = @At("HEAD"))
    private void roseum$captureBefore(PlayerEntity player, ItemStack result, CallbackInfo ci) {
        ScreenHandler self = (ScreenHandler) (Object) this;
        this.roseum$templateBefore = self.getSlot(0).getStack().copy();
    }

    // apply template policy after output (after vanilla consume)
    @Inject(method = "onTakeOutput(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;)V",
            at = @At("TAIL"))
    private void roseum$applyTemplateBehavior(PlayerEntity player, ItemStack result, CallbackInfo ci) {
        if (roseum$templateBefore.isEmpty()) return;

        // work with our results
        var id = Registries.ITEM.getId(result.getItem());
        if (!"roseum".equals(id.getNamespace())) return;

        final boolean isAlloy = "rosegold_ingot".equals(id.getPath());
        TemplatePolicy policy = isAlloy
                ? RoseumConfig.INSTANCE.smithingAlloy_templatePolicy
                : RoseumConfig.INSTANCE.smithingTransform_templatePolicy;

        switch (policy) {
            case CONSUME -> { return; } // vanilla behavior
            case OFF, DO_NOT_CONSUME -> {
                ItemStack give = roseum$templateBefore.copy();
                give.setCount(1); // give back only one
                returnStack(player, ((ScreenHandler)(Object)this).getSlot(0), give);
            }
            case DAMAGE -> {
                ItemStack dmg = roseum$templateBefore.copy();
                dmg.setCount(1);
                if (dmg.isDamageable()) {
                    dmg.setDamage(dmg.getDamage() + 1);
                    if (dmg.getDamage() >= dmg.getMaxDamage()) {
                        // broken → consumed
                        return;
                    }
                }
                returnStack(player, ((ScreenHandler)(Object)this).getSlot(0), dmg);
            }
        }
    }

    @Unique
    private static void returnStack(PlayerEntity player, Slot slot, ItemStack give) {
        ItemStack in = slot.getStack();
        if (in.isEmpty()) {
            slot.setStack(give);
            slot.markDirty();
        } else if (ItemStack.canCombine(in, give) && in.getCount() < in.getMaxCount()) {
            in.increment(give.getCount()); // 1
            slot.setStack(in);
            slot.markDirty();
        } else {
            player.getInventory().insertStack(give);
        }
    }
}
