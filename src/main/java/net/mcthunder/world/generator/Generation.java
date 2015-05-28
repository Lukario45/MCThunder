package net.mcthunder.world.generator;

import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.ShortArray3d;

/**
 * Created by conno_000 on 5/27/2015.
 */
public class Generation {

    public static void generateSuperFlat(){
        /** for (int i = 0; i < chunks.length; i++) { //Loop through all 16 chunks (verticle fashion)
         NibbleArray3d blocklight = new NibbleArray3d(light); //Create our blocklight array
         NibbleArray3d skylight = new NibbleArray3d(light); //Create our skylight array
         ShortArray3d blocks = new ShortArray3d(4096); //Array containing the blocks of THIS sub-chunk only!

         for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
         for (int cZ = 0; cZ < 16; cZ++) //Loop through z
         for (int cX = 0; cX < 16; cX++) { //Loop through x
         int y = cY + i * 16; //Get our ABSOLUTE y coordinate (used only to check our literal position)

         if (y == 0) //lowest point
         blocks.setBlock(cX, cY, cZ, 7); //Adminium
         else if (y <= 3)
         blocks.setBlock(cX, cY, cZ, 3);//Stone
         else if (y ==4 21 && y < 24) //less than 24 but above 0
         blocks.setBlock(cX, cY, cZ, 3); //Dirt
         //else if (y == 24) //Exactly 24
         //blocks.setBlock(cX, cY, cZ, 2); //Grass
         }
         Chunk chunk = new Chunk(blocks, blocklight, skylight);
         chunks[i] = chunk;
         }*/

        NibbleArray3d blocklight = new NibbleArray3d(4096); //Create our blocklight array
       NibbleArray3d skylight = new NibbleArray3d(4096); //Create our skylight array
        //THIS IS ALL FOR ONE COLUMN
       ShortArray3d blocks = new ShortArray3d(4096); //Array containing the blocks of THIS sub-chunk only!
        int i = 0;
        for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
            for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                for (int cX = 0; cX < 16; cX++) { //Loop through x
                    int y = cY + i * 16; //Get our ABSOLUTE y coordinate (used only to check our literal position)

                    if (y == 0) //lowest point
                        blocks.setBlock(cX, cY, cZ, 7); //Adminium
                    else if (y <= 3)
                        blocks.setBlock(cX, cY, cZ, 3);//Dirt
                    else if (y ==4) //less than 24 but above 0
                    blocks.setBlock(cX, cY, cZ, 2); //Grass
                }
        Chunk chunk = new Chunk(blocks, blocklight, skylight);
        //chunks[i] = chunk;
    }
}
