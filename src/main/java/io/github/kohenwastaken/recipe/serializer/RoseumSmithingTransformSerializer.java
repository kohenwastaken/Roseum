package io.github.kohenwastaken.recipe.serializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.kohenwastaken.recipe.RoseumSmithingTransformRecipe;
import net.minecraft.item.Item;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.HashMap;
import java.util.Map;

public class RoseumSmithingTransformSerializer implements RecipeSerializer<RoseumSmithingTransformRecipe> {

    @Override
    public RoseumSmithingTransformRecipe read(Identifier id, JsonObject json) {
        boolean requireTemplate = JsonHelper.getBoolean(json, "require_template", true);
        Ingredient template = json.has("template") ? Ingredient.fromJson(json.get("template")) : Ingredient.EMPTY;
        Ingredient base = Ingredient.fromJson(JsonHelper.getObject(json, "base"));
        Ingredient addition = Ingredient.fromJson(JsonHelper.getObject(json, "addition"));

        Map<Item, Item> map = new HashMap<>();
        JsonObject rm = JsonHelper.getObject(json, "result_map");
        for (Map.Entry<String, JsonElement> e : rm.entrySet()) {
            Item in = Registries.ITEM.get(new Identifier(e.getKey()));
            Item out = Registries.ITEM.get(new Identifier(JsonHelper.getString(e.getValue().getAsJsonObject(), "item")));
            map.put(in, out);
        }
        return new RoseumSmithingTransformRecipe(id, requireTemplate, template, base, addition, map);
    }

    @Override
    public RoseumSmithingTransformRecipe read(Identifier id, PacketByteBuf buf) {
        boolean requireTemplate = buf.readBoolean();
        Ingredient template = Ingredient.fromPacket(buf);
        Ingredient base = Ingredient.fromPacket(buf);
        Ingredient addition = Ingredient.fromPacket(buf);

        int size = buf.readVarInt();
        Map<Item, Item> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            Item in = Registries.ITEM.get(buf.readIdentifier());
            Item out = Registries.ITEM.get(buf.readIdentifier());
            map.put(in, out);
        }
        return new RoseumSmithingTransformRecipe(id, requireTemplate, template, base, addition, map);
    }

    @Override
    public void write(PacketByteBuf buf, RoseumSmithingTransformRecipe r) {
        buf.writeBoolean(r.requireTemplate);
        r.template.write(buf);
        r.base.write(buf);
        r.addition.write(buf);

        buf.writeVarInt(r.resultMap.size());
        r.resultMap.forEach((in, out) -> {
            buf.writeIdentifier(Registries.ITEM.getId(in));
            buf.writeIdentifier(Registries.ITEM.getId(out));
        });
    }
}
