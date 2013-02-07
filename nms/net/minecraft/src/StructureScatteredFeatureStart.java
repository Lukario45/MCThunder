package net.minecraft.src;

import java.util.Random;

public class StructureScatteredFeatureStart extends StructureStart
{
    public StructureScatteredFeatureStart(World par1World, Random par2Random, int par3, int par4)
    {
        BiomeGenBase var5 = par1World.getBiomeGenForCoords(par3 * 16 + 8, par4 * 16 + 8);

        if (var5 != BiomeGenBase.jungle && var5 != BiomeGenBase.jungleHills)
        {
            if (var5 == BiomeGenBase.swampland)
            {
                ComponentScatteredFeatureSwampHut var8 = new ComponentScatteredFeatureSwampHut(par2Random, par3 * 16, par4 * 16);
                this.components.add(var8);
            }
            else
            {
                ComponentScatteredFeatureDesertPyramid var7 = new ComponentScatteredFeatureDesertPyramid(par2Random, par3 * 16, par4 * 16);
                this.components.add(var7);
            }
        }
        else
        {
            ComponentScatteredFeatureJunglePyramid var6 = new ComponentScatteredFeatureJunglePyramid(par2Random, par3 * 16, par4 * 16);
            this.components.add(var6);
        }

        this.updateBoundingBox();
    }
}
