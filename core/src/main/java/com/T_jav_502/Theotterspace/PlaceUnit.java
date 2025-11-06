package com.T_jav_502.Theotterspace;

import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.tiles.aTile;
import com.T_jav_502.Theotterspace.units.Infantry;
import com.T_jav_502.Theotterspace.units.*;
import sun.jvm.hotspot.gc.shared.Space;

import java.util.ArrayList;
import java.util.List;

import static com.T_jav_502.Theotterspace.teams.Team.Species.*;

public class PlaceUnit {

    private List<Team> teams = new ArrayList<>();
    private aUnit[][] map;

    public PlaceUnit() {
        teams.add(new Team(OTTER));
        teams.get(0).addUnit(new Infantry(OTTER, 1 , 1));
        teams.add(new Team(WOLF));
        teams.get(1).addUnit(new Infantry(WOLF , 1 , 2));
        for (Team team : teams) {
            for(aUnit unit : team.getUnits()) {
                map[unit.getPosX()][unit.getPosY()] = unit ;
            }
        }
    }
    public aUnit[][] getPlace () {
        return map ;
    }
    public aUnit getaUnit (int posX, int posY) {
        return map[posX][posY];
    }

    /**
     * public List<aUnit> getUnits(Space space) {
     *         if (space.equals(OTTER)) {
     *             return teams.get(0).getUnits();
     *         } else if (space.equals(WOLF)) {
     *             return teams.get(1).getUnits();
     *         }
     *         else{
     *             return null;
     *         }
     *     }
     * @param space
     * @return
     */


}
