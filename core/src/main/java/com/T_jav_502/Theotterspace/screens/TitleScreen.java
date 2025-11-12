package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.T_jav_502.Theotterspace.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * The {@code TitleScreen} represents the main menu displayed when the game launches.
 * <p>
 * It provides the player with two main options by clicking on a background image:
 * </p>
 * <ul>
 * <li><b>Start Game</b> — loads a {@link TiledMap} and a {@link Map} instance, then transitions to {@link GameScreen}.</li>
 * <li><b>Quit</b> — exits the application.</li>
 * </ul>
 *
 * <p>
 * The screen uses LibGDX’s {@link Stage} and Scene2D UI system.
 * It renders a full-screen {@link Image} and overlays invisible {@link Button}
 * actors managed by a {@link Table} for input handling.
 * </p>
 *
 * <h3>Usage</h3>
 * <pre>{@code
 * Main game = new Main();
 * game.setScreen(new TitleScreen(game));
 * }</pre>
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 * <li>Graphical main menu using a background image.</li>
 * <li>Scene2D-based input handling via invisible buttons.</li>
 * <li>Clean transition to {@link GameScreen} when starting.</li>
 * </ul>
 *
 * @see GameScreen
 * @see com.badlogic.gdx.scenes.scene2d.Stage
 * @see com.badlogic.gdx.Screen
 */
public class TitleScreen implements Screen {

    private final Main game;
    private Stage stage;
    /** Stores the background texture. */
    private Texture backgroundTexture;

    /**
     * Constructs a new {@code TitleScreen}.
     *
     * @param game the main {@link Main} instance controlling the screen flow
     */
    public TitleScreen(Main game) {
        this.game = game;
    }

    /**
     * Initializes the screen when it becomes visible.
     * <p>
     * Loads the background texture and sets up the stage with
     * invisible buttons positioned over the background image.
     * </p>
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        backgroundTexture = new Texture(Gdx.files.internal("TitleScreen.png"));
        Image background = new Image(backgroundTexture);
        background.setScaling(Scaling.fill);
        background.setFillParent(true);
        stage.addActor(background);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();

        Button startButton = new Button(buttonStyle);
        Button quitButton = new Button(buttonStyle);

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                TiledMap tiledMap = new TmxMapLoader().load("Maps/test.tmx");
                //Map map = new Map("../assets/Maps/mapTuto.json");
                game.setScreen(new GameScreen(game, tiledMap, 4));
            }
        });

        // table.setDebug(true);

        float buttonSize = 300f;
        float buttonPadding = 50f;

        table.add(startButton).size(buttonSize).padRight(buttonPadding);
        table.add(quitButton).size(buttonSize).padLeft(buttonPadding);
    }

    /**
     * Renders the screen every frame.
     * <p>
     * Clears the screen, updates the stage, and draws all actors.
     * </p>
     *
     * @param delta the time elapsed since the last frame (in seconds)
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    /**
     * Updates the viewport dimensions when the window is resized.
     *
     * @param width  the new width of the window
     * @param height the new height of the window
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /**
     * Releases all LibGDX resources used by this screen.
     * <p>
     * Disposes of the {@link Stage} and the {@link Texture} to prevent memory leaks.
     * </p>
     */
    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
    }
}
