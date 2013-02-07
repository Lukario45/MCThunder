package net.minecraft.src;

import net.minecraft.server.MinecraftServer;

public class BehaviorPotionDispense implements IBehaviorDispenseItem
{
    /** Reference to the BehaviorDefaultDispenseItem object. */
    private final BehaviorDefaultDispenseItem defaultItemDispenseBehavior;

    /** Reference to the MinecraftServer object. */
    final MinecraftServer mcServer;

    public BehaviorPotionDispense(MinecraftServer par1)
    {
        this.mcServer = par1;
        this.defaultItemDispenseBehavior = new BehaviorDefaultDispenseItem();
    }

    /**
     * Dispenses the specified ItemStack from a dispenser.
     */
    public ItemStack dispense(IBlockSource par1IBlockSource, ItemStack par2ItemStack)
    {
        return ItemPotion.isSplash(par2ItemStack.getItemDamage()) ? (new BehaviorPotionDispenseLogic(this, par2ItemStack)).dispense(par1IBlockSource, par2ItemStack) : this.defaultItemDispenseBehavior.dispense(par1IBlockSource, par2ItemStack);
    }
}
