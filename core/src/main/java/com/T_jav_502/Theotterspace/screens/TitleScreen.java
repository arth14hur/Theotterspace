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
 * Represents the title screen of the game.
 * <p>
 * This screen displays the game title and provides buttons to start the game or quit the application.
 * It uses {@link Stage} and Scene2D UI elements ({@link Label}, {@link TextButton}, {@link Table})
 * for layout and input handling.
 * </p>
 * <p>
 * The start button initializes the game by loading a TiledMap and a Map object, then sets
 * the {@link GameScreen} as the current screen.
 * </p>
 * <p>
 * The quit button exits the application.
 * </p>
 */
public class TitleScreen implements Screen {

    private final Main game;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font;

    /**
     * Creates a new TitleScreen for the given game.
     *
     * @param game the main {@link Main} game instance
     */
    public TitleScreen(Main game) {
        this.game = game;
    }

    /**
     * Initializes the screen, sets up UI elements, and registers input processors.
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
                game.setScreen(new GameScreen(tiledMap, map, 4));
            }
        });

        table.add(title).padBottom(40);
        table.row();
        table.add(startButton).width(200).height(40).padBottom(20);
        table.row();
        table.add(quitButton).width(200).height(40);
    }

    /**
     * Renders the title screen.
     *
     * @param delta time in seconds since the last frame
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /**
     * Disposes resources used by this screen.
     */
    @Override
    public void dispose() {
        stage.dispose();
        font.dispose();
        batch.dispose();
    }
}
