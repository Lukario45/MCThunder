package net.mcthunder.crafting;

import net.mcthunder.material.Material;

/**
 * Created by Kevin on 12/12/2014.
 */
public class CraftingRecipe {
    private RecipeType recipeType;
    private Material output;
    private int quantityOutput;
    private boolean workbenchOnly;

    public CraftingRecipe(RecipeType recipeType, Material output, int quantityOutput, boolean workbenchOnly) {
        this.recipeType = recipeType;
        this.output = output;
        this.quantityOutput = quantityOutput;
        this.workbenchOnly = workbenchOnly;
    }

    public RecipeType getRecipeType() {
        return this.recipeType;
    }

    public void setRecipeType(RecipeType recipeType) {
        this.recipeType = recipeType;
    }

    public Material getOutput() {
        return this.output;
    }

    public void setOutput(Material output) {
        this.output = output;
    }

    public int getQuantityOutput() {
        return this.quantityOutput;
    }

    public void setQuantityOutput(int quantityOutput) {
        this.quantityOutput = quantityOutput;
    }

    public boolean getWorkbenchOnly() {
        return this.workbenchOnly;
    }

    public void setWorkbenchOnly(boolean workbenchOnly) {
        this.workbenchOnly = workbenchOnly;
    }

    public int getOutputID() {
        return getOutput().getID();
    }
}
