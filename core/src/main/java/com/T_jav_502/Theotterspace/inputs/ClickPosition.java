package com.T_jav_502.Theotterspace.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.T_jav_502.Theotterspace.tiles.Selector;

public class ClickPosition {

    private final OrthographicCamera camera;

    public ClickPosition(OrthographicCamera camera) {
        this.camera = camera;
    }

    public Vector2 update() {
        Vector3 screenCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        camera.unproject(screenCoords);

        int tileX = (int) (screenCoords.x / 32);
        int tileY = (int) (screenCoords.y / 32);


        return new Vector2(tileX, tileY);

    }
}
