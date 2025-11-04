package com.T_jav_502.Theotterspace.buildings;

import com.T_jav_502.Theotterspace.tiles.aTile;

public abstract class aFactory extends aBuilding{
    public aFactory(aTile tile , Team owner ) {
        super(tile , owner , 50);
    }
    //To Do
    //Donner une variable enum pour savoir quel type de building le Factory
    //Faire des actions différentes pour le type de factory sélectionné.
}
