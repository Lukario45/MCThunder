package net.minecraft.src;

class InventoryRepair extends InventoryBasic
{
    /** Container of this anvil's block. */
    final ContainerRepair theContainer;

    InventoryRepair(ContainerRepair par1ContainerRepair, String par2Str, int par3)
    {
        super(par2Str, par3);
        this.theContainer = par1ContainerRepair;
    }

    /**
     * Called when an the contents of an Inventory change, usually
     */
    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        this.theContainer.onCraftMatrixChanged(this);
    }
}
