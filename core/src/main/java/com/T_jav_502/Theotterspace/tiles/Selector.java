package com.T_jav_502.Theotterspace.tiles;

import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.input.*;
public class Selector {

    private boolean display = true;
    private Vector2 coordinate ;
    public Selector() {

        coordinate = new Vector2(1,1);
    }

    public Vector2 getCoordinates() {
        return coordinate;
    }

    /**
     * bouge les coordonnées du selecteur
     * bloque le curseur si il essaye de sortir
     * @param coordinate
     */
    //to do faire en sorte que le curseur ne dépasse pas la taille max de l'ecran
    public void setCoordinates(Vector2 coordinate, float maxX, float maxY) {

        if (!(coordinate.x < 0 || coordinate.x >= maxX || coordinate.y < 0 || coordinate.y >= maxY)) {
            this.coordinate = coordinate;
            if (!display) {
                setDisplay(true);
            }
        }
        else{
            setDisplay(false);
        }

    }
    public boolean isDisplay() {
        return display;
    }

    public  void setDisplay(boolean display) {
        this.display = display;
    }

}
