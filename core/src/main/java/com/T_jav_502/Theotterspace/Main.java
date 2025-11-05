package com.T_jav_502.Theotterspace;

import com.badlogic.gdx.Game;
import com.T_jav_502.Theotterspace.screens.GameScreen;

public class Main extends Game {

    @Override
    public void create() {

        // Exemple de map (tu remplaceras ça par ta vraie map)
        String[][] tiles = {
            {"F", "F", "W", "F"},
            {"D", "g", "W"},
            {"F", "F", "F"}
        };

        Map map = new Map(Map.Theme.SPACESHIP, tiles);

        // Lance le GameScreen
        setScreen(new GameScreen(map));
    }
}
