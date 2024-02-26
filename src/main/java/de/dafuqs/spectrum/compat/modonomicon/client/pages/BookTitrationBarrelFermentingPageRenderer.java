package de.dafuqs.spectrum.compat.modonomicon.client.pages;

import com.klikli_dev.modonomicon.book.BookTextHolder;
import com.klikli_dev.modonomicon.client.gui.book.BookContentScreen;
import com.mojang.blaze3d.systems.RenderSystem;
import de.dafuqs.spectrum.SpectrumCommon;
import de.dafuqs.spectrum.compat.modonomicon.ModonomiconHelper;
import de.dafuqs.spectrum.compat.modonomicon.pages.BookGatedRecipePage;
import de.dafuqs.spectrum.recipe.titration_barrel.TitrationBarrelRecipe;
import net.id.incubus_core.recipe.IngredientStack;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class BookTitrationBarrelFermentingPageRenderer extends BookGatedRecipePageRenderer<TitrationBarrelRecipe, BookGatedRecipePage<TitrationBarrelRecipe>> {

    private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/patchouli/titration_barrel.png");

    private final BookTextHolder durationText1;
    private final BookTextHolder durationText2;

    public BookTitrationBarrelFermentingPageRenderer(BookGatedRecipePage<TitrationBarrelRecipe> page) {
        super(page);

        TitrationBarrelRecipe recipe1 = page.getRecipe1();
        TitrationBarrelRecipe recipe2 = page.getRecipe2();

        durationText1 = recipe1 == null ? null
            : new BookTextHolder(TitrationBarrelRecipe.getDurationText(recipe1.getMinFermentationTimeHours(), recipe1.getFermentationData()));
        durationText2 = recipe2 == null ? null
            : new BookTextHolder(TitrationBarrelRecipe.getDurationText(recipe2.getMinFermentationTimeHours(), recipe2.getFermentationData()));
    }

    @Override
    protected int getRecipeHeight() {
        return 56;
    }

    @Override
    protected void drawRecipe(DrawContext drawContext, TitrationBarrelRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY, boolean second) {
        World world = parentScreen.getMinecraft().world;
        if (world == null) return;

        RenderSystem.enableBlend();
        drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX - 2, recipeY - 2, 0, 0, 100, 32, 128, 256);

        Fluid fluid = recipe.getFluidInput();
        boolean usesFluid = fluid != Fluids.EMPTY;
        IngredientStack bucketStack = IngredientStack.EMPTY;
        if (usesFluid) {
            bucketStack = IngredientStack.of(Ingredient.ofStacks(recipe.getFluidInput().getBucketItem().getDefaultStack()));
        }

        // the ingredients
        List<IngredientStack> ingredients = recipe.getIngredientStacks();
        int ingredientSize = ingredients.size();
        int ingredientSizeWithFluid = usesFluid ? ingredientSize + 1 : ingredientSize;
        int startX = recipeX + Math.max(-5, 15 - ingredientSizeWithFluid * 10);
        int startY = recipeY + (ingredientSizeWithFluid > 3 ? 0 : 10);
        for (int i = 0; i < ingredientSizeWithFluid; i++) {
            IngredientStack currentIngredient = i == ingredientSize ? bucketStack : ingredients.get(i);
            int yOffset;
            int xOffset;
            if (i < 3) {
                xOffset = i * 18;
                yOffset = 0;
            } else {
                xOffset = (i - 3) * 18;
                yOffset = 18;
            }
            ModonomiconHelper.renderIngredientStack(drawContext, parentScreen, startX + xOffset, startY + yOffset, mouseX, mouseY, currentIngredient);
        }

        // the titration barrel / tapping ingredient
        if (recipe.getTappingItem() == Items.AIR) {
            parentScreen.renderItemStack(drawContext, recipeX + 54, recipeY + 20, mouseX, mouseY, recipe.createIcon());
        } else {
            parentScreen.renderItemStack(drawContext, recipeX + 50, recipeY + 20, mouseX, mouseY, recipe.createIcon());
            parentScreen.renderItemStack(drawContext, recipeX + 60, recipeY + 20, mouseX, mouseY, recipe.getTappingItem().getDefaultStack());
        }

        // the output
        parentScreen.renderItemStack(drawContext, recipeX + 78, recipeY + 10, mouseX, mouseY, recipe.getOutput(world.getRegistryManager()));

        // the duration
        if (!second && durationText1 != null) {
            this.renderBookTextHolder(drawContext, durationText1, 0, recipeY + 40, BookContentScreen.PAGE_WIDTH);
        }
        if (second && durationText2 != null) {
            this.renderBookTextHolder(drawContext, durationText2, 0, recipeY + 40, BookContentScreen.PAGE_WIDTH);
        }
    }

}
