package com.T_jav_502.Theotterspace.tiles;

import com.badlogic.gdx.graphics.Texture;

public class Cover extends aTile {

        public Cover() {
            super( 1.1F, 1.2F, 1.1F);
            this.vision = false;
            texture = new Texture("Tile_Cover.png");
        }
}

