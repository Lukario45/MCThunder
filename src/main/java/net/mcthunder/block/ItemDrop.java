package net.mcthunder.block;

/**
 * Created by conno_000 on 12/28/2015.
 */
public enum ItemDrop {
    GRASS("GRASS","DIRT"),
    STONE("STONE", "COBBLESTONE");


    private final String itemThatDrops;
    private final String name;

    private ItemDrop(String name,String itemThatDrops){
        this.itemThatDrops = itemThatDrops;
        this.name = name;
    }
    public String getName(){
        return this.name();
    }
    public String getDropped(){
        return this.itemThatDrops;
    }
}
