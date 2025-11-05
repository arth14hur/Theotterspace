package com.T_jav_502.Theotterspace;

import com.T_jav_502.Theotterspace.tiles.Cover;
import com.T_jav_502.Theotterspace.tiles.Floor;
import com.T_jav_502.Theotterspace.tiles.Wall;
import com.T_jav_502.Theotterspace.tiles.aTile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {
    Path pathToMap = Paths.get("../assets/map01.txt");
    Map map;
    @BeforeEach
    void setUp() {
        map = new Map(Map.Theme.SPACESHIP,pathToMap);
    }
    @Test
    void getTheme() {
        assertEquals(Map.Theme.SPACESHIP, map.getTheme());
    }

    @Test
    void getMap() {
        System.out.println(Arrays.deepToString(map.getMap()));
        assertNotNull(map.getMap());
    }

    @Test
    void getTile() {
        assertInstanceOf(Wall.class, map.getTile(0, 0));
//        assertInstanceOf(Floor.class, map.getTile(2, 2));
        assertInstanceOf(Cover.class, map.getTile(1, 2));

    }

    @Test
    void getTilesFromFile() {
        assertEquals("[[W, W, W, W, W], [W, F, C, D, W], [W, F, F, F, W], [W, D, C, F, W], [W, W, W, W, W\n" +
                "]]" ,Arrays.deepToString(Map.getTilesFromFile(pathToMap)));
        assertEquals("W", Objects.requireNonNull(Map.getTilesFromFile(pathToMap))[0][0]);
    }
}
