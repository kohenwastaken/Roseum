package io.github.kohenwastaken;

import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

public enum RosegoldArmorMaterial implements ArmorMaterial {
    // durability multiplier, armor values, enchant, equip sound, toughness, kb-resistance, repair item
    ROSEGOLD("rosegold", 25,
            makeProtection(3, 7, 5, 3),
            30,
            SoundEvents.ITEM_ARMOR_EQUIP_GOLD,
            1.0F, 0.0F,
            () -> Ingredient.ofItems(RoseumItems.ROSEGOLD_INGOT));

    // vanilla base durabilities
    private static final Map<ArmorItem.Type, Integer> BASE_DURABILITY = new EnumMap<>(ArmorItem.Type.class);
    static {
        BASE_DURABILITY.put(ArmorItem.Type.BOOTS, 13);
        BASE_DURABILITY.put(ArmorItem.Type.LEGGINGS, 15);
        BASE_DURABILITY.put(ArmorItem.Type.CHESTPLATE, 16);
        BASE_DURABILITY.put(ArmorItem.Type.HELMET, 11);
    }

    private static EnumMap<ArmorItem.Type,Integer> makeProtection(int helm, int chest, int legs, int boots){
        EnumMap<ArmorItem.Type,Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.HELMET, helm);
        map.put(ArmorItem.Type.CHESTPLATE, chest);
        map.put(ArmorItem.Type.LEGGINGS, legs);
        map.put(ArmorItem.Type.BOOTS, boots);
        return map;
    }

    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type,Integer> protection;
    private final int enchantability;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repair;

    RosegoldArmorMaterial(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type,Integer> protection,
                          int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance,
                          Supplier<Ingredient> repair) {

        this.durabilityMultiplier = durabilityMultiplier;
        this.protection = protection;
        this.enchantability = enchantability;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repair = repair;
    }

    @Override public int getDurability(ArmorItem.Type type) { return BASE_DURABILITY.get(type) * durabilityMultiplier; }
    @Override public int getProtection(ArmorItem.Type type) { return protection.get(type); }
    @Override public int getEnchantability() { return enchantability; }
    @Override public SoundEvent getEquipSound() { return equipSound; }
    @Override public Ingredient getRepairIngredient() { return repair.get(); }
    @Override public String getName() { return Roseum.MOD_ID + ":rosegold"; }
    @Override public float getToughness() { return toughness; }
    @Override public float getKnockbackResistance() { return knockbackResistance; }
}
