////////////////////////////////////////////////
// Development
// Chris Dalke
////////////////////////////////////////////////
// Module: ToggleableCameraInputController
////////////////////////////////////////////////

/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package Game.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class ToggleableCameraInputController extends GestureDetector {
    /** The button for rotating the camera. */
    public int rotateButton = Input.Buttons.LEFT;
    /** The angle to rotate when moved the full width or height of the screen. */
    public float rotateAngle = 360f;
    /** The button for translating the camera along the up/right plane */
    public int translateButton = Input.Buttons.RIGHT;
    /** The units to translate the camera when moved the full width or height of the screen. */
    public float translateUnits = 10f; // FIXME auto calculate this based on the target
    /** The button for translating the camera along the direction axis */
    public int forwardButton = Input.Buttons.MIDDLE;
    /** The key which must be pressed to activate rotate, translate and forward or 0 to always activate. */
    public int activateKey = 0;
    /** Indicates if the activateKey is currently being pressed. */
    protected boolean activatePressed;
    /** Whether scrolling requires the activeKey to be pressed (false) or always allow scrolling (true). */
    public boolean alwaysScroll = true;
    /** The weight for each scrolled amount. */
    public float scrollFactor = -0.1f;
    /** World units per screen size */
    public float pinchZoomFactor = 10f;
    /** Whether to update the camera after it has been changed. */
    public boolean autoUpdate = true;
    /** The target to rotate around. */
    public Vector3 target = new Vector3();
    /** Whether to update the target on translation */
    public boolean translateTarget = true;
    /** Whether to update the target on forward */
    public boolean forwardTarget = true;
    /** Whether to update the target on scroll */
    public boolean scrollTarget = false;
    public int forwardKey = Input.Keys.W;
    protected boolean forwardPressed;
    public int backwardKey = Input.Keys.S;
    protected boolean backwardPressed;
    public int rotateRightKey = Input.Keys.A;
    protected boolean rotateRightPressed;
    public int rotateLeftKey = Input.Keys.D;
    protected boolean rotateLeftPressed;
    /** The camera. */
    public Camera camera;
    /** The current (first) button being pressed. */
    protected int button = -1;

    private float startX, startY;
    private final Vector3 tmpV1 = new Vector3();
    private final Vector3 tmpV2 = new Vector3();

    public boolean consumingDrag;

    protected static class CameraGestureListener extends GestureAdapter {
        public ToggleableCameraInputController controller;
        private float previousZoom;

        @Override
        public boolean touchDown (float x, float y, int pointer, int button) {
            previousZoom = 0;
            return false;
        }

        @Override
        public boolean tap (float x, float y, int count, int button) {
            return false;
        }

        @Override
        public boolean longPress (float x, float y) {
            return false;
        }

        @Override
        public boolean fling (float velocityX, float velocityY, int button) {
            return false;
        }

        @Override
        public boolean pan (float x, float y, float deltaX, float deltaY) {
            return false;
        }

        @Override
        public boolean zoom (float initialDistance, float distance) {
            float newZoom = distance - initialDistance;
            float amount = newZoom - previousZoom;
            previousZoom = newZoom;
            float w = Gdx.graphics.getWidth(), h = Gdx.graphics.getHeight();
            return controller.pinchZoom(amount / ((w > h) ? h : w));
        }

        @Override
        public boolean pinch (Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
            return false;
        }
    };

    protected final CameraGestureListener gestureListener;

    protected ToggleableCameraInputController (final CameraGestureListener gestureListener, final Camera camera) {
        super(gestureListener);
        this.gestureListener = gestureListener;
        this.gestureListener.controller = this;
        this.camera = camera;
    }

    public ToggleableCameraInputController (final Camera camera) {
        this(new CameraGestureListener(), camera);
    }

    public void update () {
        camera.update();
    }

    private int touched;
    private boolean multiTouch;
    private boolean enabled;

    public void setEnabled(boolean status){
        enabled = status;
    }

    @Override
    public boolean touchDown (int screenX, int screenY, int pointer, int button) {
        if (enabled) {
            consumingDrag = true;
            touched |= (1 << pointer);
            multiTouch = !MathUtils.isPowerOfTwo(touched);
            if (multiTouch)
                this.button = -1;
            else if (this.button < 0 && (activateKey == 0 || activatePressed)) {
                startX = screenX;
                startY = screenY;
                this.button = button;
            }
            return super.touchDown(screenX, screenY, pointer, button) || (activateKey == 0 || activatePressed);
        } else {
            return false;
        }
    }

    @Override
    public boolean touchUp (int screenX, int screenY, int pointer, int button) {
        if (enabled) {
            consumingDrag = false;
            touched &= -1 ^ (1 << pointer);
            multiTouch = !MathUtils.isPowerOfTwo(touched);
            if (button == this.button) this.button = -1;
            return super.touchUp(screenX, screenY, pointer, button) || activatePressed;
        } else {
            return false;
        }
    }

    protected boolean process (float deltaX, float deltaY, int button) {
        if (enabled) {
            if (button == rotateButton) {
                tmpV1.set(camera.direction).crs(camera.up).y = 0f;
                camera.rotateAround(target, tmpV1.nor(), deltaY * rotateAngle);
                camera.rotateAround(target, Vector3.Y, deltaX * -rotateAngle);
            }
            if (autoUpdate) camera.update();
        }
        return false;
    }

    @Override
    public boolean touchDragged (int screenX, int screenY, int pointer) {
        boolean result = super.touchDragged(screenX, screenY, pointer);
        if (result || this.button < 0) return result;
        final float deltaX = (screenX - startX) / Gdx.graphics.getWidth();
        final float deltaY = (startY - screenY) / Gdx.graphics.getHeight();
        startX = screenX;
        startY = screenY;
        return process(deltaX, deltaY, button);
    }

    @Override
    public boolean scrolled (int amount) {
        return zoom(amount * scrollFactor * translateUnits);
    }

    public boolean zoom (float amount) {
        if (enabled) {
            if (!alwaysScroll && activateKey != 0 && !activatePressed) return false;
            camera.translate(tmpV1.set(camera.direction).scl(amount));
            if (scrollTarget) target.add(tmpV1);
            if (autoUpdate) camera.update();
        }
        return true;
    }

    protected boolean pinchZoom (float amount) {
        return zoom(pinchZoomFactor * amount);
    }

    @Override
    public boolean keyDown (int keycode) {
        return false;
    }

    @Override
    public boolean keyUp (int keycode) {
        return false;
    }
}


////////////////////////////////////////////////
// End of code
////////////////////////////////////////////////