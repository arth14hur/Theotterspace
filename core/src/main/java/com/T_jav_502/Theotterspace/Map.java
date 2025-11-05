package com.T_jav_502.Theotterspace;
import com.T_jav_502.Theotterspace.tiles.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Map {

    /**
     * The class that stores all tiles.
     * It serves as a Mediator between the different classes that it contains
     * The tiles themselves store the characters and can be decorated with buildings
     * <p>
     * This class is initialized with a Theme and a Path to a file that contains the pattern of the map
     * this pattern follows the following syntax :
     * <ul>
     * <li>a "|" serves as delimiter between rows </li>
     * <li>within the rows, a "," serves as delimiter between the tiles.</li>
     * <li>for now only the symbols F, W, C and D are accepted</li>
     * <li>any other symbol will result in a null entry </li>
     * </ul>
     *
     */

    //Attributes
    public enum Theme{
        SPACESHIP,
        MARS
    }
    private final aTile[][] map;
    private final Theme theme;


    //Constructor
    public Map(Theme theme, Path mapPath){

        this.theme = theme;
        String[][] tiles = getTilesFromFile(mapPath);
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
                    default:
                        map[i][j] = null;
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
    public aTile getTile(int x, int y) {
        return map[x][y];
    }

    //Methods
    public static String[][] getTilesFromFile(Path path) {
        try {
            String content = Files.readString(path);
            String[] splited = content.split("\\s*\\|\\s*");
            String[][] tiles = new String[splited.length][];
            for  (int i = 0; i < splited.length; i++) {
                tiles[i] = splited[i].split(",");
            }
            return tiles;
        }catch (Exception e){
            System.err.println("Error reading file: " + path);
        }
        return null;
    }

    //TODO: method to evaluate distance between two tiles, (returns the distance or a negative number if not accessible)


}
