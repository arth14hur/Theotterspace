package com.T_jav_502.Theotterspace.tiles;
import com.badlogic.gdx.graphics.Texture;

public class Wall extends aTile {

    public Wall() {
        super(0, 0, 0);
        this.vision = false;
        texture = new Texture("Tile_Wall.png");
        this.walkable = false;
    }

}
