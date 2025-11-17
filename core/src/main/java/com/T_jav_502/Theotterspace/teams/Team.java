package com.T_jav_502.Theotterspace.teams;

import com.T_jav_502.Theotterspace.units.*;
import com.badlogic.gdx.utils.Array;


import java.util.ArrayList;
import java.util.List;

/**
 * Represents a team of units in the game.
 * <p>
 * Each team has a species (e.g., OTTER, WOLF) and manages a list of units
 * belonging to that team. Provides methods to add and retrieve units.
 * </p>
 */
public class Team {

    /**
     * Enum representing possible species for a team.
     */
    public enum Species {
        OTTER,
        WOLF
    }

    /** The species of this team. */
    private final Species curentSpecies;

    /** List of units that belong to this team. */
    private Array<aUnit> unitList = new Array<>();

    /**
     * Constructs a new Team with the specified species.
     *
     * @param curentSpecies the species of this team
     */
    public Team(Species curentSpecies) {
        this.curentSpecies = curentSpecies;
    }

    /**
     * Returns the species of this team.
     *
     * @return the current species
     */
    public Species getCurentSpecies() {
        return curentSpecies;
    }

    /**
     * Returns the list of units belonging to this team.
     *
     * @return list of {@link aUnit} objects
     */
    public Array<aUnit> getUnits() {
        return unitList;
    }

    /** Delete a unit
     *
     * @param unit
     */
    public void deleteUnit(aUnit unit) {
        unitList.removeValue(unit, true);
    }
    /**
     * Adds a unit to this team.
     *
     * @param unit the {@link aUnit} to add
     */
    public void addUnit(aUnit unit) {
        unitList.add(unit);
    }
}
