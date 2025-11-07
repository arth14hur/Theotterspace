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
    String pathToMap = "../assets/Maps/map01.txt";
    Map map;
    @BeforeEach
    void setUp() {
        map = new Map(pathToMap);
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
        assertInstanceOf(Wall.class, map.getTile(1, 0));
//        assertInstanceOf(Floor.class, map.getTile(2, 2));
        assertInstanceOf(Cover.class, map.getTile(1, 2));

    }
}
