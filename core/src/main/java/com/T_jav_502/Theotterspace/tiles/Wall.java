package com.T_jav_502.Theotterspace.tiles;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a "Wall" tile on the map.
 * <p>
 * This tile blocks movement and vision, providing an impassable obstacle
 * for units. It has no modifiers for defense, movement, or precision.
 * </p>
 * <p>
 * Properties:
 * <ul>
 *     <li>Defense modifier: 0.0</li>
 *     <li>Movement modifier: 0.0</li>
 *     <li>Precision modifier: 0.0</li>
 *     <li>Blocks vision</li>
 *     <li>Not walkable</li>
 *     <li>Uses texture "Tile_Wall.png"</li>
 * </ul>
 * </p>
 */
public class Wall extends aTile {

    /**
     * Constructs a Wall tile with no modifiers, blocks vision, and is not walkable.
     */
    public Wall() {
        super(0, 0, 0);
        this.vision = false;
        texture = new Texture("Tile_Wall.png");
        this.walkable = false;
    }
}
