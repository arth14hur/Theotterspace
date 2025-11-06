package com.T_jav_502.Theotterspace.units;
import com.T_jav_502.Theotterspace.teams.*;
/**
 * This class defines the parameters of all Heavy unit.
 */
public class Heavy extends aUnit{
    public Heavy(Team.Species species, int posX, int posY) {
        super(50, 4, 8, 5, species, posX, posY);
    }
}
