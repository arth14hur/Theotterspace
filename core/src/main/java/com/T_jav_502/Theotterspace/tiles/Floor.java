package com.T_jav_502.Theotterspace.tiles;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a standard "Floor" tile on the map.
 * <p>
 * This tile has neutral effects on units: it does not modify movement, defense, or precision,
 * is walkable, and does not block vision.
 * </p>
 * <p>
 * Properties:
 * <ul>
 *     <li>Defense modifier: 1.0</li>
 *     <li>Movement modifier: 1.0</li>
 *     <li>Precision modifier: 1.0</li>
 *     <li>Walkable and provides vision</li>
 *     <li>Uses texture "Tile_Floor.png"</li>
 * </ul>
 * </p>
 */
public class Floor extends aTile {

    /**
     * Constructs a Floor tile with neutral modifiers and default texture.
     */
    public Floor() {
        super(1, 1, 1);
        texture = new Texture("Tile_Floor.png");
    }
}
