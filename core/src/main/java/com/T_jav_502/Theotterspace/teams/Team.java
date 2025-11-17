package com.T_jav_502.Theotterspace.teams;

import com.badlogic.gdx.utils.Array;
import com.T_jav_502.Theotterspace.units.aUnit;

public class Team {
    private Array<aUnit> units;
    private Species curentSpecies;

    public enum Species {
        OTTER,
        WOLF
    }

    public Team(Species species) {
        this.units = new Array<>();
        this.curentSpecies = species;
    }

    public void addUnit(aUnit unit) {
        units.add(unit);
    }

    /**
     * Supprime une unité de l'équipe (en cas de mort).
     * Le paramètre 'true' indique qu'on vérifie l'identité de l'objet (==) et non juste l'égalité (.equals).
     */
    public void removeUnit(aUnit unit) {
        units.removeValue(unit, true);
    }

    public Array<aUnit> getUnits() {
        return units;
    }

    public Species getCurentSpecies() {
        return curentSpecies;
    }
}
