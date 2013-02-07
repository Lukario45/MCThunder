package net.minecraft.src;

public class BlockSponge extends Block
{
    protected BlockSponge(int par1)
    {
        super(par1, Material.sponge);
        this.blockIndexInTexture = 48;
        this.setCreativeTab(CreativeTabs.tabBlock);
    }
}
