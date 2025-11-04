package com.T_jav_502.Theotterspace.tiles;

import com.T_jav_502.Theotterspace.units;

abstract public class aTile {

    protected int posX;
    protected int posY;
    protected float modMvt;
    protected float modDef;
    protected float modPre;
    protected boolean vision = true;
    protected aUnit Unit;

    public aTile (int posX, int posY, float modDef, float modMvt, float modPre) {
        this.posX = posX;
        this.posY = posY;
        this.modDef = modDef;
        this.modMvt = modMvt;
        this.modPre = modPre;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
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
}
