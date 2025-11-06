package com.T_jav_502.Theotterspace;

import com.badlogic.gdx.Game;
import com.T_jav_502.Theotterspace.screens.GameScreen;

import java.nio.file.Path;

public class Main extends Game {

    @Override
    public void create() {

        // Charge un fichier de map (à placer dans assets/maps/)
        Path mapPath = Path.of("assets/Maps/mapTuto.txt");

        Map map = new Map(Map.Theme.SPACESHIP, mapPath);

        setScreen(new GameScreen(map));
    }
}
