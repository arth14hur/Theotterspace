package com.T_jav_502.Theotterspace.teams;

import com.badlogic.gdx.utils.Array;
import com.T_jav_502.Theotterspace.units.aUnit;

public class Team {
    private Array<aUnit> units;
    private Species curentSpecies;
    private boolean isAi; // NOUVEAU : Est-ce une IA ?

    public enum Species {
        OTTER,
        WOLF
    }

    // Constructeur modifié
    public Team(Species species, boolean isAi) {
        this.units = new Array<>();
        this.curentSpecies = species;
        this.isAi = isAi;
    }

    public void addUnit(aUnit unit) {
        units.add(unit);
    }

    public void removeUnit(aUnit unit) {
        units.removeValue(unit, true);
    }

    public Array<aUnit> getUnits() { return units; }
    public Species getCurentSpecies() { return curentSpecies; }

    // Getter pour l'IA
    public boolean isAi() { return isAi; }
}
