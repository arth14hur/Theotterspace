package com.T_jav_502.Theotterspace;

import com.badlogic.gdx.Game;
import com.T_jav_502.Theotterspace.screens.TitleScreen;
import com.badlogic.gdx.Gdx;

import java.util.Arrays;

public class Main extends Game {

    @Override
    public void create() {
        setScreen(new TitleScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        screen.dispose();
    }
}
