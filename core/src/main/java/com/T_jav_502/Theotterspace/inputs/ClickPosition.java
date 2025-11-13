package com.T_jav_502.Theotterspace.inputs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.T_jav_502.Theotterspace.tiles.Selector;
public class ClickPosition {
    private final OrthographicCamera camera;
    private Vector3 lastMouse;
    public  ClickPosition(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void update(Selector selector) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            lastMouse = new Vector3(Gdx.input.getX(), 720 - Gdx.input.getY(),0);
            System.out.println("camera + mouse = x : " + ((int)(((camera.position.x + lastMouse.x) / 32) - 20)) + " y : " + ((int)(((camera.position.y + lastMouse.y)) / 32) - 11));
            selector.setCoordinate(new Vector2(((int)(((camera.position.x + lastMouse.x) / 32) - 20)), ((int)(((camera.position.y + lastMouse.y)) / 32) - 11)));        }
    }


}
