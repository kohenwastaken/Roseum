package io.github.kohenwastaken.recipe;

import io.github.kohenwastaken.RoseumConfig;
import io.github.kohenwastaken.RoseumSmithing;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class RoseumSmithingAlloyRecipe implements SmithingRecipe {
    private final Identifier id;
    public final boolean requireTemplate;   // JSON’daki varsayılan (bilgi amaçlı)
    public final Ingredient template;       // opsiyonel
    public final Ingredient base;           // #c:copper_ingots
    public final Ingredient addition;       // #c:gold_ingots
    public final ItemStack result;          // rosegold_ingot (1)

    public RoseumSmithingAlloyRecipe(Identifier id, boolean requireTemplate, Ingredient template,
                                     Ingredient base, Ingredient addition, ItemStack result) {
        this.id = id;
        this.requireTemplate = requireTemplate;
        this.template = template == null ? Ingredient.EMPTY : template;
        this.base = base;
        this.addition = addition;
        this.result = result;
    }

    // ---- CONFIG bazlı etkin gereklilik ----
    private boolean requireTemplateEffective() {
        return RoseumConfig.INSTANCE.smithingAlloy_requireTemplate; // <-- DÜZELTİLDİ
    }

    // ---- Recipe<Inventory> ----
    @Override
    public boolean matches(Inventory inv, World world) {
        ItemStack t = inv.getStack(0);
        ItemStack b = inv.getStack(1);
        ItemStack a = inv.getStack(2);

        boolean need = requireTemplateEffective();
        if (need) {
            if (t.isEmpty()) return false;
            if (!template.isEmpty() && !template.test(t)) return false;
        } else {
            // slot boşsa sorun yok; doluysa yanlış template’i reddet
            if (!t.isEmpty() && !template.isEmpty() && !template.test(t)) return false;
        }

        // SIRADAN BAĞIMSIZ: (bakır,altın) VEYA (altın,bakır)
        boolean combo1 = base.test(b) && addition.test(a);
        boolean combo2 = base.test(a) && addition.test(b);
        return combo1 || combo2;
    }

    @Override public ItemStack craft(Inventory inv, DynamicRegistryManager regs) { return result.copy(); }
    @Override public ItemStack getOutput(DynamicRegistryManager regs) { return result.copy(); }
    @Override public Identifier getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer() { return RoseumSmithing.ALLOY_SERIALIZER; }
    @Override public RecipeType<?> getType() { return RecipeType.SMITHING; }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        var list = DefaultedList.<Ingredient>of();
        list.add(template);
        list.add(base);
        list.add(addition);
        return list;
    }

    // ---- SmithingRecipe zorunluları ----
    @Override
    public boolean testTemplate(ItemStack stack) {
        boolean need = requireTemplateEffective();
        if (stack.isEmpty()) return !need; // gerekliyse boş olamaz
        return template.isEmpty() || template.test(stack);
    }

    // QoL: iki input slotu da bakır VEYA altını kabul etsin
    @Override public boolean testBase(ItemStack s)     { return base.test(s) || addition.test(s); }     // <-- DÜZELTİLDİ
    @Override public boolean testAddition(ItemStack s) { return base.test(s) || addition.test(s); }     // <-- DÜZELTİLDİ
}
