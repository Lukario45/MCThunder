package net.mcthunder.world.generator;

/**
 * Created by conno_000 on 10/5/2015.
 */
public class PerlinTest {
    double lerp(float a0, float a1, float w){
        return (1.0 - w)*a0+w*a1;
    }

    int dotGridGradient(int ix, int iy, float x, float y){
       // float Gradient[y][x][2];

        float dx = (float) (x - (double)ix);
        float dy = (float) (y - (double)ix);
        return 0;
    }
}
