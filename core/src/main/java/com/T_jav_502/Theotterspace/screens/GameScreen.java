package com.T_jav_502.Theotterspace.screens;

import com.T_jav_502.Theotterspace.Main;
import com.T_jav_502.Theotterspace.Map;
import com.T_jav_502.Theotterspace.teams.Team;
import com.T_jav_502.Theotterspace.units.aUnit;
import com.T_jav_502.Theotterspace.inputs.CameraDrag;
import com.T_jav_502.Theotterspace.inputs.CameraZoom;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Represents the main in-game screen where the map, units, and gameplay
 * interactions are displayed and handled. It supports camera movement,
 * zooming, and a pause menu overlay with options to resume or return
 * to the title screen.
 *
 * <p><strong>Features:</strong></p>
 * <ul>
 *   <li>Renders the tile map and game units.</li>
 *   <li>Camera drag and zoom using mouse inputs.</li>
 *   <li>Pause system with semi-transparent overlay and interactive buttons.</li>
 *   <li>ESC key toggles the pause state.</li>
 * </ul>
 */
public class GameScreen implements Screen {

    /** Reference to the main application instance */
    private final Main main;

    /** Camera used to view the game world */
    private final OrthographicCamera camera;

    /** Handles screen resizing and projection for the camera */
    private final FitViewport viewport;

    /** Renders the tiled map */
    final OrthogonalTiledMapRenderer renderer;

    /** UI stage for rendering overlays and buttons */
    private final Stage stage;

    /** Reference to the current map logic */
    private final Map map;

    /** Manages camera dragging behavior */
    private final CameraDrag cameraDrag;

    /** Manages camera zoom behavior */
    private final CameraZoom cameraZoom;

    /** Font used for UI elements */
    private final BitmapFont font = new BitmapFont();

    /** Renderer used for drawing the semi-transparent pause overlay */
    private final ShapeRenderer shapeRenderer;

    /** Minimum, maximum, and step values for zoom */
    private final float MIN_ZOOM = 0.3f, MAX_ZOOM = 3f, ZOOM_STEP = 0.1f;

    /** Indicates whether the game is currently paused */
    private boolean isPaused = false;

    /** UI elements for pause display */
    private Label pauseLabel;
    private TextButton continueButton, quitButton;

    /**
     * Constructs the main game screen.
     *
     * @param main  Reference to the main application
     * @param tiledMap The Tiled map to render
     * @param map The logical map object containing teams and units
     * @param scale Scaling factor applied to the tile map renderer
     */
    public GameScreen(Main main, TiledMap tiledMap, Map map, int scale) {
        this.main = main;
        this.map = map;
        this.renderer = new OrthogonalTiledMapRenderer(tiledMap, scale);

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(1280, 720, camera);
        viewport.apply();
        camera.position.set(1280 / 2f, 720 / 2f, 0);

        this.stage = new Stage(new FitViewport(1280, 720));

        this.cameraDrag = new CameraDrag(camera);
        this.cameraZoom = new CameraZoom(camera, MIN_ZOOM, MAX_ZOOM);
        this.shapeRenderer = new ShapeRenderer();

        createUI();
        setupInput();
    }

    /**
     * Initializes the pause menu UI elements (label and buttons)
     * and places them at the center of the screen.
     */
    private void createUI() {
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        // Pause label
        pauseLabel = new Label("PAUSE", style);
        pauseLabel.setFontScale(2f);
        pauseLabel.setVisible(false);

        // Buttons
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;

        continueButton = new TextButton("Continue", buttonStyle);
        quitButton = new TextButton("Return to Menu", buttonStyle);

        continueButton.setVisible(false);
        quitButton.setVisible(false);

        // Button listeners
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause(false);
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main.setScreen(new TitleScreen(main));
            }
        });

        // Layout
        Table pauseTable = new Table();
        pauseTable.center();
        pauseTable.setFillParent(true);
        pauseTable.add(pauseLabel).padBottom(30).row();
        pauseTable.add(continueButton).width(200).height(40).padBottom(10).row();
        pauseTable.add(quitButton).width(200).height(40);
        stage.addActor(pauseTable);
    }

    /**
     * Configures input handling for camera controls and pause toggling.
     * Includes UI input processing and ESC key detection.
     */
    private void setupInput() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (!isPaused) cameraDrag.update();
                return true;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (!isPaused)
                    cameraZoom.zoom(amountY * 0.1f, Gdx.input.getX(), Gdx.input.getY());
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.ESCAPE) {
                    togglePause(!isPaused);
                }
                return true;
            }
        });
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Toggles the paused state and updates the visibility
     * of UI elements accordingly.
     *
     * @param pause true to enable pause, false to resume the game
     */
    private void togglePause(boolean pause) {
        isPaused = pause;
        pauseLabel.setVisible(pause);
        continueButton.setVisible(pause);
        quitButton.setVisible(pause);
    }

    /**
     * Renders the game scene and UI each frame.
     *
     * @param delta Time elapsed since the last frame in seconds
     */
    @Override
    public void render(float delta) {
        camera.update();
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        renderer.setView(camera);
        renderer.render();

        // Draw units if not paused
        if (!isPaused) {
            renderer.getBatch().begin();
            for (Team team : map.getTeams()) {
                for (aUnit unit : team.getUnits()) {
                    unit.draw(renderer.getBatch());
                }
            }
            renderer.getBatch().end();
        }

        // Dimmed overlay when paused
        if (isPaused) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 0.5f);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set(width / 2f, height / 2f, 0);
        stage.getViewport().update(width, height, true);
    }

    @Override public void show() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    /** Disposes of all resources used by the screen. */
    @Override
    public void dispose() {
        renderer.getMap().dispose();
        renderer.dispose();
        stage.dispose();
        font.dispose();
        shapeRenderer.dispose();
    }
}
