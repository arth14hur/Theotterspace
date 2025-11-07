package com.T_jav_502.Theotterspace;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.tiles.*;
import com.T_jav_502.Theotterspace.units.Blaster;
import com.T_jav_502.Theotterspace.units.Heavy;
import com.T_jav_502.Theotterspace.units.Infantry;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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

        // set the tiles in the map
        JsonValue jsonMap = base.get("map");
        int sizeX = base.getInt("sizeX");
        int sizeY = base.getInt("sizeY");
        map = new aTile[sizeY][sizeX];
        int y = 0;
        int x = 0;
        for (JsonValue row : jsonMap.iterator()) {
           for ( String tile :row.asStringArray()){
               switch (tile){
                   case "W":
                       map[y][x] = new Wall();
                       break;
                   case "F":
                       map[y][x] = new Floor();
                       break;
                   case "D":
                       map[y][x] = new DamagedFloor();
                       break;
                   case "C":
                       map[y][x] = new Cover();
                       break;
                   default:
                       map[y][x] = null;
               }
               x++;
           }
           x=0;
           y++;
        }

        // directly set the theme
        this.theme = Theme.valueOf(base.getString("theme"));

        //create the different teams
        teams = new Team[2];
        int i = 0;
         for (JsonValue team :base.get("teams").iterator()){
             teams[i] = new Team(Team.Species.valueOf(team.getString("species")));
             for (JsonValue unit: team.get("units").iterator()){
                 switch (unit.getString("type")){
                     case "Blaster":
                         teams[i].addUnit(new Blaster(
                             teams[i],
                             unit.getInt("posX"),
                             unit.getInt("posY")
                             ));
                         break;
                     case "Heavy":
                         teams[i].addUnit(new Heavy(
                             teams[i],
                             unit.getInt("posX"),
                             unit.getInt("posY")
                         ));
                     case "Infantry":
                         teams[i].addUnit(new Infantry(
                             teams[i],
                             unit.getInt("posX"),
                             unit.getInt("posY")
                         ));
                 }
                 map[unit.getInt("posY")][unit.getInt("posX")].setOccupied(true);
             }
             i++;
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
        return map[y][x];
    }

    public Team[] getTeams() {
        return teams;
    }
    public Team getTeam(int teamIndex) {
        return teams[teamIndex];
    }
    //Methods

    /**
     * s
     * @return an array of positions to know where a unit can move
     * TODO: change all int[] into Vector2
     */
    public Array<Vector2> whareCanWalk(Vector2 startPos, int mvt) {
        Array<Vector2> output = new Array<>();
        output.add(startPos);
        Array<Vector2> directions = new Array<>();
        directions.add(new Vector2(0,-1));
        directions.add(new Vector2(0,1));
        directions.add(new Vector2(-1, 0));
        directions.add(new Vector2(1, 0));
        Vector2 bufferPosition = new Vector2();
        while (mvt > 0) {
            for (Vector2 position : output) {
               for (Vector2 direction : directions) {
                   bufferPosition.x = direction.x + position.x;
                   bufferPosition.y = direction.y + position.y;
                   if (bufferPosition.x > 0 && bufferPosition.y > 0 && bufferPosition.y < map.length && bufferPosition.x < map[0].length && !output.contains(bufferPosition, false)) {

                       if (map[(int) bufferPosition.y][ (int) bufferPosition.x].isWalkable() && map[(int) bufferPosition.y][ (int) bufferPosition.x] != null && !map[(int) bufferPosition.y][ (int) bufferPosition.x].isOccupied()) {
                           output.add(new Vector2 (bufferPosition.x, bufferPosition.y));
                       }

                   }
               }
            }
            mvt --;
        }
        System.out.println(output);
        return output;
    }

    public boolean canUnitMoveHere(Array<Vector2> positions, Vector2 position) {
                return positions.contains(position, false);
    }


}
