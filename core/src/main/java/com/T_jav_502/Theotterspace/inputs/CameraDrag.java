package com.T_jav_502.Theotterspace.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.Input;

/**
 * Handles camera movement by dragging with the middle mouse button.
 * <p>
 * When the middle mouse button is pressed and dragged, this class updates
 * the position of the given {@link OrthographicCamera} based on the mouse movement.
 * The movement is scaled by the camera's current zoom level.
 * </p>
 */
public class CameraDrag {

    private final OrthographicCamera camera;
    private float lastMouseX, lastMouseY;
    private boolean dragging = false;

    /**
     * Creates a new CameraDrag controller for the specified camera.
     *
     * @param camera the {@link OrthographicCamera} to control
     */
    public CameraDrag(OrthographicCamera camera) {
        this.camera = camera;
    }

    /**
     * Updates the camera's position if the middle mouse button is pressed.
     * <p>
     * Should be called once per frame, typically in the render or update loop.
     * Handles starting and stopping the drag automatically.
     * </p>
     */
    public void update() {
        if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();

            if (!dragging) {
                dragging = true;
                lastMouseX = mouseX;
                lastMouseY = mouseY;
            }

            float dx = (lastMouseX - mouseX) * camera.zoom;
            float dy = (mouseY - lastMouseY) * camera.zoom;

            camera.position.add(dx, dy, 0);

            lastMouseX = mouseX;
            lastMouseY = mouseY;
        } else {
            dragging = false;
        }
    }
}
