package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.T_jav_502.Theotterspace.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * The {@code TitleScreen} represents the main menu displayed when the game launches.
 * <p>
 * It provides the player with two main options:
 * </p>
 * <ul>
 *     <li><b>Start Game</b> — loads a {@link TiledMap} and a {@link Map} instance, then transitions to {@link GameScreen}.</li>
 *     <li><b>Quit</b> — exits the application.</li>
 * </ul>
 *
 * <p>
 * The screen uses LibGDX’s {@link Stage} and Scene2D UI system
 * ({@link Label}, {@link TextButton}, and {@link Table}) for rendering
 * and input handling. The interface is minimal and purely text-based.
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
 *   <li>Simple text-based main menu (no external UI skin required).</li>
 *   <li>Scene2D-based button input handling.</li>
 *   <li>Clean transition to {@link GameScreen} when starting.</li>
 * </ul>
 *
 * @see GameScreen
 * @see com.badlogic.gdx.scenes.scene2d.Stage
 * @see com.badlogic.gdx.Screen
 */
public class TitleScreen implements Screen {

    /** Reference to the main game instance controlling screen transitions. */
    private final Main game;

    /** Stage used for managing UI actors and handling input. */
    private Stage stage;

    /** Sprite batch used for rendering text and UI. */
    private SpriteBatch batch;

    /** Font used for rendering the title and button text. */
    private BitmapFont font;

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
     * Creates and configures all UI components (title label, buttons)
     * and registers input processors with the {@link Stage}.
     * </p>
     */
    @Override
    public void show() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        LabelStyle labelStyle = new LabelStyle();
        labelStyle.font = font;

        Label title = new Label("THE OTTER SPACE", labelStyle);

        TextButtonStyle buttonStyle = new TextButtonStyle();
        buttonStyle.font = font;

        TextButton startButton = new TextButton("Commencer", buttonStyle);
        TextButton quitButton = new TextButton("Quitter", buttonStyle);

        // Quit button listener
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Start button listener
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                TiledMap tiledMap = new TmxMapLoader().load("Maps/test.tmx");
                Map map = new Map("../assets/Maps/mapTuto.json");
                game.setScreen(new GameScreen(game, tiledMap, map, 4));
            }
        });

        table.add(title).padBottom(40);
        table.row();
        table.add(startButton).width(200).height(40).padBottom(20);
        table.row();
        table.add(quitButton).width(200).height(40);
    }

    /**
     * Renders the screen every frame.
     * <p>
     * Clears the screen, updates stage actors, and draws the UI.
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
     * Should be called when the screen is no longer needed to avoid memory leaks.
     * </p>
     */
    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        batch.dispose();
    }
}
