package com.T_jav_502.Theotterspace;

import com.badlogic.gdx.Game;
import com.T_jav_502.Theotterspace.screens.TitleScreen;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new TitleScreen(this));
    }
}
