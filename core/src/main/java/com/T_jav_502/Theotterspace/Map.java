package com.T_jav_502.Theotterspace;
import com.T_jav_502.Theotterspace.tiles.*

public class Map {

    //Attributes
    public enum Theme{
        SPACESHIP,
        MARS
    }
    private final aTile[][] map;
    private final Theme theme;


    //Constructor
    public Map(Theme theme, String[][] tiles) {
        this.theme = theme;
        map = new aTile[tiles.length][tiles[0].length];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                switch (tiles[i][j]){
                    case "W":
                        map[i][j] = new Wall();
                        break;
                    case "F":
                        map[i][j] = new Floor();
                        break;
                    case "D":
                        map[i][j] = new DamagedFloor();
                        break;
                    case "C":
                        map[i][j] = new Cover();
                        break;
                }
            }
        }
    }

    //Getters
    public Theme getTheme() {
        return theme;
    }
    public aTile[][] getMap() {
        return map;
    }

}
