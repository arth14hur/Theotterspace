package com.T_jav_502.Theotterspace.tiles;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents a "Damaged Floor" tile on the map.
 * <p>
 * This tile slightly reduces unit movement and precision due to its damaged state,
 * but does not block vision and is walkable.
 * </p>
 * <p>
 * Properties:
 * <ul>
 *     <li>Defense modifier: 0.9</li>
 *     <li>Movement modifier: 0.9</li>
 *     <li>Precision modifier: 0.9</li>
 *     <li>Walkable and does not block vision</li>
 *     <li>Uses texture "Tile_DamagedFloor.png"</li>
 * </ul>
 * </p>
 */
public class DamagedFloor extends aTile {

    /**
     * Constructs a DamagedFloor tile with predefined modifiers and texture.
     */
    public DamagedFloor() {
        super(0.9F, 0.9F, 0.9F);  // damaged floor, movement and precision penalties
        texture = new Texture("Tile_DamagedFloor.png");
    }
}
