package com.T_jav_502.Theotterspace;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.tiles.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;


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
    private Team[] teams;




    //Constructor
    public Map(String mapPath){

        //get the json file
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal(mapPath));
        JsonValue jsonMap = base.get("map");



        int sizeX = base.getInt("sizeX");
        int sizeY = base.getInt("sizeY");
        map = new aTile[sizeX][sizeY];
        int x = 0;
        int y = 0;
        for (JsonValue row : jsonMap.iterator()) {
           for ( String tile :row.asStringArray()){
               switch (tile){
                   case "W":
                       map[x][y] = new Wall();
                       break;
                   case "F":
                       map[x][y] = new Floor();
                       break;
                   case "D":
                       map[x][y] = new DamagedFloor();
                       break;
                   case "C":
                       map[x][y] = new Cover();
                       break;
                   default:
                       map[x][y] = null;
               }
               y++;
           }
           y=0;
           x++;
        }

        // directly set the theme
        this.theme = Theme.valueOf(base.getString("theme"));

        //create the different teams

        //generate map

        //create units in their team







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
