////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: SimplexNoise
////////////////////////////////////////////////

package Engine.Math.Noise;

public class SimplexNoise {
    public static float[][] generateOctavedSimplexNoise(final int width,
                                                        final int height,
                                                        final int octaves,
                                                        final float roughness,
                                                        final float scale)
    {
        final float[][] totalNoise = new float[width][height];
        float layerFrequency = scale;
        float layerWeight = 1f;
        float weightSum = 0f;

        for (int octave = 0; octave < octaves; octave++)
        {
            // Calculate single layer/octave of simplex noise, then add it to total noise
            for (int x = 0; x < width; x++)
            {
                for (int y = 0; y < height; y++)
                {
                    totalNoise[x][y] += (float) (new SimplexNoise_octave(octave)).noise(x * layerFrequency, y * layerFrequency) * layerWeight;
                }
            }

            // Increase variables with each incrementing octave
            layerFrequency *= 2f;
            weightSum += layerWeight;
            layerWeight *= roughness;
        }

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                totalNoise[x][y] /= weightSum;
            }
        }

        return totalNoise;
    }

}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////