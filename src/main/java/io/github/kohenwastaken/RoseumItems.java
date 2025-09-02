package io.github.kohenwastaken;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class RoseumItems {

    // ===== basic items =====
    public static final Item ROSEGOLD_INGOT  = register("rosegold_ingot",  new Item(new Item.Settings()));
    public static final Item ROSEGOLD_NUGGET = register("rosegold_nugget", new Item(new Item.Settings()));
    public static final Item RAW_ROSEGOLD    = register("raw_rosegold",    new Item(new Item.Settings()));

    // ===== tools =====
    public static final Item ROSEGOLD_SWORD   = register("rosegold_sword",
            new SwordItem(RoseToolMaterial.ROSEGOLD, 3, -2.4F, new Item.Settings()));

    public static final Item ROSEGOLD_PICKAXE = register("rosegold_pickaxe",
            new PickaxeItem(RoseToolMaterial.ROSEGOLD, 1, -2.8F, new Item.Settings()));

    public static final Item ROSEGOLD_AXE     = register("rosegold_axe",
            new AxeItem(RoseToolMaterial.ROSEGOLD, 6.0F, -3.1F, new Item.Settings()));

    public static final Item ROSEGOLD_SHOVEL  = register("rosegold_shovel",
            new ShovelItem(RoseToolMaterial.ROSEGOLD, 1.5F, -3.0F, new Item.Settings()));

    public static final Item ROSEGOLD_HOE     = register("rosegold_hoe",
            new HoeItem(RoseToolMaterial.ROSEGOLD, -2, 0.0F, new Item.Settings()));
    
    // ===== armors =====
    public static final Item ROSEGOLD_HELMET = register("rosegold_helmet",
            new ArmorItem(RosegoldArmorMaterial.ROSEGOLD, ArmorItem.Type.HELMET, new Item.Settings()));
    public static final Item ROSEGOLD_CHESTPLATE = register("rosegold_chestplate",
            new ArmorItem(RosegoldArmorMaterial.ROSEGOLD, ArmorItem.Type.CHESTPLATE, new Item.Settings()));
    public static final Item ROSEGOLD_LEGGINGS = register("rosegold_leggings",
            new ArmorItem(RosegoldArmorMaterial.ROSEGOLD, ArmorItem.Type.LEGGINGS, new Item.Settings()));
    public static final Item ROSEGOLD_BOOTS = register("rosegold_boots",
            new ArmorItem(RosegoldArmorMaterial.ROSEGOLD, ArmorItem.Type.BOOTS, new Item.Settings()));
    
    // ===== template =====
    public static final Item ALLOY_TEMPLATE = register("alloy_template",
            new Item(new FabricItemSettings()
                    .maxCount(16)                // stacksize
                    .rarity(Rarity.UNCOMMON)	// maybe RARE
                    .maxDamage(128))); 
    
    
    // ===== registration helpers =====
    private static Item register(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(Roseum.MOD_ID, name), item);
    }

    public static void registerModItems() {
        Roseum.LOGGER.info("Registering items for {}", Roseum.MOD_ID);
    }
}
