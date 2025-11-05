package com.T_jav_502.Theotterspace.tiles;

import com.T_jav_502.Theotterspace.units.*;
import com.badlogic.gdx.graphics.Texture;

abstract public class aTile {

    protected float modMvt;
    protected float modDef;
    protected float modPre;
    protected boolean vision = true;
    protected aUnit Unit;
    protected boolean walkable = true;
    protected Texture texture;

    public aTile ( float modDef, float modMvt, float modPre) {
        this.modDef = modDef;
        this.modMvt = modMvt;
        this.modPre = modPre;
    }


    public float getModMvt() {
        return modMvt;
    }

    public float getModPre() {
        return modPre;
    }

    public float getModDef() {
        return modDef;
    }

    public boolean isVision() {
        return vision;
    }

    public aUnit getUnit() {
        return Unit;
    }

    public boolean isWalkable(){return walkable;}

    public Texture getTexture() {
        return texture;
    }
}
