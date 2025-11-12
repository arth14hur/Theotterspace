package com.T_jav_502.Theotterspace.tiles;

import com.T_jav_502.Theotterspace.units.*;
import com.badlogic.gdx.graphics.Texture;

/**
 * Abstract base class representing a tile on the game map.
 * <p>
 * Tiles define properties that affect units, such as movement cost, defense, and precision modifiers.
 * They also store visual representation (texture) and occupancy/visibility status.
 * </p>
 * <p>
 * Subclasses should define specific types of tiles (e.g., floor, wall, water) and their behaviors.
 * </p>
 */
abstract public class aTile {

    /** Modifier for unit movement on this tile (higher = slower movement). */
    protected float modMvt;

    /** Modifier for unit defense when occupying this tile (higher = better defense). */
    protected float modDef;

    /** Modifier for unit precision or accuracy when interacting with this tile (higher = better precision). */
    protected float modPre;

    /** Whether this tile provides vision (e.g., not blocking line of sight). */
    protected boolean vision = true;

    /** Whether units can walk on this tile. */
    protected boolean walkable = true;

    /** Texture used to visually represent the tile. */
    protected Texture texture;

    /** Whether the tile is currently occupied by a unit. */
    protected boolean occupied = false;

    /**
     * Constructs a tile with specified defense, movement, and precision modifiers.
     *
     * @param modDef defense modifier for the tile
     * @param modMvt movement modifier for the tile
     * @param modPre precision modifier for the tile
     */
    public aTile(float modDef, float modMvt, float modPre) {
        this.modDef = modDef;
        this.modMvt = modMvt;
        this.modPre = modPre;
    }

    /** @return the movement modifier of this tile */
    public float getModMvt() {
        return modMvt;
    }

    /** @return the precision modifier of this tile */
    public float getModPre() {
        return modPre;
    }

    /** @return the defense modifier of this tile */
    public float getModDef() {
        return modDef;
    }

    /** @return true if this tile provides vision */
    public boolean isVision() {
        return vision;
    }

    /** @return true if this tile is currently occupied by a unit */
    public boolean isOccupied() {
        return occupied;
    }

    /**
     * Sets whether this tile is occupied by a unit.
     *
     * @param occupied true if occupied, false otherwise
     */
    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    /** @return true if units can walk on this tile */
    public boolean isWalkable() {
        return walkable;
    }

    /** @return the texture representing this tile */
    public Texture getTexture() {
        return texture;
    }
}
