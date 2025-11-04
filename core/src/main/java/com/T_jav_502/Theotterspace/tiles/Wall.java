package com.T_jav_502.Theotterspace.tiles;
import com.T_jav_502.Theotterspace.tiles.aTile;

public class Wall extends aTile {

    public Wall(int posX, int posY) {
        super(posX, posY, 0, 0, 0);
        this.posX = posX;
        this.posY = posY;
        this.vision = false;
    }
}
