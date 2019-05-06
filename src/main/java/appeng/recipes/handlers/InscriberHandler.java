
package appeng.recipes.handlers;

import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.JsonContext;

import appeng.api.AEApi;
import appeng.api.features.IInscriberRecipeBuilder;
import appeng.api.features.IInscriberRegistry;
import appeng.api.features.InscriberProcessType;
import appeng.recipes.IAERecipeFactory;
import appeng.recipes.factories.recipes.PartRecipeFactory;

public class InscriberHandler implements IAERecipeFactory {

    @Override
    public void register(JsonObject json, JsonContext ctx) {
        ItemStack result = PartRecipeFactory.getResult(json, ctx);
        String mode = JsonUtils.getString(json, "mode");

        JsonObject ingredients = JsonUtils.getJsonObject(json, "ingredients");

        List<ItemStack> middle = Arrays
                .asList(CraftingHelper.getIngredient(ingredients.get("middle"), ctx).getMatchingStacks());
        ItemStack[] top = new ItemStack[] { null };
        if (ingredients.has("top")) {
            top = CraftingHelper.getIngredient(JsonUtils.getJsonObject(ingredients, "top"), ctx).getMatchingStacks();
        }

        ItemStack[] bottom = new ItemStack[] { null };
        if (ingredients.has("bottom")) {
            bottom = CraftingHelper.getIngredient(JsonUtils.getJsonObject(ingredients, "bottom"), ctx)
                    .getMatchingStacks();
        }

        final IInscriberRegistry reg = AEApi.instance().registries().inscriber();
        for (ItemStack element : top) {
            for (ItemStack element2 : bottom) {
                final IInscriberRecipeBuilder builder = reg.builder();
                builder.withOutput(result);
                builder.withProcessType(
                        "press".equals(mode) ? InscriberProcessType.PRESS : InscriberProcessType.INSCRIBE);
                builder.withInputs(middle);

                if (element != null) {
                    builder.withTopOptional(element);
                }
                if (element2 != null) {
                    builder.withBottomOptional(element2);
                }

                reg.addRecipe(builder.build());
            }
        }
    }

}
