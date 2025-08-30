package io.github.kohenwastaken;

import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;

import java.util.function.Supplier;

public enum RoseToolMaterial implements ToolMaterial {
    // values: (miningLevel, durability, miningSpeed, attackDamage, enchantability, repair)
    ROSEGOLD(3, 750, 7.0F, 2.0F, 25, () -> Ingredient.ofItems(RoseumItems.ROSEGOLD_INGOT));

    private final int miningLevel;
    private final int durability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Supplier<Ingredient> repairSupplier;

    RoseToolMaterial(int miningLevel, int durability, float miningSpeed, float attackDamage,
                     int enchantability, Supplier<Ingredient> repairSupplier) {
        this.miningLevel = miningLevel;
        this.durability = durability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairSupplier = repairSupplier;
    }

    @Override public int getDurability()              { return durability; }
    @Override public float getMiningSpeedMultiplier()  { return miningSpeed; }
    @Override public float getAttackDamage()           { return attackDamage; }
    @Override public int getMiningLevel()              { return miningLevel; }
    @Override public int getEnchantability()           { return enchantability; }
    @Override public Ingredient getRepairIngredient()  { return repairSupplier.get(); }
}
