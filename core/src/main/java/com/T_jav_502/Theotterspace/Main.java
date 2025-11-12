package com.T_jav_502.Theotterspace;

import com.badlogic.gdx.Game;
import com.T_jav_502.Theotterspace.screens.TitleScreen;
import com.badlogic.gdx.Gdx;

/**
 * The main entry point for the game.
 * <p>
 * This class extends {@link Game} and is responsible for initializing the application,
 * setting the initial screen, and handling disposal of resources.
 * </p>
 * <p>
 * On creation, it sets the {@link TitleScreen} as the starting screen.
 * </p>
 */
public class Main extends Game {

    /**
     * Called when the application is first created.
     * Sets the initial screen to {@link TitleScreen}.
     */
    @Override
    public void create() {
        setScreen(new TitleScreen(this));
    }

    /**
     * Called when the application is disposed.
     * Ensures that the current screen and other resources are properly released.
     */
    @Override
    public void dispose() {
        super.dispose();
        screen.dispose();
    }
}
