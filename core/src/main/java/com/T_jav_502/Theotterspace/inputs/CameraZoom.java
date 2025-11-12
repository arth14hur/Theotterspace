package com.T_jav_502.Theotterspace.inputs;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Handles zooming of an {@link OrthographicCamera} while keeping a specific point under the cursor stationary.
 * <p>
 * This class allows zooming in and out within defined minimum and maximum limits.
 * When zooming, the camera's position is adjusted so that the point under the mouse or touch pointer
 * remains in the same world position.
 * </p>
 */
public class CameraZoom {

    private final OrthographicCamera camera;
    private final float minZoom;
    private final float maxZoom;

    /**
     * Creates a new CameraZoom controller for the specified camera.
     *
     * @param camera  the {@link OrthographicCamera} to control
     * @param minZoom the minimum allowed zoom value
     * @param maxZoom the maximum allowed zoom value
     */
    public CameraZoom(OrthographicCamera camera, float minZoom, float maxZoom) {
        this.camera = camera;
        this.minZoom = minZoom;
        this.maxZoom = maxZoom;
    }

    /**
     * Zooms the camera by a specified delta, keeping the world position under the pointer fixed.
     * <p>
     * This method should be called when processing mouse scroll or pinch gestures.
     * </p>
     *
     * @param delta    the change in zoom (positive to zoom out, negative to zoom in)
     * @param pointerX the x-coordinate of the pointer in screen coordinates
     * @param pointerY the y-coordinate of the pointer in screen coordinates
     */
    public void zoom(float delta, float pointerX, float pointerY) {
        Vector3 beforeZoom = camera.unproject(new Vector3(pointerX, pointerY, 0));

        camera.zoom += delta;
        if (camera.zoom < minZoom) camera.zoom = minZoom;
        if (camera.zoom > maxZoom) camera.zoom = maxZoom;

        Vector3 afterZoom = camera.unproject(new Vector3(pointerX, pointerY, 0));
        camera.position.add(beforeZoom.x - afterZoom.x, beforeZoom.y - afterZoom.y, 0);
    }
}
