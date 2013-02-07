package net.minecraft.src;

public interface IEntitySelector
{
    /**
     * Return whether the specified entity is applicable to this filter.
     */
    boolean isEntityApplicable(Entity var1);
}
