package net.mcthunder.world.generator;

import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.block.Material;
import net.mcthunder.world.Biome;
import net.mcthunder.world.Column;
import net.mcthunder.world.Region;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.ShortArray3d;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import static net.mcthunder.api.Utils.getLong;
import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by conno_000 on 5/27/2015.
 */
public class Generation {
    private static Column flatColumn;
    private static File regionTemplate = new File("rgn.mca");
    private static HashMap<Long, Column> generationHashMap = new HashMap<>();

    public static HashMap<Long, Column>  getGenerationHashMap(){
        return generationHashMap;
    }

    private static void generateSuperFlat() {
        Chunk[] chunks = new Chunk[1];
        NibbleArray3d blocklight = new NibbleArray3d(4096); //Create our blocklight array
        NibbleArray3d skylight = new NibbleArray3d(4096); //Create our skylight array
        //THIS IS ALL FOR ONE COLUMN
        ShortArray3d blocks = new ShortArray3d(4096); //Array containing the blocks of THIS sub-chunk only!
        for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
            for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                for (int cX = 0; cX < 16; cX++) { //Loop through x
                    if (cY == 0) //lowest point
                        blocks.setBlock(cX, cY, cZ, Material.BEDROCK.getID()); //Adminium
                    else if (cY <= 3)
                        blocks.setBlock(cX, cY, cZ, Material.DIRT.getID());//Dirt
                    else if (cY == 4)
                        blocks.setBlock(cX, cY, cZ, Material.GRASS.getID()); //Grass
                    if (cY >= 4)
                        skylight.set(cX, cY, cZ, 15);
                }
        chunks[0] = new Chunk(blocks, blocklight, skylight);
        byte[] biomeData = new byte[256];
        byte plains = (byte) Biome.PLAINS.getID();
        for (int j = 0; j < 256; j++)
            biomeData[j] = plains;
        flatColumn = new Column(123, chunks, biomeData);
    }

    private static void generateGrid() {//For testing purposes to allow seeing chunks at a glance
        Chunk[] chunks = new Chunk[1];
        NibbleArray3d blocklight = new NibbleArray3d(4096); //Create our blocklight array
        NibbleArray3d skylight = new NibbleArray3d(4096); //Create our skylight array
        //THIS IS ALL FOR ONE COLUMN
        ShortArray3d blocks = new ShortArray3d(4096); //Array containing the blocks of THIS sub-chunk only!
        for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
            for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                for (int cX = 0; cX < 16; cX++) { //Loop through x
                    if (cY == 0) //lowest point
                        blocks.setBlock(cX, cY, cZ, Material.BEDROCK.getID()); //Adminium
                    else if (cY <= 3)
                        blocks.setBlock(cX, cY, cZ, Material.DIRT.getID());//Dirt
                    else if (cY == 4)
                        blocks.setBlock(cX, cY, cZ, (cX == 0 || cX == 15 || cZ == 0 || cZ == 15) ? Material.LAPIS_BLOCK.getID() : Material.GRASS.getID()); //Grass
                    if (cY >= 4)
                        skylight.set(cX, cY, cZ, 15);
                }
        chunks[0] = new Chunk(blocks, blocklight, skylight);
        byte[] biomeData = new byte[256];
        byte plains = (byte) Biome.PLAINS.getID();
        for (int j = 0; j < 256; j++)
            biomeData[j] = plains;
        flatColumn = new Column(123, chunks, biomeData);
    }

    public static void createPerlinNoise(){

    }

    public static void saveFlatRegion(String worldName, int regionX, int regionZ){
        if(flatColumn == null)
            generateSuperFlat();
        if (!regionTemplate.exists())
            tellConsole(LoggingLevel.SEVERE,"MCThunder is missing Region File Template! Download from website");
        else {
            try {
                Files.copy(regionTemplate.toPath(), new File("worlds/" + worldName + "/region/r." + regionX + "." + regionZ + ".mca").toPath());
                Region rg = new Region(MCThunder.getWorld(worldName), getLong(regionX,regionZ));

                for (int x = 0; x < 32; x++)
                    for (int z = 0; z < 32; z++) {
                        getGenerationHashMap().put(getLong(x,z),flatColumn);
                        rg.saveNewChunk(getLong(x, z));
                        getGenerationHashMap().remove(getLong(x,z));
                    }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}


/**
 * THESE ARE NOTES ON 3D PERLIN NOISE FOR WORLD GENERATION
 * I UNDERSTAND HOW IT WORKS SORT OF BUT I DO NOT KNOW THE LOGIC BEHIND PERLIN NOISE 3D
 * http://devmag.org.za/2009/04/25/perlin-noise/
 * Must create number of smooth noise arrays
 * Each array is called an octave
 * Blend all together 
 * Create "Smooth Noise" float between 0 and One
 * This is Direct Code from the website 
 * float[][] GenerateWhiteNoise(int width, int height)
{
    Random random = new Random(0); //Seed to 0 for testing
    float[][] noise = GetEmptyArray(width, height);
 
    for (int i = 0; i &lt; width; i++)
    {
        for (int j = 0; j &lt; height; j++)
        {
            noise[i][j] = (float)random.NextDouble() % 1;
        }
    }
 
    return noise;
}
* Direct quote
* "For creating the kth octave, sample the noise array at every point (i*2k, j*2k) , for all i, j, and interpolate 
* the other points linearly. The value 2k is called the wave length of that octave, and the value 1/2k is called the frequency.

* The following pseudo C snippet shows how the kth octave is generated. Index variables are integers,
* and remember that integer division gives a floored integer result (for example, 5 / 3 gives 1)."
* Some more code
float[][] GenerateSmoothNoise(float[][] baseNoise, int octave)
{
   int width = baseNoise.Length;
   int height = baseNoise[0].Length;
 
   float[][] smoothNoise = GetEmptyArray(width, height);
 
   int samplePeriod = 1 &lt;&lt; octave; // calculates 2 ^ k
   float sampleFrequency = 1.0f / samplePeriod;
 
   for (int i = 0; i &lt; width; i++)
   {
      //calculate the horizontal sampling indices
      int sample_i0 = (i / samplePeriod) * samplePeriod;
      int sample_i1 = (sample_i0 + samplePeriod) % width; //wrap around
      float horizontal_blend = (i - sample_i0) * sampleFrequency;
 
      for (int j = 0; j &lt; height; j++)
      {
         //calculate the vertical sampling indices
         int sample_j0 = (j / samplePeriod) * samplePeriod;
         int sample_j1 = (sample_j0 + samplePeriod) % height; //wrap around
         float vertical_blend = (j - sample_j0) * sampleFrequency;
 
         //blend the top two corners
         float top = Interpolate(baseNoise[sample_i0][sample_j0],
            baseNoise[sample_i1][sample_j0], horizontal_blend);
 
         //blend the bottom two corners
         float bottom = Interpolate(baseNoise[sample_i0][sample_j1],
            baseNoise[sample_i1][sample_j1], horizontal_blend);
 
         //final blend
         smoothNoise[i][j] = Interpolate(top, bottom, vertical_blend);
      }
   }
 
   return smoothNoise;
}
* Generate Perlin Noise code
float[][] GeneratePerlinNoise(float[][] baseNoise, int octaveCount)
{
   int width = baseNoise.Length;
   int height = baseNoise[0].Length;
 
   float[][][] smoothNoise = new float[octaveCount][][]; //an array of 2D arrays containing
 
   float persistance = 0.5f;
 
   //generate smooth noise
   for (int i = 0; i &lt; octaveCount; i++)
   {
       smoothNoise[i] = GenerateSmoothNoise(baseNoise, i);
   }
 
    float[][] perlinNoise = GetEmptyArray(width, height);
    float amplitude = 1.0f;
    float totalAmplitude = 0.0f;
 
    //blend noise together
    for (int octave = octaveCount - 1; octave &gt;= 0; octave--)
    {
       amplitude *= persistance;
       totalAmplitude += amplitude;
 
       for (int i = 0; i &lt; width; i++)
       {
          for (int j = 0; j &lt; height; j++)
          {
             perlinNoise[i][j] += smoothNoise[octave][i][j] * amplitude;
          }
       }
    }
 
   //normalisation
   for (int i = 0; i &lt; width; i++)
   {
      for (int j = 0; j &lt; height; j++)
      {
         perlinNoise[i][j] /= totalAmplitude;
      }
   }
 
   return perlinNoise;
}
* */
