package com.T_jav_502.Theotterspace.tiles;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a "Cover" tile on the map.
 * <p>
 * This tile provides defensive advantages to units standing on it,
 * modifies movement and precision, and blocks vision.
 * </p>
 * <p>
 * Properties:
 * <ul>
 *     <li>Defense modifier: 1.1</li>
 *     <li>Movement modifier: 1.2</li>
 *     <li>Precision modifier: 1.1</li>
 *     <li>Blocks vision</li>
 *     <li>Uses texture "Tile_Cover.png"</li>
 * </ul>
 * </p>
 */
public class Cover extends aTile {

    /**
     * Constructs a Cover tile with predefined modifiers and texture.
     */
    public Cover() {
        super(1.1F, 1.2F, 1.1F);
        this.vision = false;
        texture = new Texture("Tile_Cover.png");
    }
}
