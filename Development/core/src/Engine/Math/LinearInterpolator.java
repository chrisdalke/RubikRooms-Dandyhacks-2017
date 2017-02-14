////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: LinearInterpolator
////////////////////////////////////////////////

package Engine.Math;

import com.badlogic.gdx.math.Interpolation;

public class LinearInterpolator implements Interpolator {
    public LinearInterpolator() {
    }

    public float calculate(float start, float end, float alpha) {
        return (new Interpolation.Pow(1)).apply(start,end,alpha);
    }
}

////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////