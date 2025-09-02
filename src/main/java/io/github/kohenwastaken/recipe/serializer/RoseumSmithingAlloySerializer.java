package io.github.kohenwastaken.recipe.serializer;

import com.google.gson.JsonObject;
import io.github.kohenwastaken.recipe.RoseumSmithingAlloyRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class RoseumSmithingAlloySerializer implements RecipeSerializer<RoseumSmithingAlloyRecipe> {

    @Override
    public RoseumSmithingAlloyRecipe read(Identifier id, JsonObject json) {
        boolean requireTemplate = JsonHelper.getBoolean(json, "require_template", false);
        Ingredient template = json.has("template") ? Ingredient.fromJson(json.get("template")) : Ingredient.EMPTY;
        Ingredient base = Ingredient.fromJson(JsonHelper.getObject(json, "base"));
        Ingredient addition = Ingredient.fromJson(JsonHelper.getObject(json, "addition"));

        JsonObject res = JsonHelper.getObject(json, "result");
        Item item = Registries.ITEM.get(new Identifier(JsonHelper.getString(res, "item")));
        ItemStack result = new ItemStack(item, 1);

        return new RoseumSmithingAlloyRecipe(id, requireTemplate, template, base, addition, result);
    }

    @Override
    public RoseumSmithingAlloyRecipe read(Identifier id, PacketByteBuf buf) {
        boolean requireTemplate = buf.readBoolean();
        Ingredient template = Ingredient.fromPacket(buf);
        Ingredient base = Ingredient.fromPacket(buf);
        Ingredient addition = Ingredient.fromPacket(buf);
        ItemStack result = buf.readItemStack();
        return new RoseumSmithingAlloyRecipe(id, requireTemplate, template, base, addition, result);
    }

    @Override
    public void write(PacketByteBuf buf, RoseumSmithingAlloyRecipe r) {
        buf.writeBoolean(r.requireTemplate);
        r.template.write(buf);
        r.base.write(buf);
        r.addition.write(buf);
        buf.writeItemStack(r.result);
    }
}
