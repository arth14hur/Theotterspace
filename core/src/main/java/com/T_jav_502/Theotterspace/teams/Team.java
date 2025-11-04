package com.T_jav_502.Theotterspace.teams;
import  com.T_jav_502.Theotterspace.units.*;

import java.util.ArrayList;
import java.util.List;

import static com.T_jav_502.Theotterspace.teams.Team.Species.OTTER;

public class Team {
    public Team(Species curentSpecies) {
        this.curentSpecies = curentSpecies;
    }

    public enum Species {
        OTTER,
        WOLF
    }
    private final Species curentSpecies;

    private List<aUnit> unitList = new ArrayList<aUnit>() ;




    public Species getCurentSpecies() {
        return curentSpecies;
    }

    public List<aUnit> getUnits() {
        return unitList;
    }
}
