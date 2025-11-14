package com.T_jav_502.Theotterspace.units;
import com.T_jav_502.Theotterspace.teams.*;
/**
 * This class defines the parameters of all Blaster unit.
 */
public class Blaster extends aUnit{
    public Blaster(Team team, int posX, int posY) {
        super(20, 5, 4, 10 , team, posX, posY,"Blaster");
        this.range = 2;
    }
}
