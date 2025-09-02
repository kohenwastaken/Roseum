package io.github.kohenwastaken.recipe;

import io.github.kohenwastaken.RoseumSmithing;
import io.github.kohenwastaken.RoseumConfig;
import io.github.kohenwastaken.RoseumConfig.TemplatePolicy;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.Map;

public class RoseumSmithingTransformRecipe implements SmithingRecipe {
    private final Identifier id;
    public final boolean requireTemplate;
    public final Ingredient template;
    public final Ingredient base;
    public final Ingredient addition;
    public final Map<Item, Item> resultMap;
    
    private boolean requireTemplateEffective() {
        return RoseumConfig.INSTANCE.smithingTransform_templatePolicy != TemplatePolicy.OFF;
    }

    public RoseumSmithingTransformRecipe(Identifier id, boolean requireTemplate, Ingredient template,
                                         Ingredient base, Ingredient addition, Map<Item, Item> resultMap) {
        this.id = id;
        this.requireTemplate = requireTemplate;
        this.template = template == null ? Ingredient.EMPTY : template;
        this.base = base;
        this.addition = addition;
        this.resultMap = resultMap;
    }

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
        	//for refusing wrong template enable this
            //if (!t.isEmpty() && !template.isEmpty() && !template.test(t)) return false;
        }

        if (!base.test(b) || !addition.test(a)) return false;
        return resultMap.containsKey(b.getItem());
    }

    @Override public ItemStack craft(Inventory inv, DynamicRegistryManager regs) {
        Item out = resultMap.get(inv.getStack(1).getItem());
        return out == null ? ItemStack.EMPTY : new ItemStack(out, 1);
    }

    @Override public ItemStack getOutput(DynamicRegistryManager regs) {
        return resultMap.values().stream().findFirst().map(ItemStack::new).orElse(ItemStack.EMPTY);
    }

    @Override public Identifier getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer() { return RoseumSmithing.TRANSFORM_SERIALIZER; }
    @Override public RecipeType<?> getType() { return RecipeType.SMITHING; }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        var list = DefaultedList.<Ingredient>of();
        list.add(template);
        list.add(base);
        list.add(addition);
        return list;
    }

    @Override
    public boolean testTemplate(ItemStack stack) {
        boolean need = requireTemplateEffective();
        if (!need) return true;
        if (stack.isEmpty()) return false;
        return template.isEmpty() || template.test(stack);
    }
    @Override public boolean testBase(ItemStack s)     { return base.test(s); }
    @Override public boolean testAddition(ItemStack s) { return addition.test(s); }
}
