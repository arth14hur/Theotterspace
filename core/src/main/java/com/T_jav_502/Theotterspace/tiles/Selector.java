package com.T_jav_502.Theotterspace.tiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.input.*;
public class Selector {
    private aTile tile;
    private Texture texture;
    private Vector2 coordinate ;
    public Selector() {
        tile = null ;
        texture = new Texture("Select2.png");
        coordinate = new Vector2(1,1);
    }

    public Vector2 getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Vector2 coordinate) {
        if (!(coordinate.x < 0 || coordinate.y < 0)) {
            this.coordinate = coordinate;
        }
        else{
            System.out.println("Invalid coordinate");
        }
    }

    public aTile getTile() {
        return tile;
    }

    public void setTile(aTile tile) {
        this.tile = tile;
    }
}
